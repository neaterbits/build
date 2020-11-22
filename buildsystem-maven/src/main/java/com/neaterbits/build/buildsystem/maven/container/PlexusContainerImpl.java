package com.neaterbits.build.buildsystem.maven.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.exception.ComponentLifecycleException;
import org.codehaus.plexus.configuration.PlexusConfigurationException;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;
import org.eclipse.sisu.plexus.Hints;

public final class PlexusContainerImpl extends BaseContainerImpl implements PlexusContainer {

    private static class Component {
        private final String hint;
        private final Object component;
        
        Component(String hint, Object component) {

            Objects.requireNonNull(hint);
            Objects.requireNonNull(component);
            
            this.hint = hint;
            this.component = component;
        }
    }
    
    private final Map<Object, Object> contextMap;
    
    private final Context context;
    
    private final Map<String, List<Component>> components;
    
    public PlexusContainerImpl() {

        this.contextMap = new HashMap<>();
        
        this.context = new Context() {

            @Override
            public void put(Object key, Object value) {

                Objects.requireNonNull(key);

                synchronized (this) {
                    contextMap.put(key, value);
                }
            }
            
            @Override
            public Map<Object, Object> getContextData() {

                final Map<Object, Object> map;
                
                synchronized (this) {
                    map = Collections.unmodifiableMap(new HashMap<>(contextMap));
                }
                
                return map;
            }
            
            @Override
            public Object get(Object key) throws ContextException {

                Objects.requireNonNull(key);

                final Object value;
                
                synchronized (this) {
                    value = contextMap.get(key);
                }
                
                return value;
            }
            
            @Override
            public boolean contains(Object key) {
                
                Objects.requireNonNull(key);
                
                final boolean contains;
                
                synchronized (this) {
                    contains = contextMap.containsKey(key);
                }
                
                return contains;
            }
        };
        
        this.components = new HashMap<>();
    }
    
    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public List<ComponentDescriptor<?>> discoverComponents(ClassRealm classRealm) throws PlexusConfigurationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ClassRealm getContainerRealm() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ClassRealm setLookupRealm(ClassRealm realm) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ClassRealm getLookupRealm() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ClassRealm createChildRealm(String id) {
        throw new UnsupportedOperationException();
    }
    
    private void releaseObject(Object component) {
        
        Objects.requireNonNull(component);
        
    }

    @Override
    public void release(Object component) throws ComponentLifecycleException {

        synchronized (this) {
            releaseObject(component);
        }
    }

    @Override
    public void releaseAll(Map<String, ?> components) throws ComponentLifecycleException {
        
        synchronized (this) {
            components.values().forEach(this::releaseObject);
        }
    }

    @Override
    public void releaseAll(List<?> components) throws ComponentLifecycleException {
        
        synchronized (this) {
            components.forEach(this::releaseObject);
        }
    }

    @Override
    public void dispose() {
        throw new UnsupportedOperationException();
    }

    private List<Component> lookupComponents(String role) {
        
        Objects.requireNonNull(role);
        
        return components.get(role);
    }
    
    @Override
    Object lookupObject(Class<?> type, String role, String hint) {
        
        final Object object;
        
        synchronized (this) {

            final List<Component> list = lookupComponents(role);
            
            final String hintToFind = hintToFind(hint);
            
            object = list == null || list.isEmpty()
                    ? null
                    : list.stream()
                        .filter(c -> c.hint.equals(hintToFind))
                        .findFirst()
                        .map(c -> c.component)
                        .orElse(null);
        }
        
        return object;
    }

    @Override
    List<Object> lookupObjectList(String role) {

        final List<Object> objectList;
        
        synchronized (this) {
            final List<Component> list = lookupComponents(role);
    
            objectList = list == null || list.isEmpty()
                    ? null
                    : list.stream()
                        .map(c -> c.component)
                        .collect(Collectors.toUnmodifiableList());
        }
        
        return objectList;
    }

    @Override
    Map<String, Object> lookupObjectMap(String role) {

        final Map<String, Object> map;
        
        synchronized (this) {
            final List<Component> list = lookupComponents(role);
    
            map = list == null || list.isEmpty()
                    ? null
                    : list.stream()
                        .collect(Collectors.toUnmodifiableMap(c -> c.hint, c -> c.component));
        }

        return map;
    }

    @Override
    void addComponentObject(Object component, String role, String hint) {

        Objects.requireNonNull(component);
        Objects.requireNonNull(role);
        Objects.requireNonNull(hint);
        
        synchronized (this) {
            if (hasComponent(role, hint)) {
                throw new IllegalStateException();
            }
            
            List<Component> list = components.get(role);
            
            if (list == null) {
                list = new ArrayList<>();
                
                components.put(role, list);
            }

            list.add(new Component(hint, component));
        }
    }

    @Override
    ComponentDescriptor<?> getComponentObjectDescriptor(Class<?> type, String role, String hint) {
        
        final List<ComponentDescriptor<?>> descriptors = getComponentDescriptorList(role);
        
        final String hintToFind = hintToFind(hint);

        return descriptors == null || descriptors.isEmpty()
                ? null
                : descriptors.stream()
                    .filter(d -> d.getRoleHint().equals(hintToFind))
                    .findFirst()
                    .orElse(null);
    }

    @Override
    List<ComponentDescriptor<?>> getComponentObjectDescriptorList(Class<?> type, String role) {
        throw new UnsupportedOperationException();
    }

    @Override
    Map<String, ComponentDescriptor<?>> getComponentObjectDescriptorMap(Class<?> type, String role) {
        
        final List<ComponentDescriptor<?>> descriptors = getComponentDescriptorList(role);

        return descriptors == null || descriptors.isEmpty()
                ? null
                : descriptors.stream()
                    .collect(
                            Collectors.toUnmodifiableMap(
                                            ComponentDescriptor::getRoleHint,
                                            Function.identity()));
    }
    
    private static String hintToFind(String hint) {
        return hint != null ? hint : Hints.DEFAULT_HINT;
    }
}
