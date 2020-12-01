	package com.neaterbits.build.buildsystem.maven;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.project.model.xml.MavenXMLProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.stream.JavaxXMLStreamReaderFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class MavenModulesReaderTest {

	@Test
	public void testModulesReaderReadFromRoot() throws XMLReaderException, IOException {
	    checkModulesReader(new File("../"), new File("../"));
	}

	@Test
	public void testModulesReaderReadFromBuildMain() throws XMLReaderException, IOException {
       checkModulesReader(new File("../build-main"), new File("../"));
	}

    @Test
    public void testModulesReaderReadFromBuildSystemCommon() throws XMLReaderException, IOException {
       checkModulesReader(new File("../buildsystem-common"), new File("../"));
    }
	
	// Should always read all projects
	private void checkModulesReader(File startDirectory, File rootDirectory) throws XMLReaderException, IOException {

	    final List<MavenXMLProject<Void>> mavenProjects = MavenModulesReader.readModules(
                startDirectory,
                new JavaxXMLStreamReaderFactory());

        assertThat(mavenProjects).isNotNull();
        
        assertThat(mavenProjects.isEmpty()).isFalse();
        
        final Set<MavenProject> distinctProjects = mavenProjects.stream()
            .map(MavenXMLProject::getProject)
            .collect(Collectors.toSet());
        
        assertThat(distinctProjects.size()).isEqualTo(mavenProjects.size());

        final Set<MavenModuleId> moduleIds = mavenProjects.stream()
                .map(p -> p.getProject().getModuleId())
                .collect(Collectors.toSet());
        
        assertThat(mavenProjects.size()).isEqualTo(moduleIds.size());
        
        final int numberOfDirectories = rootDirectory.listFiles(file -> new File(file, "pom.xml").exists()).length;
        
        assertThat(moduleIds.size()).isEqualTo(numberOfDirectories + 1); // + 1 for root project
	}

	@Test
	public void testManyModulesReader() throws XMLReaderException, IOException {

		MavenModulesReader.readModules(new File("../"), new JavaxXMLStreamReaderFactory());
	}
}
