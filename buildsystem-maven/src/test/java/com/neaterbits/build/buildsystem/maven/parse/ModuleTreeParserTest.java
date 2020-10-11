package com.neaterbits.build.buildsystem.maven.parse;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.elements.MavenProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;

import static org.assertj.core.api.Assertions.assertThat;

public class ModuleTreeParserTest {

	@Test
	public void testRead() throws XMLReaderException, IOException {

		final File file = new File("../pom.xml");

		final MavenProject mavenProject = PomTreeParser.readModule(file);

		assertThat(mavenProject).isNotNull();

		assertThat(mavenProject.getModuleId()).isNotNull();
		assertThat(mavenProject.getModuleId().getGroupId()).isNotNull();
		assertThat(mavenProject.getModuleId().getArtifactId()).isNotNull();
		assertThat(mavenProject.getModuleId().getVersion()).isNotNull();
	}
}
