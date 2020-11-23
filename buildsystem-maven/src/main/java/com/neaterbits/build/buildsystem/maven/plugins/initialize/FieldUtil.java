package com.neaterbits.build.buildsystem.maven.plugins.initialize;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class FieldUtil {

    private static final Set<String> primitiveTypes;
    
    static {
        final Set<String> types = new HashSet<>();
        
        addType(types, "void");
        addType(types, "byte");
        addType(types, "short");
        addType(types, "int");
        addType(types, "long");
        addType(types, "float");
        addType(types, "double");
        addType(types, "boolean");
        addType(types, "char");
        
        primitiveTypes = Collections.unmodifiableSet(types);
    }
    
    private static void addType(Set<String> set, String type) {
        
        set.add(type);
    }
    
    @FunctionalInterface
    interface GetFieldValue {
        
        Object getFieldValue(Field field) throws MojoInitializeException;
    }
    
    static void setFieldValue(
            MojoExecutionContext context,
            Object object,
            String fieldName,
            String configuredFieldType,
            GetFieldValue getValue) throws MojoInitializeException {
        
        final Field field;
        
        final Class<?> cl = object.getClass();

        try {
            field = cl.getDeclaredField(fieldName);
        } catch (NoSuchFieldException | SecurityException ex) {
            throw new IllegalStateException(ex);
        }
        
        final String actualFieldType = field.getType().getName();
        
        if (configuredFieldType != null && !configuredFieldType.equals(actualFieldType)) {
            throw new TypeMismatchException(context, configuredFieldType, actualFieldType, fieldName);
        }
        
        final Object fieldValue = getValue.getFieldValue(field);
        
        setFieldValue(context, object, field, fieldValue);
    }
    
    static boolean isPrimitiveType(String fieldType) {
        
        return primitiveTypes.contains(fieldType);
    }

    static boolean isPrimitiveType(Class<?> fieldType) {
        
        return primitiveTypes.contains(fieldType.getName());
    }
    
    @FunctionalInterface
    interface Converter {
        
        Object convert(String string) throws ValueFormatException;
    }
    
    private static void setFieldValue(MojoExecutionContext context, Object object, Field field, Object value) {
        
        final boolean fieldAccessible = field.canAccess(object);

        if (!fieldAccessible) {
            field.setAccessible(true);
        }

        try {
            field.set(object, value);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        }
        finally {
            if (!fieldAccessible) {
                field.setAccessible(false);
            }
        }
    }

}
