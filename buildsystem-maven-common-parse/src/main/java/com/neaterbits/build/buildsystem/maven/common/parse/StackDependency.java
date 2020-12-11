package com.neaterbits.build.buildsystem.maven.common.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.common.model.MavenExclusion;
import com.neaterbits.util.parse.context.Context;

final class StackDependency extends StackEntity implements TypeSetter {

    private String type;
    private String classifier;
    private String scope;
	private String optional;
	
	private List<MavenExclusion> exclusions;

	StackDependency(Context context) {
		super(context);
	}

    String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    String getClassifier() {
        return classifier;
    }

    void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    String getOptional() {
		return optional;
	}

    String getScope() {
        return scope;
    }

    void setScope(String scope) {
        this.scope = scope;
    }

	void setOptional(String optional) {
		this.optional = optional;
	}

    List<MavenExclusion> getExclusions() {
        return exclusions;
    }

    void setExclusions(List<MavenExclusion> exclusions) {
        this.exclusions = exclusions;
    }
}
