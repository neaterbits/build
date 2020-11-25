package com.neaterbits.build.buildsystem.maven.common.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;

public interface DependenciesSetter {

    void setDependencies(List<MavenDependency> dependencies);
}
