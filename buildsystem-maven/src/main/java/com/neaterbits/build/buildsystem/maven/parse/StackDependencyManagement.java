package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenDependency;
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
