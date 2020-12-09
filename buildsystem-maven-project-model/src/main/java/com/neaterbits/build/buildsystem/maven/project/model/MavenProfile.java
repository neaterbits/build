package com.neaterbits.build.buildsystem.maven.project.model;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public final class MavenProfile {

    private final String id;
    private final MavenActivation activation;
    
    private final MavenCommon common;
    
    private final Map<String, String> properties;
   
    public MavenProfile(
            String id,
            MavenActivation activation,
            MavenCommon common,
            Map<String, String> properties) {

        Objects.requireNonNull(common);
        
        this.id = id;
        this.activation = activation;
        this.common = common;
        this.properties = properties != null
                ? Collections.unmodifiableMap(properties)
                : null;
    }
    
    public MavenProfile(MavenProfile other, MavenCommon common) {
        this(other.id, other.activation, common, other.properties);
    }

    public String getId() {
        return id;
    }

    public MavenActivation getActivation() {
        return activation;
    }

    public MavenCommon getCommon() {
        return common;
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}
