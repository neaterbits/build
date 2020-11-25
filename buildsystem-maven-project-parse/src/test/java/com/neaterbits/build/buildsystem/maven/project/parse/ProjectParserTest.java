package com.neaterbits.build.buildsystem.maven.project.parse;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;

public class ProjectParserTest extends BasePomParserTest {

    @Test
    public void testProject() throws IOException, XMLReaderException {

        final String groupId = "theGroupId";
        final String artifactId = "theArtifactId";
        final String version = "theVersion";

        final String pomString =
                "<project>"

              + "  <groupId>" + groupId + "</groupId>"
              + "  <artifactId>" + artifactId + "</artifactId>"
              + "  <version>" + version + "</version>"

              + "  <parent>"
              + "    <groupId>parentGroupId</groupId>"
              + "    <artifactId>parentArtifactId</artifactId>"
              + "    <version>parentVersion</version>"
              + "    <relativePath>./the/relative/path</relativePath>"
              + "  </parent>"

              + "  <name>The project name</name>"
              + "  <description>The project description</description>"

              + "</project>";


        final MavenProject project = parse(pomString);

        assertThat(project.getParent().getModuleId().getGroupId()).isEqualTo("parentGroupId");
        assertThat(project.getParent().getModuleId().getArtifactId()).isEqualTo("parentArtifactId");
        assertThat(project.getParent().getModuleId().getVersion()).isEqualTo("parentVersion");

        assertThat(project.getParent().getRelativePath()).isEqualTo("./the/relative/path");
        
        assertThat(project.getName()).isEqualTo("The project name");
        assertThat(project.getDescription()).isEqualTo("The project description");
    }
}
