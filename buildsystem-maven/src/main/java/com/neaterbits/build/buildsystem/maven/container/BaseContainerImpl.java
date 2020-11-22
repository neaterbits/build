package com.neaterbits.build.buildsystem.maven.container;

import java.util.List;
import java.util.Map;

import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.composition.CycleDetectedInComponentGraphException;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

public abstract class BaseContainerImpl implements PlexusContainer {

    abstract Object lookupObject(Class<?> type, String role, String hint);

    abstract List<Object> lookupObjectList(String role);
    
    abstract Map<String, Object> lookupObjectMap(String role);
   
    abstract void addComponentObject(Object component, String role, String hint);

    abstract ComponentDescriptor<?> getComponentObjectDescriptor(Class<?> type, String role, String hint);
    
    abstract List<ComponentDescriptor<?>> getComponentObjectDescriptorList(Class<?> type, String role);

    abstract Map<String, ComponentDescriptor<?>> getComponentObjectDescriptorMap(Class<?> type, String role);

    @Override
    public final Object lookup(String role) throws ComponentLookupException {
        
        final Object object = lookupObject(null, role, null);
        
        if (object == null) {
            throw new ComponentLookupException("No such component", role, null);
        }
        
        return object;
    }

    @Override
    public final Object lookup(String role, String hint) throws ComponentLookupException {

        final Object object = lookupObject(null, role, hint);
        
        if (object == null) {
            throw new ComponentLookupException("No such component", role, hint);
        }
        
        return object;
    }

    @Override
    public final <T> T lookup(Class<T> role) throws ComponentLookupException {
        
        @SuppressWarnings("unchecked")
        final T object = (T)lookupObject(null, classRole(role), null);

        if (object == null) {
            throw new ComponentLookupException("No such component", classRole(role), null);
        }

        return object;
    }

    @Override
    public final <T> T lookup(Class<T> role, String hint) throws ComponentLookupException {
        
        @SuppressWarnings("unchecked")
        final T object = (T)lookupObject(null, classRole(role), hint);

        if (object == null) {
            throw new ComponentLookupException("No such component", classRole(role), hint);
        }

        return object;
    }

    @Override
    public final <T> T lookup(Class<T> type, String role, String hint) throws ComponentLookupException {

        @SuppressWarnings("unchecked")
        final T object = (T)lookupObject(type, role, hint);

        if (object == null) {
            throw new ComponentLookupException("No such component", role, hint);
        }

        return object;
    }

    @Override
    public final List<Object> lookupList(String role) throws ComponentLookupException {

        final List<Object> list = lookupObjectList(role);
        
        if (list == null || list.isEmpty()) {
            throw new ComponentLookupException("No such component", role, null);
        }
        
        return list;
    }

    @Override
    public final <T> List<T> lookupList(Class<T> role) throws ComponentLookupException {
        
        @SuppressWarnings("unchecked")
        final List<T> list = (List<T>)lookupObjectList(classRole(role));

        if (list == null || list.isEmpty()) {
            throw new ComponentLookupException("No such component", classRole(role), null);
        }
        
        return list;
    }

    @Override
    public final Map<String, Object> lookupMap(String role) throws ComponentLookupException {

        final Map<String, Object> map = lookupObjectMap(role);
        
        if (map == null || map.isEmpty()) {
            throw new ComponentLookupException("No such component", role, null);
        }
        
        return map;
    }

    @Override
    public final <T> Map<String, T> lookupMap(Class<T> role) throws ComponentLookupException {

        @SuppressWarnings("unchecked")
        final Map<String, T> map = (Map<String, T>)lookupObjectMap(classRole(role));
        
        if (map == null || map.isEmpty()) {
            throw new ComponentLookupException("No such component", classRole(role), null);
        }
        
        return map;
    }

    @Override
    public final boolean hasComponent(String role) {
        
        final List<Object> objects = lookupObjectList(role);
        
        return objects != null && !objects.isEmpty();
    }

    @Override
    public final boolean hasComponent(String role, String hint) {
        return lookupObject(null, role, hint) != null;
    }

    @Override
    public final boolean hasComponent(Class<?> role) {
        return lookupObject(null, classRole(role), null) != null;
    }

    @Override
    public final boolean hasComponent(Class<?> role, String hint) {
        return lookupObject(null, classRole(role), hint) != null;
    }

    @Override
    public final boolean hasComponent(Class<?> type, String role, String hint) {
        return lookupObject(type, role, hint) != null;
    }

    @Override
    public final void addComponent(Object component, String role) {
        addComponentObject(component, role, null);
    }

    @Override
    public final <T> void addComponent(T component, Class<?> role, String hint) {
        addComponentObject(component, classRole(role), hint);
    }

    @Override
    public final <T> void addComponentDescriptor(ComponentDescriptor<T> descriptor)
            throws CycleDetectedInComponentGraphException {

        throw new UnsupportedOperationException();
    }

    @Override
    public final ComponentDescriptor<?> getComponentDescriptor(String role, String hint) {
        return getComponentObjectDescriptor(null, role, hint);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T> ComponentDescriptor<T> getComponentDescriptor(Class<T> type, String role, String hint) {
        return (ComponentDescriptor<T>)getComponentObjectDescriptor(type, role, hint);
    }

    @Override
    public final List<ComponentDescriptor<?>> getComponentDescriptorList(String role) {
        return getComponentObjectDescriptorList(null, role);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final <T> List<ComponentDescriptor<T>> getComponentDescriptorList(Class<T> type, String role) {
        return (List<ComponentDescriptor<T>>)(List)getComponentObjectDescriptorList(type, role);
    }

    @Override
    public final Map<String, ComponentDescriptor<?>> getComponentDescriptorMap(String role) {
        return getComponentObjectDescriptorMap(null, role);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final <T> Map<String, ComponentDescriptor<T>> getComponentDescriptorMap(Class<T> type, String role) {
        return (Map<String, ComponentDescriptor<T>>)(Map)getComponentObjectDescriptorMap(null, role);
    }

    private static String classRole(Class<?> cl) {
        return cl.getName();
    }
}
