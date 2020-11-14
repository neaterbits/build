package com.neaterbits.build.buildsystem.maven.parse;

import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenDependencyManagement;

interface DependencyManagementSetter {

    void setDependencyManagement(MavenDependencyManagement dependencyManagement);
}
