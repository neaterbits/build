package com.neaterbits.ide.buildsystem.maven.elements;

import com.neaterbits.ide.buildsystem.maven.MavenModuleId;

public class MavenDependency extends MavenEntity {

	private final String scope;
	private final String optional;
	
	public MavenDependency(MavenModuleId moduleId, String packaging, String scope, String optional) {
		super(moduleId, packaging);
	
		this.scope = scope;
		this.optional = optional;
	}

	public String getScope() {
		return scope;
	}
	
	public String getOptional() {
		return optional;
	}

	@Override
	public String toString() {
		return "MavenDependency [scope=" + scope + ", optional=" + optional + ", getModuleId()=" + getModuleId()
				+ ", getPackaging()=" + getPackaging() + "]";
	}
}
