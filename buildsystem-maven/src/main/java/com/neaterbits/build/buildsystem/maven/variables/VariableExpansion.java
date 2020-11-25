package com.neaterbits.build.buildsystem.maven.variables;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Function;

import com.neaterbits.build.buildsystem.maven.xml.model.DocumentModel;
import com.neaterbits.util.ArrayUtils;
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

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    
    public static <NODE, ELEMENT extends NODE, DOCUMENT extends NODE>
    String replaceVariable(
            String text,
            MavenBuiltinVariables builtinVariables,
            Map<String, String> properties,
            DocumentModel<NODE, ELEMENT, DOCUMENT> pomModel,
            DOCUMENT document) {
        
        String cur = text;
        
        for (;;) {
            
            final String expanded = expandVariables(cur, var -> {
            
                final String [] parts = StringUtils.split(var, '.');
                final String result;
                
                String replacedFromModel;

                if (parts.length == 2 && parts[0].equals("project") && parts[1].equals("basedir")) {
                    result = builtinVariables.getProjectBaseDir().getAbsolutePath();
                }
                else if (parts.length == 2 && parts[0].equals("project") && parts[1].equals("baseUri")) {
                    result = "file:/" + builtinVariables.getProjectBaseDir().getAbsolutePath();
                }
                else if (    parts.length == 3
                          && parts[0].equals("maven")
                          && parts[1].equals("build")
                          && parts[2].equals("timestamp")) {
                    
                    String formatString;
                    
                    final DateTimeFormatter formatter;

                    if (properties != null) {
                        formatString = properties.get("maven.build.timestamp.format");

                        if (formatString != null && !formatString.isEmpty()) {
                            formatter = DateTimeFormatter.ofPattern(formatString);
                        }
                        else {
                            formatter = FORMATTER;
                        }
                    }
                    else {
                        formatter = FORMATTER;
                    }
                    
                    result = formatter.format(builtinVariables.getBuildStartTime());
                }
                else if (parts.length == 2 && parts[0].equals("env")) {
                    result = System.getenv(parts[1]);
                }
                else if (null != (replacedFromModel = replaceFromModel(parts, pomModel, document))) {
                    result = replacedFromModel;
                }
                else if (properties != null) {
                    result = properties.get(var);
                }
                else {
                    result = null;
                }
    
                return result;
            });

            if (cur.equals(expanded)) {
                break;
            }
            
            cur = expanded;
        }
        
        return cur;
    }

    private static String[][] SINGLE_ELEMENTS = new String [][] {
        
        merge("project", "modelVersion"),

        merge("project", "groupId"),
        merge("project", "artifactId"),
        merge("project", "version"),
        merge("project", "packaging"),

        merge("project", "parent", "groupId"),
        merge("project", "parent", "artifactId"),
        merge("project", "parent", "version"),
        merge("project", "parent", "relativePath"),
        
        merge("project", "build", "directory"),
        merge("project", "build", "outputDirectory"),
        merge("project", "build", "finalName"),
        merge("project", "build", "testOutputDirectory"),
        merge("project", "build", "sourceDirectory"),
        merge("project", "build", "scriptSourceDirectory"),
        merge("project", "build", "testSourceDirectory"),
        
        merge("project", "reporting", "outputDirectory"),

        merge("project", "name"),
        merge("project", "description"),
        merge("project", "url"),
        merge("project", "inceptionYear"),
        
    };
    
    private static String [] merge(String ... tags) {

        return tags;
    }
    
    private static <NODE, ELEMENT extends NODE, DOCUMENT extends NODE>
    String replaceFromModel(
            String [] parts,
            DocumentModel<NODE, ELEMENT, DOCUMENT> pomModel,
            DOCUMENT document) {

        String replaced = null;

        // Search for a matching element
        for (String [] singleElement : SINGLE_ELEMENTS) {
            if (ArrayUtils.startsWith(parts, singleElement)) {

                // Replace under here if can be found in model
                replaced = findElementNode(parts, 0, pomModel, document);
                break;
            }
        }

        return replaced;
    }

    private static <NODE, ELEMENT extends NODE, DOCUMENT extends NODE>
    String findElementNode(
            String [] parts,
            int index,
            DocumentModel<NODE, ELEMENT, DOCUMENT> pomModel,
            NODE node) {
        
        final ELEMENT element = pomModel.getElement(node, parts[index]);
        
        final String result;
        
        if (element != null) {

            if (index == parts.length - 1) {
                result = pomModel.getText(element);
            }
            else {
                result = findElementNode(parts, index + 1, pomModel, element);
            }
        }
        else {
            result = null;
        }
        
        return result;
    }
}
