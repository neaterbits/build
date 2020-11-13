package com.neaterbits.build.buildsystem.maven.parse;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.elements.MavenProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;

public class TestResourcesParserTest extends BasePomParserTest {

    @Test
    public void testResources() throws IOException, XMLReaderException {

        final String groupId = "theGroupId";
        final String artifactId = "theArtifactId";
        final String version = "theVersion";

        final String pomString =
                "<project>"

              + "  <groupId>" + groupId + "</groupId>"
              + "  <artifactId>" + artifactId + "</artifactId>"
              + "  <version>" + version + "</version>"

              + "  <build>"
              + "    <testResources>"
              + "      <testResource>"
              + "        <targetPath>theTargetPath</targetPath>"
              + "        <filtering>true</filtering>"
              + "        <directory>theDirectory</directory>"
              + "        <includes>"
              + "          <include>include1</include>"
              + "          <include>include2</include>"
              + "          <include>include3</include>"
              + "        </includes>"
              + "        <excludes>"
              + "          <exclude>exclude1</exclude>"
              + "          <exclude>exclude2</exclude>"
              + "          <exclude>exclude3</exclude>"
              + "        </excludes>"
              + "      </testResource>"
              + "    </testResources>"
              + "  </build>"

              + "</project>";

        final MavenProject project = parse(pomString);
        
        assertThat(project.getBuild().getTestResources().size()).isEqualTo(1);
        assertThat(project.getBuild().getTestResources().get(0).getTargetPath()).isEqualTo("theTargetPath");
        assertThat(project.getBuild().getTestResources().get(0).getFiltering()).isEqualTo(Boolean.TRUE);
        assertThat(project.getBuild().getTestResources().get(0).getDirectory()).isEqualTo("theDirectory");
        
        assertThat(project.getBuild().getTestResources().get(0).getIncludes().size()).isEqualTo(3);
        assertThat(project.getBuild().getTestResources().get(0).getIncludes().get(0)).isEqualTo("include1");
        assertThat(project.getBuild().getTestResources().get(0).getIncludes().get(1)).isEqualTo("include2");
        assertThat(project.getBuild().getTestResources().get(0).getIncludes().get(2)).isEqualTo("include3");
        
        assertThat(project.getBuild().getTestResources().get(0).getExcludes().size()).isEqualTo(3);
        assertThat(project.getBuild().getTestResources().get(0).getExcludes().get(0)).isEqualTo("exclude1");
        assertThat(project.getBuild().getTestResources().get(0).getExcludes().get(1)).isEqualTo("exclude2");
        assertThat(project.getBuild().getTestResources().get(0).getExcludes().get(2)).isEqualTo("exclude3");
    }    
}
