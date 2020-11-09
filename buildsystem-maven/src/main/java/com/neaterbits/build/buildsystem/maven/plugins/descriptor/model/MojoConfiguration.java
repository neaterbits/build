package com.neaterbits.build.buildsystem.maven.plugins.descriptor.model;

import java.util.Objects;

public final class MojoConfiguration {

    private final String paramName;
    
    private final String implementation;
    
    private final String defaultValue;

    public MojoConfiguration(String paramName, String implementation, String defaultValue) {
        
        Objects.requireNonNull(paramName);
        
        this.paramName = paramName;
        this.implementation = implementation;
        this.defaultValue = defaultValue;
    }

    public String getParamName() {
        return paramName;
    }

    public String getImplementation() {
        return implementation;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
