package com.neaterbits.build.buildsystem.maven.elements;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class MavenPluginConfigurationMap {

    private final Map<String, Object> values;

    public MavenPluginConfigurationMap(Map<String, Object> values) {
        
        Objects.requireNonNull(values);
        
        this.values = Collections.unmodifiableMap(values);
    }

    public Object getValue(String key) {
        
        Objects.requireNonNull(key);

        return values.get(key);
    }
    
    public String getString(String key) {
        return (String)getValue(key);
    }

    @SuppressWarnings("unchecked")
    public List<String> getStringList(String key) {
        return (List<String>)getValue(key);
    }
    
    public MavenPluginConfigurationMap getSubObject(String key) {
        return (MavenPluginConfigurationMap)getValue(key);
    }
    
    @SuppressWarnings("unchecked")
    public List<MavenPluginConfigurationMap> getSubObjectList(String key) {
        return (List<MavenPluginConfigurationMap>)getValue(key);
    }
    
    public Set<String> getKeys() {
        return values.keySet();
    }

    @Override
    public String toString() {
        return "MavenPluginConfiguration [values=" + values + "]";
    }
}
