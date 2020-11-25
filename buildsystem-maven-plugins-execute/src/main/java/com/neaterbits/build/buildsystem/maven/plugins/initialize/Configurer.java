package com.neaterbits.build.buildsystem.maven.plugins.initialize;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import com.neaterbits.build.buildsystem.maven.common.model.configuration.MavenConfiguration;
import com.neaterbits.build.buildsystem.maven.common.model.configuration.PlexusConfigurationMap;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoParameter;
import com.neaterbits.util.StringUtils;

class Configurer {
    
    static boolean applyConfiguration(
            MojoExecutionContext context,
            MojoParameter parameter,
            MavenConfiguration configuration) throws MojoInitializeException {

        Objects.requireNonNull(context);
        Objects.requireNonNull(parameter);
        
        boolean configurationApplied;
        
        if (configuration == null) {
            configurationApplied = false;
        }
        else {
            final String fieldName = parameter.getName();
            final String fieldType = parameter.getType();
            
            apply(
                    context,
                    context.getMojo(),
                    configuration.getMap(),
                    fieldName,
                    parameter.getAlias(),
                    fieldType,
                    parameter.getImplementation(),
                    parameter.getRequired() != null && parameter.getRequired());
            
            
            configurationApplied = true;
        }
        
        return configurationApplied;
    }

    private static void apply(
            MojoExecutionContext context,
            Object toApplyTo,
            PlexusConfigurationMap map,
            String fieldName,
            String alias,
            String configuredFieldType,
            String parameterImplementationName,
            boolean required) throws MojoInitializeException {
        
        Object obj = map.getValue(fieldName);
        
        if (obj == null && alias != null) {
            obj = map.getValue(alias);
        }
      
        final Object object;
        
        if (obj == null) {
            
            if (required || MojoFieldUtil.isPrimitiveType(configuredFieldType)) {
                throw new MissingRequiredValueException(context, fieldName);
            }
            
            object = null;
        }
        else if (obj instanceof PlexusConfigurationMap) {
            
            final PlexusConfigurationMap subMap = (PlexusConfigurationMap)obj;
            
            if (configuredFieldType != null && configuredFieldType.equals("java.util.Map")) {
                object = applyMap(context, subMap);
            }
            else if (configuredFieldType != null && configuredFieldType.equals("java.util.Properties")) {
                object = applyProperties(subMap);
            }
            else {
                object = makeComplexObject(
                        context,
                        subMap,
                        fieldName,
                        parameterImplementationName);
            }
        }
        else {
            object = obj;
        }
        
        MojoFieldUtil.setFieldValue(
                context,
                toApplyTo,
                fieldName,
                configuredFieldType,
                field -> object == null
                        ? null
                        : ConvertValueUtil.convertValue(context, configuredFieldType, field.getType(), object, fieldName));
    }
    
    private static Map<String, Object> applyMap(MojoExecutionContext context, PlexusConfigurationMap subMap) throws MojoInitializeException {
        
        final Map<String, Object> hashMap = new HashMap<>();
        
        for (String key : subMap.getKeys()) {
            
            final Object subValue = subMap.getValue(key);
            
            final Object toAdd;
            
            if (subValue instanceof PlexusConfigurationMap) {
                
                final PlexusConfigurationMap map = (PlexusConfigurationMap)subValue;
                
                if (map.getImplementation() != null && !map.getImplementation().isEmpty()) {
                    toAdd = makeComplexObject(context, map, null, map.getImplementation());
                }
                else {
                    toAdd = applyMap(context, map);
                }
            }
            else {
                toAdd = subValue;
            }
            
            hashMap.put(key, toAdd);
        }

        return hashMap;
    }
    
    private static Properties applyProperties(PlexusConfigurationMap subMap) {
        
        final Properties properties = new Properties();
        
        final List<?> list = subMap.getSubObjectList("property");
        
        if (list != null) {

            for (Object o : list) {
            
                if (o instanceof PlexusConfigurationMap) {
                    
                    final PlexusConfigurationMap propertyMap = (PlexusConfigurationMap)o;
                    
                    final String name = propertyMap.getString("name");
                    final String value = propertyMap.getString("value");
                    
                    if (name != null) {
                        properties.put(name, value);
                    }
                }
            }
        }

        return properties;
    }
    
    private static Object makeComplexObject(
            MojoExecutionContext context,
            PlexusConfigurationMap map,
            String fieldName,
            String parameterImplementationName) throws MojoInitializeException {

        // Complex object
        
        // Guess class name from mapping
        final String className;
        
        if (parameterImplementationName != null && !parameterImplementationName.isEmpty()) {
            className = parameterImplementationName;
        }
        else if (map.getImplementation() != null && !map.getImplementation().isEmpty()) {
            className = map.getImplementation();
        }
        else {
            Objects.requireNonNull(fieldName);
            
            className = classNameInSameScopeAs(context.getMojo().getClass(), StringUtils.capitalize(fieldName));
        }

        final Class<?> cl;
        try {
            cl = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw new UnknownFieldTypeException(context, className, fieldName);
        }
        
        final Object obj;

        try {
            obj = cl.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            throw new IllegalStateException(ex);
        }

        for (String subFieldName : map.getKeys()) {
            apply(context, obj, map, subFieldName, null, null, null, false);
        }

        return obj;
    }
    
    
    private static String classNameInSameScopeAs(Class<?> cl, String className) {

        final String name;
        
        if (cl.getEnclosingMethod() != null) {
            throw new IllegalArgumentException();
        }
        
        if (cl.getEnclosingClass() != null) {
            throw new IllegalArgumentException();
        }
            
        name = cl.getPackageName() + '.' + className;

        return name;
    }
}
