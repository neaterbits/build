package com.neaterbits.build.buildsystem.maven.elements;

import com.neaterbits.build.buildsystem.maven.MavenModuleId;

public final class MavenPlugin extends MavenEntity {

    public MavenPlugin(String groupId, String artifactId, String version) {
        this(new MavenModuleId(groupId, artifactId, version));
    }

	public MavenPlugin(MavenModuleId moduleId) {
		super(moduleId, null);
	}
}
