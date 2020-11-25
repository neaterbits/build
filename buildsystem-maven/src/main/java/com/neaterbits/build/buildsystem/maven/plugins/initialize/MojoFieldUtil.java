package com.neaterbits.build.buildsystem.maven.plugins.initialize;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.neaterbits.util.reflection.FieldUtil;

class MojoFieldUtil {

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
        
        final Class<?> cl = object.getClass();

        final Field field = FieldUtil.getFieldInClassOrBaseClass(cl, fieldName);
        
        final String actualFieldType = field.getType().getName();
        
        if (configuredFieldType != null && !configuredFieldType.equals(actualFieldType)) {
            throw new TypeMismatchException(context, configuredFieldType, actualFieldType, fieldName);
        }
        
        final Object fieldValue = getValue.getFieldValue(field);
        
        FieldUtil.setFieldValue(object, field, fieldValue);
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
}
