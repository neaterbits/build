package com.neaterbits.build.buildsystem.maven.project.model;

import com.neaterbits.build.buildsystem.maven.common.model.MavenEntity;
import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;

public class MavenPlugin extends MavenEntity {

    public MavenPlugin(String groupId, String artifactId, String version) {
        
        this(new MavenModuleId(groupId, artifactId, version));
    }

    public MavenPlugin(MavenModuleId moduleId) {
        super(moduleId, null);
    }
}
