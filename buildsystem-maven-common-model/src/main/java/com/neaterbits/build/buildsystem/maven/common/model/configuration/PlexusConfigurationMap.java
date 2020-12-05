package com.neaterbits.build.buildsystem.maven.common.model.configuration;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class PlexusConfigurationMap {

    private final Map<String, Object> values;
    private final String implementation;

    public PlexusConfigurationMap(Map<String, Object> values) {
        this(values, null);
    }
    
    public PlexusConfigurationMap(Map<String, Object> values, String implementation) {
        
        this.values = values != null
                    ? Collections.unmodifiableMap(values)
                    : Collections.emptyMap();

        this.implementation = implementation;
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
    
    public PlexusConfigurationMap getSubObject(String key) {
        return (PlexusConfigurationMap)getValue(key);
    }
    
    @SuppressWarnings("unchecked")
    public List<PlexusConfigurationMap> getSubObjectList(String key) {
        return (List<PlexusConfigurationMap>)getValue(key);
    }
    
    public Set<String> getKeys() {
        return values.keySet();
    }

    public String getImplementation() {
        return implementation;
    }

    @Override
    public String toString() {
        return "MavenPluginConfiguration [values=" + values + "]";
    }
}
