package com.neaterbits.ide.buildsystem.maven.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.ide.buildsystem.maven.elements.MavenDependency;

final class StackDependencies extends StackBase {

	private final List<MavenDependency> dependencies;
	
	StackDependencies(Context context) {
		super(context);
		
		this.dependencies = new ArrayList<>();
	}

	public List<MavenDependency> getDependencies() {
		return dependencies;
	}
	
	public void addDependency(MavenDependency dependency) {
		Objects.requireNonNull(dependency);

		dependencies.add(dependency);
	}
}
