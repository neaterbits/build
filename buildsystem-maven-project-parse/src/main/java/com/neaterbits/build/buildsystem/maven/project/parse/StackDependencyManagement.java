package com.neaterbits.build.buildsystem.maven.project.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.common.parse.DependenciesSetter;
import com.neaterbits.util.parse.context.Context;

final class StackDependencyManagement extends StackBase implements DependenciesSetter {

    private List<MavenDependency> dependencies;

    StackDependencyManagement(Context context) {
        super(context);
    }

    List<MavenDependency> getDependencies() {
        return dependencies;
    }

    @Override
    public void setDependencies(List<MavenDependency> dependencies) {
        this.dependencies = dependencies;
    }
}
