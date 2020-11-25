package com.neaterbits.build.buildsystem.maven.project.parse;

import com.neaterbits.build.buildsystem.maven.project.model.MavenBuild;

interface BuildSetter {

    void setBuild(MavenBuild build);
}
