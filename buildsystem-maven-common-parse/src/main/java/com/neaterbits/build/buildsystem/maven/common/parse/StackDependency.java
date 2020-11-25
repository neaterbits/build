package com.neaterbits.build.buildsystem.maven.common.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.common.model.MavenExclusion;
import com.neaterbits.util.parse.context.Context;

final class StackDependency extends StackEntity implements TypeSetter {

	private String scope;
    private String type;
	private String optional;
	
	private List<MavenExclusion> exclusions;

	StackDependency(Context context) {
		super(context);
	}

    String getScope() {
		return scope;
	}

	void setScope(String scope) {
		this.scope = scope;
	}

    String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    String getOptional() {
		return optional;
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
