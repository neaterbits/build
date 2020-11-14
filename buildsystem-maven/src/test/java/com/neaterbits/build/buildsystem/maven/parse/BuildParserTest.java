package com.neaterbits.build.buildsystem.maven.parse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.elements.MavenBuild;
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
              + "    <defaultGoal>install</defaultGoal>"
              + "    <directory>theDirectory</directory>"
              + "    <finalName>theFinalName</finalName>"
              
              + "    <outputDirectory>theOutputDirectory</outputDirectory>"
              + "    <sourceDirectory>theSourceDirectory</sourceDirectory>"
              + "    <scriptSourceDirectory>theScriptSourceDirectory</scriptSourceDirectory>"
              + "    <testSourceDirectory>theTestSourceDirectory</testSourceDirectory>"
              + "  </build>"

              + "</project>";


        final MavenProject project = parse(pomString);

        final MavenBuild build = project.getCommon().getBuild();
        
        assertThat(build.getDefaultGoal()).isEqualTo("install");
        assertThat(build.getDirectory()).isEqualTo("theDirectory");
        assertThat(build.getFinalName()).isEqualTo("theFinalName");
        assertThat(build.getOutputDirectory()).isEqualTo("theOutputDirectory");
        assertThat(build.getSourceDirectory()).isEqualTo("theSourceDirectory");
        assertThat(build.getScriptSourceDirectory()).isEqualTo("theScriptSourceDirectory");
        assertThat(build.getTestSourceDirectory()).isEqualTo("theTestSourceDirectory");
    }
}
