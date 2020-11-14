package com.neaterbits.build.buildsystem.maven.parse;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.elements.MavenIssueManagement;
import com.neaterbits.build.buildsystem.maven.elements.MavenProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;

public class IssueManagementParserTest extends BasePomParserTest {

    @Test
    public void testIssueManagement() throws IOException, XMLReaderException {

        final String groupId = "theGroupId";
        final String artifactId = "theArtifactId";
        final String version = "theVersion";

        final String pomString =
                "<project>"

              + "  <groupId>" + groupId + "</groupId>"
              + "  <artifactId>" + artifactId + "</artifactId>"
              + "  <version>" + version + "</version>"

              + "  <issueManagement>"
              + "    <system>Bugzilla</system>"
              + "    <url>https://issue.management.url</url>"
              + "  </issueManagement>"

              + "</project>";

        final MavenProject project = parse(pomString);
        
        final MavenIssueManagement issueManagement = project.getIssueManagement();
        
        assertThat(issueManagement.getSystem()).isEqualTo("Bugzilla");
        assertThat(issueManagement.getUrl()).isEqualTo("https://issue.management.url");
    }    
}
