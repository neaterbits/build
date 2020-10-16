package com.neaterbits.build.buildsystem.maven;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.parse.PomTreeParser;
import com.neaterbits.build.buildsystem.maven.xml.MavenXMLProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;

public class MavenModulesReader {

	static <DOCUMENT> List<MavenXMLProject<DOCUMENT>> readModules(
								File baseDirectory,
								XMLReaderFactory<DOCUMENT> xmlReaderFactory) throws XMLReaderException, IOException {

		final List<MavenXMLProject<DOCUMENT>> modules = new ArrayList<>();
		
		readModules(baseDirectory, modules, xmlReaderFactory);

		return modules;
	}

	static <DOCUMENT> void readModules(
			File pomDirectory,
			List<MavenXMLProject<DOCUMENT>> modules,
			XMLReaderFactory<DOCUMENT> xmlReaderFactory) throws XMLReaderException, IOException {

		// System.out.println("## read file " + pomDirectory.getPath());
		
		final File pomFile = new File(pomDirectory, "pom.xml");
		
		final MavenXMLProject<DOCUMENT> mavenProject = PomTreeParser.readModule(pomFile, xmlReaderFactory);
		
		if (mavenProject == null) {
			throw new IllegalStateException();
		}
		
		modules.add(mavenProject);
		
		final List<String> subModules = mavenProject.getProject().getSubModules();
		
		if (subModules != null) {
			
			for (String subModule : subModules) {
				readModules(new File(pomDirectory, subModule), modules, xmlReaderFactory);
			}
		}
	}
}
