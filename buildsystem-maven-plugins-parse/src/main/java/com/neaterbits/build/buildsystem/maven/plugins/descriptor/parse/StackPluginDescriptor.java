package com.neaterbits.build.buildsystem.maven.plugins.descriptor.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.common.parse.DependenciesSetter;
import com.neaterbits.build.buildsystem.maven.common.parse.StackEntity;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoDescriptor;
import com.neaterbits.util.parse.context.Context;

final class StackPluginDescriptor
        extends StackEntity
        implements NameSetter, DescriptionSetter, DependenciesSetter, InheritedByDefaultSetter {

    private String name;
    
    private String description;
    
    private String goalPrefix;
    
    private Boolean isolatedRealm;
    
    private Boolean inheritedByDefault;
    
    private final List<MojoDescriptor> mojos;
    
    private List<MavenDependency> dependencies;
    
    StackPluginDescriptor(Context context) {
        super(context);
        
        this.mojos = new ArrayList<>();
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
    
    void setGoalPrefix(String goalPrefix) {
        this.goalPrefix = goalPrefix;
    }
    
    void setIsolatedRealm(boolean isolatedRealm) {
        this.isolatedRealm = isolatedRealm;
    }

    @Override
    public void setInheritedByDefault(Boolean inheritedByDefault) {
        this.inheritedByDefault = inheritedByDefault;
    }

    void addMojo(MojoDescriptor mojo) {
        
        Objects.requireNonNull(mojo);
        
        mojos.add(mojo);
    }

    @Override
    public void setDependencies(List<MavenDependency> dependencies) {
        
        Objects.requireNonNull(dependencies);
        
        this.dependencies = dependencies;
    }

    MavenPluginDescriptor build() {
        return new MavenPluginDescriptor(
                makeModuleId(),
                name, description,
                goalPrefix,
                isolatedRealm, inheritedByDefault,
                mojos,
                dependencies);
    }
}
