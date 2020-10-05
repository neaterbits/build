package com.neaterbits.ide.buildsystem.maven;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import com.neaterbits.ide.buildsystem.maven.elements.MavenProject;
import com.neaterbits.ide.buildsystem.maven.parse.PomTreeParser;

public class MavenModulesReader {

	static List<MavenProject> readModules(File baseDirectory) throws XMLStreamException, IOException {

		final List<MavenProject> modules = new ArrayList<>();
		
		readModules(baseDirectory, modules);

		return modules;
	}

	static void readModules(File pomDirectory, List<MavenProject> modules) throws XMLStreamException, IOException {

		// System.out.println("## read file " + pomDirectory.getPath());
		
		final File pomFile = new File(pomDirectory, "pom.xml");
		
		final MavenProject mavenProject = PomTreeParser.readModule(pomFile);
		
		if (mavenProject == null) {
			throw new IllegalStateException();
		}
		
		modules.add(mavenProject);
		
		if (mavenProject.getSubModules() != null) {
			
			for (String subModule : mavenProject.getSubModules()) {
				readModules(new File(pomDirectory, subModule), modules);
			}
		}
	}
}
