package com.neaterbits.build.buildsystem.maven.parse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.elements.MavenProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;

public class BuildParserTest extends BasePomParserTest {

    @Test
    public void testBuild() throws IOException, XMLReaderException {

        final String groupId = "theGroupId";
        final String artifactId = "theArtifactId";
        final String version = "theVersion";

        final String pomString =
                "<project>"

              + "  <groupId>" + groupId + "</groupId>"
              + "  <artifactId>" + artifactId + "</artifactId>"
              + "  <version>" + version + "</version>"

              + "  <build>"
              + "    <directory>theDirectory</directory>"
              + "    <finalName>theFinalName</finalName>"
              + "    <outputDirectory>theOutputDirectory</outputDirectory>"
              + "    <sourceDirectory>theSourceDirectory</sourceDirectory>"
              + "    <scriptSourceDirectory>theScriptSourceDirectory</scriptSourceDirectory>"
              + "    <testSourceDirectory>theTestSourceDirectory</testSourceDirectory>"
              + "  </build>"

              + "</project>";


        final MavenProject project = parse(pomString);

        assertThat(project.getBuild().getDirectory()).isEqualTo("theDirectory");
        assertThat(project.getBuild().getFinalName()).isEqualTo("theFinalName");
        assertThat(project.getBuild().getOutputDirectory()).isEqualTo("theOutputDirectory");
        assertThat(project.getBuild().getSourceDirectory()).isEqualTo("theSourceDirectory");
        assertThat(project.getBuild().getScriptSourceDirectory()).isEqualTo("theScriptSourceDirectory");
        assertThat(project.getBuild().getTestSourceDirectory()).isEqualTo("theTestSourceDirectory");
    }
}
