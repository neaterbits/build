package com.neaterbits.build.buildsystem.maven.common.model;

import java.util.Collections;
import java.util.List;

public final class MavenDependency extends MavenEntity {

    private final String classifier;
    private final String type;
    private final String scope;
	private final String optional;
	
	private final List<MavenExclusion> exclusions;
	
	public MavenDependency(String groupId, String artifactId, String version) {
	    this(new MavenModuleId(groupId, artifactId, version), null, null, null, null, null);
	}

	public MavenDependency(MavenModuleId moduleId, String type, String classifier, String scope, String optional, List<MavenExclusion> exclusions) {
		super(moduleId, null);
	
        this.type = type;
        this.classifier = classifier;
		this.scope = scope;
		this.optional = optional;
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
