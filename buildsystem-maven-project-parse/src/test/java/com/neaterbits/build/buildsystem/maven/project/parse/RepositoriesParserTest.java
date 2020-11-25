package com.neaterbits.build.buildsystem.maven.project.parse;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.project.model.MavenRepository;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;

public class RepositoriesParserTest extends BasePomParserTest {

    @Test
    public void testParseRepositories() throws IOException, XMLReaderException {

        final String groupId = "theGroupId";
        final String artifactId = "theArtifactId";
        final String version = "theVersion";

        final String pomString =
                "<project>"

              + "  <groupId>" + groupId + "</groupId>"
              + "  <artifactId>" + artifactId + "</artifactId>"
              + "  <version>" + version + "</version>"
              + "  <name>The project name</name>"
              + "  <url>https://the.project.url</url>"

              + "  <repositories>"
              + "     <repository>"
              + "       <releases>"
              + "         <enabled>true</enabled>"
              + "         <updatePolicy>never</updatePolicy>"
              + "         <checksumPolicy>warn</checksumPolicy>"
              + "      </releases>"
              + "       <snapshots>"
              + "         <enabled>false</enabled>"
              + "         <updatePolicy>always</updatePolicy>"
              + "         <checksumPolicy>ignore</checksumPolicy>"
              + "      </snapshots>"
              + "      <name>Plugin repository 1</name>"
              + "      <id>plugin_repository_1</id>"
              + "      <url>https://plugin.repository1</url>"
              + "      <layout>classic</layout>"
              + "    </repository>"
              + "     <repository>"
              + "       <releases>"
              + "         <enabled>false</enabled>"
              + "         <updatePolicy>daily</updatePolicy>"
              + "         <checksumPolicy>ignore</checksumPolicy>"
              + "      </releases>"
              + "       <snapshots>"
              + "         <enabled>true</enabled>"
              + "         <updatePolicy>interval:60</updatePolicy>"
              + "         <checksumPolicy>warn</checksumPolicy>"
              + "      </snapshots>"
              + "      <name>Plugin repository 2</name>"
              + "      <id>plugin_repository_2</id>"
              + "      <url>https://plugin.repository2</url>"
              + "      <layout>default</layout>"
              + "    </repository>"
              + "  </repositories>"
              + "</project>";


        final MavenProject project = parse(pomString);
        
        final List<MavenRepository> repositories = project.getCommon().getRepositories();
        
        assertThat(repositories).isNotNull();
        assertThat(repositories.size()).isEqualTo(2);
        
        assertThat(repositories.get(0).getReleases().getEnabled()).isTrue();
        assertThat(repositories.get(0).getReleases().getUpdatePolicy()).isEqualTo("never");
        assertThat(repositories.get(0).getReleases().getChecksumPolicy()).isEqualTo("warn");

        assertThat(repositories.get(0).getSnapshots().getEnabled()).isFalse();
        assertThat(repositories.get(0).getSnapshots().getUpdatePolicy()).isEqualTo("always");
        assertThat(repositories.get(0).getSnapshots().getChecksumPolicy()).isEqualTo("ignore");
        
        assertThat(repositories.get(0).getName()).isEqualTo("Plugin repository 1");
        assertThat(repositories.get(0).getId()).isEqualTo("plugin_repository_1");
        assertThat(repositories.get(0).getUrl()).isEqualTo("https://plugin.repository1");
        assertThat(repositories.get(0).getLayout()).isEqualTo("classic");

        assertThat(repositories.get(1).getReleases().getEnabled()).isFalse();
        assertThat(repositories.get(1).getReleases().getUpdatePolicy()).isEqualTo("daily");
        assertThat(repositories.get(1).getReleases().getChecksumPolicy()).isEqualTo("ignore");

        assertThat(repositories.get(1).getSnapshots().getEnabled()).isTrue();
        assertThat(repositories.get(1).getSnapshots().getUpdatePolicy()).isEqualTo("interval:60");
        assertThat(repositories.get(1).getSnapshots().getChecksumPolicy()).isEqualTo("warn");
        
        assertThat(repositories.get(1).getName()).isEqualTo("Plugin repository 2");
        assertThat(repositories.get(1).getId()).isEqualTo("plugin_repository_2");
        assertThat(repositories.get(1).getUrl()).isEqualTo("https://plugin.repository2");
        assertThat(repositories.get(1).getLayout()).isEqualTo("default");
    }
}
