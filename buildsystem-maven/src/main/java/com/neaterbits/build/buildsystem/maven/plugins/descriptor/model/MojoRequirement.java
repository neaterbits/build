package com.neaterbits.build.buildsystem.maven.plugins.descriptor.model;

public final class MojoRequirement {

    private final String role;
    
    private final String roleHint;
    
    private final String fieldName;

    public MojoRequirement(String role, String roleHint, String fieldName) {
        this.role = role;
        this.roleHint = roleHint;
        this.fieldName = fieldName;
    }

    public String getRole() {
        return role;
    }

    public String getRoleHint() {
        return roleHint;
    }

    public String getFieldName() {
        return fieldName;
    }
}
