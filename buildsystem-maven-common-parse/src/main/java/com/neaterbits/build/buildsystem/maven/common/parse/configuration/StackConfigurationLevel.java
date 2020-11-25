package com.neaterbits.build.buildsystem.maven.common.parse.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.build.buildsystem.common.parse.StackText;
import com.neaterbits.build.buildsystem.maven.common.model.configuration.PlexusConfigurationMap;
import com.neaterbits.util.parse.context.Context;

class StackConfigurationLevel extends StackText {

    private final String tagName;

    private Map<String, Object> subObjects;
    
    StackConfigurationLevel(Context context, String tagName) {
        super(context);

        Objects.requireNonNull(tagName);

        this.tagName = tagName;
    }

    final String getTagName() {
        return tagName;
    }
    
    final boolean isSet() {
        return getText() != null || (subObjects != null && !subObjects.isEmpty());
    }
    
    final PlexusConfigurationMap getObject() {
        return new PlexusConfigurationMap(subObjects);
    }
    
    Object get(String key) {
        return subObjects.get(key);
    }
    
    final void add(String key, Object subObject) {
        
        Objects.requireNonNull(key);
        Objects.requireNonNull(subObject);
        
        if (subObjects == null) {

            this.subObjects = new HashMap<>();
            
            subObjects.put(key, subObject);
        }
        else {
            final Object existing = subObjects.get(key);
            
            if (existing == null) {
                subObjects.put(key, subObject);
            }
            else if (existing instanceof List<?>) {

                @SuppressWarnings("unchecked")
                final List<Object> list = (List<Object>)existing;
                
                list.add(subObject);
            }
            else {
                final List<Object> list = new ArrayList<>();
                
                list.add(existing);
                list.add(subObject);
                
                subObjects.put(key, list);
            }
        }
    }
}
