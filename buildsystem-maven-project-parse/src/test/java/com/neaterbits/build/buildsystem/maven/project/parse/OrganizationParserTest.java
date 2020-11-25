package com.neaterbits.build.buildsystem.maven.project.parse;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.project.model.MavenOrganization;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;

public class OrganizationParserTest extends BasePomParserTest {

    @Test
    public void testOrganization() throws IOException, XMLReaderException {

        final String groupId = "theGroupId";
        final String artifactId = "theArtifactId";
        final String version = "theVersion";

        final String pomString =
                "<project>"

              + "  <groupId>" + groupId + "</groupId>"
              + "  <artifactId>" + artifactId + "</artifactId>"
              + "  <version>" + version + "</version>"

              + "  <organization>"
              + "    <name>TheOrganization</name>"
              + "    <url>https://the.organization.url</url>"
              + "  </organization>"

              + "</project>";

        final MavenProject project = parse(pomString);
        
        final MavenOrganization issueManagement = project.getOrganization();
        
        assertThat(issueManagement.getName()).isEqualTo("TheOrganization");
        assertThat(issueManagement.getUrl()).isEqualTo("https://the.organization.url");
    }    

}
