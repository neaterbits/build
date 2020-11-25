package com.neaterbits.build.buildsystem.maven.plexus.components.parse.common;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.util.parse.context.Context;

public final class StackRequirement
        extends StackBase
        implements RoleSetter, RoleHintSetter {

    private String role;
    
    private String roleHint;
    
    private String fieldName;
    
    private String field;
    
    public StackRequirement(Context context) {
        super(context);
    }

    public String getRole() {
        return role;
    }

    @Override
    public void setRole(String role) {
        this.role = role;
    }

    public String getRoleHint() {
        return roleHint;
    }

    @Override
    public void setRoleHint(String roleHint) {
        this.roleHint = roleHint;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
