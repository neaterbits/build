package com.neaterbits.build.buildsystem.maven.plugins.descriptor.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.common.model.MavenEntity;
import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;

public final class MavenPluginDescriptor extends MavenEntity {

    private final String name;
    
    private final String description;
    
    private final String goalPrefix;
    
    private final Boolean isolatedRealm;
    
    private final Boolean inheritedByDefault;
    
    private final List<MojoDescriptor> mojos;
    
    private final List<MavenDependency> dependencies;

    public MavenPluginDescriptor(
            MavenModuleId moduleId,
            String name, String description,
            String goalPrefix,
            Boolean isolatedRealm, Boolean inheritedByDefault,
            List<MojoDescriptor> mojos,
            List<MavenDependency> dependencies) {

        super(moduleId, null);

        this.name = name;
        this.description = description;
        this.goalPrefix = goalPrefix;
        this.isolatedRealm = isolatedRealm;
        this.inheritedByDefault = inheritedByDefault;
        
        this.mojos = Collections.unmodifiableList(
                mojos == null
                    ? Collections.emptyList()
                    : new ArrayList<>(mojos));

        this.dependencies = Collections.unmodifiableList(
                dependencies == null
                    ? Collections.emptyList()
                    : new ArrayList<>(dependencies));
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getGoalPrefix() {
        return goalPrefix;
    }

    public Boolean getIsolatedRealm() {
        return isolatedRealm;
    }

    public Boolean getInheritedByDefault() {
        return inheritedByDefault;
    }

    public List<MojoDescriptor> getMojos() {
        return mojos;
    }

    public List<MavenDependency> getDependencies() {
        return dependencies;
    }
}
