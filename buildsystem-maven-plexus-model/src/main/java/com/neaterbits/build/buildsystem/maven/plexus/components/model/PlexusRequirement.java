package com.neaterbits.build.buildsystem.maven.plexus.components.model;

public class PlexusRequirement {

    private final String role;
    
    private final String roleHint;
    
    private final String fieldName;
    
    private final String field;

    public PlexusRequirement(String role, String roleHint, String fieldName, String field) {
        this.role = role;
        this.roleHint = roleHint;
        this.fieldName = fieldName;
        this.field = field;
    }

    public final String getRole() {
        return role;
    }

    public final String getRoleHint() {
        return roleHint;
    }

    public final String getFieldName() {
        return fieldName;
    }

    public final String getField() {
        return field;
    }
}
