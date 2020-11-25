package com.neaterbits.build.buildsystem.maven.project.parse;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.project.model.xml.MavenXMLProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.stream.JavaxXMLStreamReaderFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class ModuleTreeParserTest {

	@Test
	public void testRead() throws XMLReaderException, IOException {

		final File file = new File("../pom.xml");

		final MavenXMLProject<Void> mavenXMLProject
			= PomTreeParser.readModule(file, new JavaxXMLStreamReaderFactory());
	
		final MavenProject mavenProject = mavenXMLProject.getProject();

		assertThat(mavenProject).isNotNull();

		assertThat(mavenProject.getModuleId()).isNotNull();
		assertThat(mavenProject.getModuleId().getGroupId()).isNotNull();
		assertThat(mavenProject.getModuleId().getArtifactId()).isNotNull();
		assertThat(mavenProject.getModuleId().getVersion()).isNotNull();
	}
}
