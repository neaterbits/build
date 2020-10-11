package com.neaterbits.build.buildsystem.maven;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.neaterbits.build.buildsystem.common.BuildSystem;
import com.neaterbits.build.buildsystem.common.BuildSystemRoot;
import com.neaterbits.build.buildsystem.common.ScanException;
import com.neaterbits.build.buildsystem.maven.elements.MavenProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.types.ModuleId;

public final class MavenBuildSystem implements BuildSystem {

	@Override
	public boolean isBuildSystemFor(File rootDirectory) {

		final File pomFile = new File(rootDirectory, "pom.xml");

		return pomFile.exists() && pomFile.isFile() && pomFile.canRead();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY>
	BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY> scan(File rootDirectory) throws ScanException {

		final List<MavenProject> mavenProjects;

		try {
			mavenProjects = MavenModulesReader.readModules(rootDirectory);
		} catch (XMLReaderException | IOException ex) {
			throw new ScanException("Failed to scan project", ex);
		}

		return (BuildSystemRoot)new MavenBuildRoot(mavenProjects);
	}
}
