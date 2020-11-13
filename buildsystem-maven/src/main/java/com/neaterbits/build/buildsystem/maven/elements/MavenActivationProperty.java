package com.neaterbits.build.buildsystem.maven.elements;

public final class MavenActivationProperty {

    private final String name;
    private final String value;
    
    public MavenActivationProperty(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
