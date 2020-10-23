package com.neaterbits.build.buildsystem.maven.parse;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

final class StackProperties extends StackBase {

    private final Map<String, String> properties;
    
    StackProperties(Context context) {
        super(context);
        
        this.properties = new HashMap<>();
    }
    
    
    void add(String name, String value) {
        
        Objects.requireNonNull(name);
        Objects.requireNonNull(value);
        
        properties.put(name, value);
    }


    Map<String, String> getProperties() {
        return properties;
    }
}
