package com.neaterbits.ide.buildsystem.maven;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import com.neaterbits.compiler.util.modules.ModuleId;
import com.neaterbits.ide.buildsystem.maven.elements.MavenProject;
import com.neaterbits.ide.common.buildsystem.BuildSystem;
import com.neaterbits.ide.common.buildsystem.BuildSystemRoot;
import com.neaterbits.ide.common.buildsystem.ScanException;

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
		} catch (XMLStreamException | IOException ex) {
			throw new ScanException("Failed to scan project", ex);
		}
		
		return (BuildSystemRoot)new MavenBuildRoot(mavenProjects);
	}
}
