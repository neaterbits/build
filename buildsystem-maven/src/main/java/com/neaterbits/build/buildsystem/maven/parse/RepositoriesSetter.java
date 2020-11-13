package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenRepository;

interface RepositoriesSetter {

    void setRepositories(List<MavenRepository> repositories);
}
