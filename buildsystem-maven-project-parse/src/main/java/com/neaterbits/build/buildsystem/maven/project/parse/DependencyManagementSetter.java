package com.neaterbits.build.buildsystem.maven.project.parse;

import com.neaterbits.build.buildsystem.maven.project.model.MavenDependencyManagement;

interface DependencyManagementSetter {

    void setDependencyManagement(MavenDependencyManagement dependencyManagement);
}
