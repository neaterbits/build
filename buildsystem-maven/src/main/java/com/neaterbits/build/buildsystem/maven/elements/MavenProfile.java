package com.neaterbits.build.buildsystem.maven.elements;

import java.util.Objects;

public final class MavenProfile {

    private final String id;
    private final MavenActivation activation;
    
    private final MavenCommon common;
   
    public MavenProfile(
            String id,
            MavenActivation activation,
            MavenCommon common) {

        Objects.requireNonNull(id);
        Objects.requireNonNull(activation);
        Objects.requireNonNull(common);
        
        this.id = id;
        this.activation = activation;
        this.common = common;
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
}
