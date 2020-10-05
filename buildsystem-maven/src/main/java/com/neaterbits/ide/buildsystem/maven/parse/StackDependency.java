package com.neaterbits.ide.buildsystem.maven.parse;

import com.neaterbits.compiler.util.Context;

final class StackDependency extends StackEntity {

	private String scope;
	private String optional;
	
	StackDependency(Context context) {
		super(context);
	}

	String getScope() {
		return scope;
	}

	void setScope(String scope) {
		this.scope = scope;
	}

	String getOptional() {
		return optional;
	}

	void setOptional(String optional) {
		this.optional = optional;
	}
}
