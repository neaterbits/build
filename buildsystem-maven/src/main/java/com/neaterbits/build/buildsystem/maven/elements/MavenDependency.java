package com.neaterbits.build.buildsystem.maven.elements;

import java.util.Collections;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.MavenModuleId;

public final class MavenDependency extends MavenEntity {

    private final String classifier;
    private final String type;
    private final String scope;
	private final String optional;
	
	private final List<MavenExclusion> exclusions;
	
	public MavenDependency(MavenModuleId moduleId, String type, String classifier, String scope, String optional, List<MavenExclusion> exclusions) {
		super(moduleId, null);
	
		this.scope = scope;
		this.type = type;
		this.optional = optional;
		this.classifier = classifier;
		this.exclusions = exclusions != null
		        ? Collections.unmodifiableList(exclusions)
                : null;
	}

    public String getType() {
        return type;
    }

	public String getClassifier() {
        return classifier;
    }

    public String getScope() {
		return scope;
	}
	
    public String getOptional() {
		return optional;
	}

	public List<MavenExclusion> getExclusions() {
        return exclusions;
    }

    @Override
	public String toString() {
		return "MavenDependency [scope=" + scope + ", optional=" + optional + ", getModuleId()=" + getModuleId()
				+ ", getPackaging()=" + getPackaging() + "]";
	}
}
