package com.neaterbits.build.buildsystem.maven.project.model;

public final class MavenActivationOS {

    private final String name;
    private final String family;
    private final String arch;
    private final String version;
    
    public MavenActivationOS(String name, String family, String arch, String version) {
        this.name = name;
        this.family = family;
        this.arch = arch;
        this.version = version;
    }
    public String getName() {
        return name;
    }
    public String getFamily() {
        return family;
    }
    public String getArch() {
        return arch;
    }
    public String getVersion() {
        return version;
    }
    
}
