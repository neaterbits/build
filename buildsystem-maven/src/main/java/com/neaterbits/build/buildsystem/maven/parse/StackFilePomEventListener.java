package com.neaterbits.build.buildsystem.maven.parse;

import java.io.File;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.elements.MavenProject;
import com.neaterbits.util.parse.context.Context;

final class StackFilePomEventListener extends BaseStackPomEventListener {

	private final File rootDirectory;

	private MavenProject mavenProject;

	StackFilePomEventListener(File rootDirectory) {

		Objects.requireNonNull(rootDirectory);

		this.rootDirectory = rootDirectory;
	}

	public MavenProject getMavenProject() {
		return mavenProject;
	}

	@Override
	public void onProjectEnd(Context context) {

		final StackProject project = pop();

		final MavenProject mavenProject = new MavenProject(
				rootDirectory,
				project.makeModuleId(),
				project.getParentModuleId(),
				project.getPackaging(),
				project.getProperties(),
				project.getSubModules(),
				project.getDependencies(),
				project.getBuild(),
				project.getPluginRepositories());

		this.mavenProject = mavenProject;
	}
}
