package com.neaterbits.build.buildsystem.maven.variables;

import java.util.Map;
import java.util.function.Function;

import com.neaterbits.util.StringUtils;

public class VariableExpansion {

    static String expandVariables(String string, Function<String, String> getValue) {
        
        int index = -1;
        int lastIndex = 0;

        StringBuilder sb = null;

        for (;;) {
            
            final String startPattern = "${";
            
            index = string.indexOf(startPattern, lastIndex);
            
            if (index >= 0 && index < string.length() - 1) {
                
                if (sb == null) {
                    sb = new StringBuilder();
                }

                sb.append(string.substring(lastIndex, index));
                
                final int fromIndex = index + startPattern.length();
                final int endIndex = string.indexOf('}', fromIndex);
                
                if (endIndex > fromIndex) {
                    
                    final String varName = string.substring(fromIndex, endIndex);
                    
                    final String result = getValue.apply(varName);
                    
                    if (result != null) {
                        sb.append(result);
                    }
                    else {
                        sb.append(string.substring(index, endIndex + 1));
                    }
                }
                else {
                    sb.append(string.substring(index, endIndex + 1));
                }
                
                lastIndex = endIndex + 1;
            }
            else {
                if (lastIndex > 0) {
                    sb.append(string.substring(lastIndex));
                }
                break;
            }
        }
        
        return sb != null
                ? sb.toString()
                : string;
    }

    public static String replaceVariable(
            String text,
            Map<String, String> properties) {
        
        return expandVariables(text, var -> {
            
            final String [] parts = StringUtils.split(var, '.');
            final String result;
            
            if (parts.length == 2 && parts[0].equals("env")) {
                result = System.getenv(parts[1]);
            }
            else {
                result = properties.get(var);
            }

            return result;
        });
    }
}
