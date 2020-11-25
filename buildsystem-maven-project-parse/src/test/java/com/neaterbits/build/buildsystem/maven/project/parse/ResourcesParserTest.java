package com.neaterbits.build.buildsystem.maven.project.parse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.project.model.MavenBuild;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;

public class ResourcesParserTest extends BasePomParserTest {

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
              + "    <resources>"
              + "      <resource>"
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
              + "      </resource>"
              + "    </resources>"
              + "  </build>"

              + "</project>";

        final MavenProject project = parse(pomString);
        
        final MavenBuild build = project.getCommon().getBuild();
        
        assertThat(build.getResources().size()).isEqualTo(1);
        assertThat(build.getResources().get(0).getTargetPath()).isEqualTo("theTargetPath");
        assertThat(build.getResources().get(0).getFiltering()).isEqualTo(Boolean.TRUE);
        assertThat(build.getResources().get(0).getDirectory()).isEqualTo("theDirectory");
        
        assertThat(build.getResources().get(0).getIncludes().size()).isEqualTo(3);
        assertThat(build.getResources().get(0).getIncludes().get(0)).isEqualTo("include1");
        assertThat(build.getResources().get(0).getIncludes().get(1)).isEqualTo("include2");
        assertThat(build.getResources().get(0).getIncludes().get(2)).isEqualTo("include3");
        
        assertThat(build.getResources().get(0).getExcludes().size()).isEqualTo(3);
        assertThat(build.getResources().get(0).getExcludes().get(0)).isEqualTo("exclude1");
        assertThat(build.getResources().get(0).getExcludes().get(1)).isEqualTo("exclude2");
        assertThat(build.getResources().get(0).getExcludes().get(2)).isEqualTo("exclude3");
    }    
}