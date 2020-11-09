package com.neaterbits.build.buildsystem.maven.elements;

import com.neaterbits.build.buildsystem.maven.MavenModuleId;

public class MavenDependency extends MavenEntity {

	private final String scope;
    private final String type;
	private final String optional;
	
	public MavenDependency(MavenModuleId moduleId, String packaging, String scope, String type, String optional) {
		super(moduleId, packaging);
	
		this.scope = scope;
		this.type = type;
		this.optional = optional;
	}

	public String getScope() {
		return scope;
	}
	
	public String getType() {
        return type;
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
