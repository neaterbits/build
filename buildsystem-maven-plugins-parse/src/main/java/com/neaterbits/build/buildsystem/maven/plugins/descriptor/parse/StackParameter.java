package com.neaterbits.build.buildsystem.maven.plugins.descriptor.parse;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.build.buildsystem.maven.common.parse.TypeSetter;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoParameter;
import com.neaterbits.util.parse.context.Context;

final class StackParameter
    extends StackBase
    implements NameSetter, ImplementationSetter, DescriptionSetter, SinceSetter, DeprecatedSetter, TypeSetter {

    private String name;
    
    private String alias;

    private String type;

    private Boolean required;
    
    private Boolean editable;
        
    private String implementation;
    
    private String description;
    
    private String since;
    
    private String deprecated;
    
    StackParameter(Context context) {
        super(context);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    void setRequired(Boolean required) {
        this.required = required;
    }

    void setEditable(Boolean editable) {
        this.editable = editable;
    }

    @Override
    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setSince(String since) {
        this.since = since;
    }

    @Override
    public void setDeprecated(String deprecated) {
        this.deprecated = deprecated;
    }
    
    MojoParameter build() {

        return new MojoParameter(
                name,
                alias,
                type,
                required,
                editable,
                implementation,
                description,
                since,
                deprecated);
    }
}
