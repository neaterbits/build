	package com.neaterbits.build.buildsystem.maven;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.elements.MavenProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;

import static org.assertj.core.api.Assertions.assertThat;

public class MavenModulesReaderTest {

	@Test
	public void testModulesReader() throws XMLReaderException, IOException {
		final List<MavenProject> mavenProjects = MavenModulesReader.readModules(new File("../"));
		
		assertThat(mavenProjects).isNotNull();
		
		assertThat(mavenProjects.isEmpty()).isFalse();
	}

	@Test
	public void testManyModulesReader() throws XMLReaderException, IOException {
		MavenModulesReader.readModules(new File("../"));
	}
}
