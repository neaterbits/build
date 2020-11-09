package com.neaterbits.build.buildsystem.maven.plugins.descriptor.parse;

import com.neaterbits.build.buildsystem.maven.parse.StackBase;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoRequirement;
import com.neaterbits.util.parse.context.Context;

final class StackRequirement extends StackBase {

    private String role;
    
    private String roleHint;
    
    private String fieldName;
    
    StackRequirement(Context context) {
        super(context);
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setRoleHint(String roleHint) {
        this.roleHint = roleHint;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
    MojoRequirement build() {
        return new MojoRequirement(role, roleHint, fieldName);
    }
}
