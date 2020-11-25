package com.neaterbits.build.buildsystem.maven.plugins.initialize;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.plugins.initialize.MojoFieldUtil.Converter;

class ConvertValueUtil {

    static Object convertValue(
            MojoExecutionContext context,
            String configuredFieldType,
            Class<?> fieldType,
            Object value,
            String fieldName)
                        throws UnknownFieldTypeException, MissingRequiredValueException, ValueFormatException, TypeMismatchException {
        
        if (MojoFieldUtil.isPrimitiveType(fieldType) && value == null) {
            throw new MissingRequiredValueException(context, fieldName);
        }
        
        Object converted;
        
        if (value instanceof String) {
            
            final String string = (String)value;

            try {
                final Converter converter = getConversionFunction(context, fieldType, fieldName);
                
                converted = converter != null
                        ? converter.convert(string)
                        : value;
            }
            catch (NumberFormatException ex) {
                throw new ValueFormatException(context, fieldName);
            }
        }
        else if (value instanceof String []) {

            final String [] strings = (String[])value;

            if (fieldType.equals(String[].class)) {
                converted = value;
            }
            else if (fieldType.isArray()) {

                final Converter converter = getConversionFunction(context, fieldType.getComponentType(), fieldName);
                
                if (converter != null) {
                    
                    converted = Array.newInstance(fieldType.getComponentType(), strings.length);
                    
                    for (int i = 0; i < strings.length; ++ i) {
                        Array.set(converted, i, converter.convert(strings[i]));
                    }
                }
                else {
                    converted = value;
                }
            }
            else if (fieldType.equals(List.class)) {
                converted = Arrays.asList(strings);
            }
            else {
                converted = value;
            }
        }
        else if (value instanceof List<?>) {
            
            final List<?> list = (List<?>)value;
            
            if (fieldType.equals(List.class)) {
                converted = value;
            }
            else if (fieldType.equals(String[].class)) {
                converted = list.toArray(new String[list.size()]);
            }
            else if (fieldType.isArray()) {
                
                if (list.isEmpty()) {
                    converted = Array.newInstance(fieldType.getComponentType(), 0);
                }
                else if (list.get(0) instanceof String) {
                    
                    final Converter converter = getConversionFunction(context, fieldType.getComponentType(), fieldName);
                    
                    if (converter != null) {
                        
                        converted = Array.newInstance(fieldType.getComponentType(), list.size());
                        
                        for (int i = 0; i < list.size(); ++ i) {
                            Array.set(converted, i, converter.convert((String)list.get(i)));
                        }
                    }
                    else {
                        throw new TypeMismatchException(context, configuredFieldType, fieldType.getName(), fieldName);
                    }
                }
                else {
                    throw new TypeMismatchException(context, configuredFieldType, fieldType.getName(), fieldName);
                }
            }
            else {
                converted = value;
            }
        }
        else {
            converted = value;
        }
        
        return converted;
    }

    private static Converter getConversionFunction(MojoExecutionContext context, Class<?> fieldType, String fieldName) {

        final Converter function;
        
        if (fieldType.equals(byte.class) || fieldType.equals(Byte.class)) {
            function = Byte::valueOf;
        }
        else if (fieldType.equals(short.class) || fieldType.equals(Short.class)) {
            function = Short::valueOf;
        }
        else if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
            function = Integer::valueOf;
        }
        else if (fieldType.equals(long.class) || fieldType.equals(Long.class)) {
            function = Long::valueOf;
        }
        else if (fieldType.equals(float.class) || fieldType.equals(Float.class)) {
            function = Float::valueOf;
        }
        else if (fieldType.equals(double.class) || fieldType.equals(Double.class)) {
            function = Double::valueOf;
        }
        else if (fieldType.equals(BigInteger.class)) {
            function = BigInteger::new;
        }
        else if (fieldType.equals(BigDecimal.class)) {
            function = BigDecimal::new;
        }
        else if (fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) {
            function = Boolean::valueOf;
        }
        else if (fieldType.equals(char.class) || fieldType.equals(Character.class)) {
            
            function = string -> {
                if (string.length() != 1) {
                    throw new ValueFormatException(context, fieldName);
                }
    
                return string.charAt(0);
            };
        }
        else {
            function = null;
        }
        
        return function;
    }
}
