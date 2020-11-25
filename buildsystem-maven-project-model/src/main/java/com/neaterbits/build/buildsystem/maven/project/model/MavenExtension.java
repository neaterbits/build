package com.neaterbits.build.buildsystem.maven.project.model;

import com.neaterbits.build.buildsystem.maven.common.model.MavenEntity;
import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;

public final class MavenExtension extends MavenEntity {

	public MavenExtension(MavenModuleId moduleId) {
		super(moduleId, null);
	}
}
