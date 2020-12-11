package com.neaterbits.build.buildsystem.maven.project.parse;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;

public class ClassifierParserTest extends BasePomParserTest {

    @Test
    public void testClassifier() throws IOException, XMLReaderException {

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

              + "      <classifier>depClassifier</classifier>"
              
              + "    </dependency>"
              + "  </dependencies>"
              + "</project>";

        final MavenProject project = parse(pomString);

        assertThat(project.getCommon().getDependencies().size()).isEqualTo(1);

        final MavenDependency dependency = project.getCommon().getDependencies().get(0);

        assertThat(dependency.getClassifier()).isEqualTo("depClassifier");
    }
}
