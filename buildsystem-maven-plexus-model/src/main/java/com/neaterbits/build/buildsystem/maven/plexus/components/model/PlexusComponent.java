package com.neaterbits.build.buildsystem.maven.plexus.components.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class PlexusComponent {

    private final String role;
    private final String roleHint;
    private final String implementation;
    private final String instantiationStrategy;
    
    private final List<PlexusRequirement> requirements;
    
    public PlexusComponent(
            String role,
            String roleHint,
            String implementation,
            String instantiationStrategy,
            List<PlexusRequirement> requirements) {

        Objects.requireNonNull(role);
        Objects.requireNonNull(implementation);
        
        this.role = role;
        this.roleHint = roleHint;
        this.implementation = implementation;
        this.instantiationStrategy = instantiationStrategy;
        
        this.requirements = requirements != null
                ? Collections.unmodifiableList(requirements)
                : null;
    }

    public String getRole() {
        return role;
    }

    public String getRoleHint() {
        return roleHint;
    }

    public String getImplementation() {
        return implementation;
    }

    public String getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public List<PlexusRequirement> getRequirements() {
        return requirements;
    }
}
