package com.neaterbits.build.buildsystem.maven.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.build.buildsystem.maven.elements.MavenDependency;
import com.neaterbits.util.parse.context.Context;

public final class StackDependencies extends StackBase {

	private final List<MavenDependency> dependencies;

	public StackDependencies(Context context) {
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
