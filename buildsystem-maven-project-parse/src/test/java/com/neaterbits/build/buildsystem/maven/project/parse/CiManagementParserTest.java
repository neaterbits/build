package com.neaterbits.build.buildsystem.maven.project.parse;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.project.model.MavenCiManagement;
import com.neaterbits.build.buildsystem.maven.project.model.MavenNotifier;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;

public class CiManagementParserTest extends BasePomParserTest {

    @Test
    public void testCiManagement() throws IOException, XMLReaderException {

        final String groupId = "theGroupId";
        final String artifactId = "theArtifactId";
        final String version = "theVersion";

        final String pomString =
                "<project>"

              + "  <groupId>" + groupId + "</groupId>"
              + "  <artifactId>" + artifactId + "</artifactId>"
              + "  <version>" + version + "</version>"

              + "  <ciManagement>"
              + "    <system>TheCiManagementSystem</system>"
              + "    <url>https://ci.management.url</url>"

              + "    <notifiers>"
              + "      <notifier>"
              + "         <type>mail</type>"
              + "         <sendOnError>true</sendOnError>"
              + "         <sendOnFailure>true</sendOnFailure>"
              + "         <sendOnSuccess>false</sendOnSuccess>"
              + "         <sendOnWarning>false</sendOnWarning>"
              
              + "         <configuration>"
              + "           <address>email@ci.management.url</address>"
              + "         </configuration>"

              + "      </notifier>"
              + "    </notifiers>"
              
              + "  </ciManagement>"

              + "</project>";

        final MavenProject project = parse(pomString);
        
        final MavenCiManagement ciManagement = project.getCiManagement();
        
        assertThat(ciManagement.getSystem()).isEqualTo("TheCiManagementSystem");
        assertThat(ciManagement.getUrl()).isEqualTo("https://ci.management.url");
        
        assertThat(ciManagement.getNotifiers().size()).isEqualTo(1);
        
        final MavenNotifier notifier = ciManagement.getNotifiers().get(0);
    
        assertThat(notifier.getType()).isEqualTo("mail");
        assertThat(notifier.getSendOnError()).isTrue();
        assertThat(notifier.getSendOnFailure()).isTrue();
        assertThat(notifier.getSendOnSuccess()).isFalse();
        assertThat(notifier.getSendOnWarning()).isFalse();
        
        assertThat(notifier.getConfiguration().getProperty("address"))
                    .isEqualTo("email@ci.management.url");
    }
}
