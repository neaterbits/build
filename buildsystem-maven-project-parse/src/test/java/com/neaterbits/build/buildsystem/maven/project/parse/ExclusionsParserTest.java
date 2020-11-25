package com.neaterbits.build.buildsystem.maven.project.parse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;

public class ExclusionsParserTest extends BasePomParserTest {

    @Test
    public void testExclusions() throws IOException, XMLReaderException {

        final String groupId = "theGroupId";
        final String artifactId = "theArtifactId";
        final String version = "theVersion";

        final String pomString =
                "<project>"

              + "  <groupId>" + groupId + "</groupId>"
              + "  <artifactId>" + artifactId + "</artifactId>"
              + "  <version>" + version + "</version>"
              
              + "  <dependencies>"
              + "    <dependency>"
              + "      <groupId>depGroupId</groupId>"
              + "      <artifactId>depGroupId</artifactId>"
              + "      <version>depVersion</version>"
              
              + "      <exclusions>"
              + "        <exclusion>"
              + "           <groupId>exclusion1GroupId</groupId>"
              + "           <artifactId>exclusion1ArtifactId</artifactId>"
              + "        </exclusion>"
              + "        <exclusion>"
              + "           <groupId>exclusion2GroupId</groupId>"
              + "           <artifactId>exclusion2ArtifactId</artifactId>"
              + "        </exclusion>"
              + "        <exclusion>"
              + "           <groupId>exclusion3GroupId</groupId>"
              + "           <artifactId>exclusion3ArtifactId</artifactId>"
              + "        </exclusion>"
              + "      </exclusions>"
              + "    </dependency>"
              + "  </dependencies>"
              + "</project>";

        final MavenProject project = parse(pomString);

        assertThat(project.getCommon().getDependencies().size()).isEqualTo(1);

        final MavenDependency dependency = project.getCommon().getDependencies().get(0);
        
        assertThat(dependency.getExclusions().size()).isEqualTo(3);

        assertThat(dependency.getExclusions().get(0).getGroupId()).isEqualTo("exclusion1GroupId");
        assertThat(dependency.getExclusions().get(0).getArtifactId()).isEqualTo("exclusion1ArtifactId");

        assertThat(dependency.getExclusions().get(1).getGroupId()).isEqualTo("exclusion2GroupId");
        assertThat(dependency.getExclusions().get(1).getArtifactId()).isEqualTo("exclusion2ArtifactId");

        assertThat(dependency.getExclusions().get(2).getGroupId()).isEqualTo("exclusion3GroupId");
        assertThat(dependency.getExclusions().get(2).getArtifactId()).isEqualTo("exclusion3ArtifactId");
    }
}
