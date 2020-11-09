package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenDependency;

public interface DependenciesSetter {

    void setDependencies(List<MavenDependency> dependencies);
}
