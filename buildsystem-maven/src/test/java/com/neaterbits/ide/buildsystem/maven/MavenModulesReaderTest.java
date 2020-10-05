	package com.neaterbits.ide.buildsystem.maven;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import com.neaterbits.ide.buildsystem.maven.elements.MavenProject;

import static org.assertj.core.api.Assertions.assertThat;

public class MavenModulesReaderTest {

	@Test
	public void testModulesReader() throws XMLStreamException, IOException {
		final List<MavenProject> mavenProjects = MavenModulesReader.readModules(new File("../"));
		
		assertThat(mavenProjects).isNotNull();
		
		assertThat(mavenProjects.isEmpty()).isFalse();
	}

	@Test
	public void testManyModulesReader() throws XMLStreamException, IOException {
		MavenModulesReader.readModules(new File("../../dataview/source"));
	}
}
