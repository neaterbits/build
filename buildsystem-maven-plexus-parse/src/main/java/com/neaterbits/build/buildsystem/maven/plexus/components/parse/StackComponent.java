package com.neaterbits.build.buildsystem.maven.plexus.components.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.build.buildsystem.maven.plexus.components.model.PlexusRequirement;
import com.neaterbits.build.buildsystem.maven.plexus.components.parse.common.RoleHintSetter;
import com.neaterbits.build.buildsystem.maven.plexus.components.parse.common.RoleSetter;
import com.neaterbits.util.parse.context.Context;

final class StackComponent
        extends StackBase
        implements RoleSetter, RoleHintSetter {

    private String role;
    
    private String roleHint;
    
    private String implementation;

    private String instantiationStrategy;

    private List<PlexusRequirement> requirements;

    StackComponent(Context context) {
        super(context);
    }

    String getRole() {
        return role;
    }

    @Override
    public void setRole(String role) {
        this.role = role;
    }

    String getRoleHint() {
        return roleHint;
    }

    @Override
    public void setRoleHint(String roleHint) {
        this.roleHint = roleHint;
    }

    String getImplementation() {
        return implementation;
    }

    void setImplementation(String implementation) {
        this.implementation = implementation;
    }

    String getInstantiationStrategy() {
        return instantiationStrategy;
    }

    void setInstantiationStrategy(String instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

    List<PlexusRequirement> getRequirements() {
        return requirements;
    }

    void setRequirements(List<PlexusRequirement> requirements) {
        this.requirements = requirements;
    }
}
