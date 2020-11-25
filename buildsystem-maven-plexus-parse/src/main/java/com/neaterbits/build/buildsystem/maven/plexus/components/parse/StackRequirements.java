package com.neaterbits.build.buildsystem.maven.plexus.components.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.build.buildsystem.maven.plexus.components.model.PlexusRequirement;
import com.neaterbits.util.parse.context.Context;

final class StackRequirements extends StackBase {

    private final List<PlexusRequirement> requirements;

    StackRequirements(Context context) {
        super(context);

        this.requirements = new ArrayList<>();
    }

    void add(PlexusRequirement requirement) {
        
        Objects.requireNonNull(requirement);
    
        requirements.add(requirement);
    }

    List<PlexusRequirement> getRequirements() {
        return requirements;
    }
}
