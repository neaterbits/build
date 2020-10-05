package com.neaterbits.ide.buildsystem.maven.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.Context;

final class StackModules extends StackBase {
	
	private final List<String> modules;

	StackModules(Context context) {
		super(context);

		this.modules = new ArrayList<>();
	}

	List<String> getModules() {
		return modules;
	}
	
	void addModule(String module) {
		Objects.requireNonNull(module);

		modules.add(module);
	}
}
