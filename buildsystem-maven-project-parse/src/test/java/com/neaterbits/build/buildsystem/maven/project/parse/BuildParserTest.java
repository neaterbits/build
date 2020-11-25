package com.neaterbits.build.buildsystem.maven.project.parse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.project.model.MavenBuild;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
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
              
              + "    <filters>"
              + "      <filter>filter1</filter>"
              + "      <filter>filter2</filter>"
              + "      <filter>filter3</filter>"
              + "    </filters>"
              
              + "    <outputDirectory>theOutputDirectory</outputDirectory>"
              + "    <sourceDirectory>theSourceDirectory</sourceDirectory>"
              + "    <scriptSourceDirectory>theScriptSourceDirectory</scriptSourceDirectory>"
              + "    <testSourceDirectory>theTestSourceDirectory</testSourceDirectory>"
              + "  </build>"

              + "  <dependencyManagement>"
              + "     <dependencies>"
              + "     </dependencies>"
              + "  </dependencyManagement>"

              + "</project>";


        final MavenProject project = parse(pomString);

        final MavenBuild build = project.getCommon().getBuild();
        
        assertThat(build.getDefaultGoal()).isEqualTo("install");
        assertThat(build.getDirectory()).isEqualTo("theDirectory");
        assertThat(build.getFinalName()).isEqualTo("theFinalName");

        assertThat(build.getFilters()).isNotNull();
        assertThat(build.getFilters().size()).isEqualTo(3);
        assertThat(build.getFilters().get(0)).isEqualTo("filter1");
        assertThat(build.getFilters().get(1)).isEqualTo("filter2");
        assertThat(build.getFilters().get(2)).isEqualTo("filter3");

        assertThat(build.getOutputDirectory()).isEqualTo("theOutputDirectory");
        assertThat(build.getSourceDirectory()).isEqualTo("theSourceDirectory");
        assertThat(build.getScriptSourceDirectory()).isEqualTo("theScriptSourceDirectory");
        assertThat(build.getTestSourceDirectory()).isEqualTo("theTestSourceDirectory");
    
        assertThat(project.getCommon().getDependencyManagement().getDependencies()).isEmpty();
    }
}
