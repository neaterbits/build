package com.neaterbits.build.buildsystem.maven.components.plexus.elements;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class PlexusComponent {

    private final String role;
    private final String roleHint;
    private final String implementation;
    
    private final List<PlexusRequirement> requirements;
    
    public PlexusComponent(
            String role,
            String roleHint,
            String implementation,
            List<PlexusRequirement> requirements) {

        Objects.requireNonNull(role);
        Objects.requireNonNull(implementation);
        
        this.role = role;
        this.roleHint = roleHint;
        this.implementation = implementation;
        
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

    public List<PlexusRequirement> getRequirements() {
        return requirements;
    }
}
