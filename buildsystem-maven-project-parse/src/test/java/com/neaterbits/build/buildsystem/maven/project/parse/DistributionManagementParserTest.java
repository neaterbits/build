package com.neaterbits.build.buildsystem.maven.project.parse;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.project.model.MavenDistributionManagement;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;

public class DistributionManagementParserTest extends BasePomParserTest {

    @Test
    public void testDistributionManagement() throws IOException, XMLReaderException {

        final String groupId = "theGroupId";
        final String artifactId = "theArtifactId";
        final String version = "theVersion";

        final String pomString =
                "<project>"

              + "  <groupId>" + groupId + "</groupId>"
              + "  <artifactId>" + artifactId + "</artifactId>"
              + "  <version>" + version + "</version>"

              + "  <distributionManagement>"
              + "    <downloadUrl>https://download.url</downloadUrl>"
              + "    <status>theStatus</status>"
              
              + "    <repository>"
              + "      <uniqueVersion>true</uniqueVersion>"
              + "      <id>theRepository</id>"
              + "      <name>The repository name</name>"
              + "      <url>https://repository.url</url>"
              + "      <layout>theRepositoryLayout</layout>"
              + "    </repository>"
              
              + "    <snapshotRepository>"
              + "      <uniqueVersion>true</uniqueVersion>"
              + "      <id>theSnapshotRepository</id>"
              + "      <name>The snapshot repository name</name>"
              + "      <url>https://snapshotrepository.url</url>"
              + "      <layout>theSnapshotRepositoryLayout</layout>"
              + "    </snapshotRepository>"

              + "    <site>"
              + "      <id>theSiteId</id>"
              + "      <name>The site name</name>"
              + "      <url>https://site.url</url>"
              + "    </site>"
              
              + "    <relocation>"
              + "      <groupId>relocationGroupId</groupId>"
              + "      <artifactId>relocationArtifactId</artifactId>"
              + "      <version>relocationVersion</version>"
              + "      <message>Relocation message</message>"
              + "    </relocation>"

              + "  </distributionManagement>"

              + "</project>";

        final MavenProject project = parse(pomString);
        
        final MavenDistributionManagement distributionManagement = project.getCommon().getDistributionManagement();
        assertThat(distributionManagement).isNotNull();

        assertThat(distributionManagement.getDownloadUrl()).isEqualTo("https://download.url");
        assertThat(distributionManagement.getStatus()).isEqualTo("theStatus");
        
        assertThat(distributionManagement.getRepository().getUniqueVersion()).isEqualTo(true);
        assertThat(distributionManagement.getRepository().getId()).isEqualTo("theRepository");
        assertThat(distributionManagement.getRepository().getName()).isEqualTo("The repository name");
        assertThat(distributionManagement.getRepository().getUrl()).isEqualTo("https://repository.url");
        assertThat(distributionManagement.getRepository().getLayout()).isEqualTo("theRepositoryLayout");

        assertThat(distributionManagement.getSnapshotRepository().getUniqueVersion()).isEqualTo(true);
        assertThat(distributionManagement.getSnapshotRepository().getId()).isEqualTo("theSnapshotRepository");
        assertThat(distributionManagement.getSnapshotRepository().getName()).isEqualTo("The snapshot repository name");
        assertThat(distributionManagement.getSnapshotRepository().getUrl()).isEqualTo("https://snapshotrepository.url");
        assertThat(distributionManagement.getSnapshotRepository().getLayout()).isEqualTo("theSnapshotRepositoryLayout");

        assertThat(distributionManagement.getSite().getId()).isEqualTo("theSiteId");
        assertThat(distributionManagement.getSite().getName()).isEqualTo("The site name");
        assertThat(distributionManagement.getSite().getUrl()).isEqualTo("https://site.url");

        assertThat(distributionManagement.getRelocation().getModuleId().getGroupId()).isEqualTo("relocationGroupId");
        assertThat(distributionManagement.getRelocation().getModuleId().getArtifactId()).isEqualTo("relocationArtifactId");
        assertThat(distributionManagement.getRelocation().getModuleId().getVersion()).isEqualTo("relocationVersion");
        assertThat(distributionManagement.getRelocation().getMessage()).isEqualTo("Relocation message");
    }    
}
