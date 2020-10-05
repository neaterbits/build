package com.neaterbits.ide.buildsystem.maven.elements;

import java.util.Objects;

import com.neaterbits.ide.buildsystem.maven.MavenModuleId;

abstract class MavenEntity {

	private final MavenModuleId moduleId;
	
	private final String packaging;

	MavenEntity(MavenModuleId moduleId, String packaging) {
		
		Objects.requireNonNull(moduleId);
		
		this.moduleId = moduleId;
		
		this.packaging = packaging;
	}

	public final MavenModuleId getModuleId() {
		return moduleId;
	}

	public String getPackaging() {
		return packaging;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [moduleId=" + moduleId + ", packaging=" + packaging + "]";
	}
}
