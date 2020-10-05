package com.neaterbits.build.buildsystem.maven.elements;

import com.neaterbits.build.buildsystem.maven.MavenModuleId;

public class MavenPlugin extends MavenEntity {

	public MavenPlugin(MavenModuleId moduleId, String packaging) {
		super(moduleId, null);
	}
}
