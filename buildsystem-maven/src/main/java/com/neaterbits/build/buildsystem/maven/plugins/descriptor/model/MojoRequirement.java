package com.neaterbits.build.buildsystem.maven.plugins.descriptor.model;

import com.neaterbits.build.buildsystem.maven.components.plexus.elements.PlexusRequirement;

public final class MojoRequirement extends PlexusRequirement {

    public MojoRequirement(String role, String roleHint, String fieldName) {
        super(role, roleHint, fieldName, null);
    }
}
