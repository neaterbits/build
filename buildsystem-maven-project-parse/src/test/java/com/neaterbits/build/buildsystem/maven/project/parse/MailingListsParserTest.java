package com.neaterbits.build.buildsystem.maven.project.parse;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;

public class MailingListsParserTest extends BasePomParserTest {

    @Test
    public void testMailingLists() throws IOException, XMLReaderException {

        final String groupId = "theGroupId";
        final String artifactId = "theArtifactId";
        final String version = "theVersion";

        final String pomString =
                "<project>"

              + "  <groupId>" + groupId + "</groupId>"
              + "  <artifactId>" + artifactId + "</artifactId>"
              + "  <version>" + version + "</version>"

              + "  <mailingLists>"
              + "      <mailingList>"
              + "        <name>TheMailingListName</name>"
              + "        <subscribe>subscribe@mailinglist</subscribe>"
              + "        <unsubscribe>unsubscribe@mailinglist</unsubscribe>"
              + "        <post>user@mailinglist</post>"
              
              + "        <archive>https://archive.mailinglist</archive>"

              + "        <otherArchives>"
              + "          <otherArchive>https://otherarchive.mailinglist</otherArchive>"
              + "        </otherArchives>"
              + "      </mailingList>"
              + "  </mailingLists>"

              + "</project>";

        final MavenProject project = parse(pomString);
        
        assertThat(project.getMailingLists().size()).isEqualTo(1);

        assertThat(project.getMailingLists().get(0).getName()).isEqualTo("TheMailingListName");
        assertThat(project.getMailingLists().get(0).getSubscribe()).isEqualTo("subscribe@mailinglist");
        assertThat(project.getMailingLists().get(0).getUnsubscribe()).isEqualTo("unsubscribe@mailinglist");
        assertThat(project.getMailingLists().get(0).getPost()).isEqualTo("user@mailinglist");
        
        assertThat(project.getMailingLists().get(0).getArchive()).isEqualTo("https://archive.mailinglist");

        assertThat(project.getMailingLists().get(0).getOtherArchives().size()).isEqualTo(1);
        assertThat(project.getMailingLists().get(0).getOtherArchives().get(0)).isEqualTo("https://otherarchive.mailinglist");
    }    
}
