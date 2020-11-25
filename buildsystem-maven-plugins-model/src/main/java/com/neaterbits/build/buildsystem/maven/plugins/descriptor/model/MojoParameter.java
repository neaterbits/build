package com.neaterbits.build.buildsystem.maven.plugins.descriptor.model;

public final class MojoParameter {
    
    private final String name;
    
    private final String alias;

    private final String type;

    private final Boolean required;
    
    private final Boolean editable;
        
    private final String implementation;
    
    private final String description;
    
    private final String since;
    
    private final String deprecated;

    public MojoParameter(
            String name,
            String alias,
            String type,
            Boolean required,
            Boolean editable,
            String implementation,
            String description,
            String since,
            String deprecated) {

        this.name = name;
        this.alias = alias;
        this.type = type;
        this.required = required;
        this.editable = editable;
        this.implementation = implementation;
        this.description = description;
        this.since = since;
        this.deprecated = deprecated;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public String getType() {
        return type;
    }

    public Boolean getRequired() {
        return required;
    }

    public Boolean getEditable() {
        return editable;
    }

    public String getImplementation() {
        return implementation;
    }

    public String getDescription() {
        return description;
    }

    public String getSince() {
        return since;
    }

    public String getDeprecated() {
        return deprecated;
    }
}
