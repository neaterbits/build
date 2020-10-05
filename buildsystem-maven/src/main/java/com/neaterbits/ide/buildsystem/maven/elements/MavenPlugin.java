package com.neaterbits.ide.buildsystem.maven.elements;

import com.neaterbits.ide.buildsystem.maven.MavenModuleId;

public class MavenPlugin extends MavenEntity {

	public MavenPlugin(MavenModuleId moduleId, String packaging) {
		super(moduleId, null);
	}
}
