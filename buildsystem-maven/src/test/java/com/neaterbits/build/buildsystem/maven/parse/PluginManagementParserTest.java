package com.neaterbits.build.buildsystem.maven.parse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.elements.MavenBuild;
import com.neaterbits.build.buildsystem.maven.elements.MavenProject;
import com.neaterbits.build.buildsystem.maven.elements.MavenReporting;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;

public class PluginManagementParserTest extends BasePomParserTest {

    @Test
    public void testPluginManagement() throws IOException, XMLReaderException {
    
        final String groupId = "theGroupId";
        final String artifactId = "theArtifactId";
        final String version = "theVersion";
    
        final String pomString =
                "<project>"
    
              + "  <groupId>" + groupId + "</groupId>"
              + "  <artifactId>" + artifactId + "</artifactId>"
              + "  <version>" + version + "</version>"
    
              + "  <build>"
              + "    <pluginManagement>"
              + "      <plugins>"
              + "      </plugins>"
              + "    </pluginManagement>"
              + "  </build>"
    
              + "  <reporting>"
              + "    <pluginManagement>"
              + "      <plugins>"
              + "      </plugins>"
              + "    </pluginManagement>"
              + "  </reporting>"

              + "</project>";
    
        final MavenProject project = parse(pomString);
        
        final MavenBuild build = project.getCommon().getBuild();
        
        assertThat(build.getPluginManagement()).isNotNull();
        assertThat(build.getPluginManagement().getPlugins()).isEmpty();
        
        final MavenReporting reporting = project.getCommon().getReporting();

        assertThat(reporting.getPluginManagement()).isNotNull();
        assertThat(reporting.getPluginManagement().getPlugins()).isEmpty();
    }
}
