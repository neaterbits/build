package com.neaterbits.build.buildsystem.maven.project.parse;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.project.model.MavenPluginRepository;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;

public class PluginRepositoriesParserTest extends BasePomParserTest {

    @Test
    public void testParsePluginRepositories() throws IOException, XMLReaderException {

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

              + "  <pluginRepositories>"
              + "     <pluginRepository>"
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
              + "    </pluginRepository>"
              + "     <pluginRepository>"
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
              + "    </pluginRepository>"
              + "  </pluginRepositories>"
              + "</project>";


        final MavenProject project = parse(pomString);
        
        final List<MavenPluginRepository> pluginRepositories = project.getCommon().getPluginRepositories();
        
        assertThat(pluginRepositories).isNotNull();
        assertThat(pluginRepositories.size()).isEqualTo(2);
        
        assertThat(pluginRepositories.get(0).getReleases().getEnabled()).isTrue();
        assertThat(pluginRepositories.get(0).getReleases().getUpdatePolicy()).isEqualTo("never");
        assertThat(pluginRepositories.get(0).getReleases().getChecksumPolicy()).isEqualTo("warn");

        assertThat(pluginRepositories.get(0).getSnapshots().getEnabled()).isFalse();
        assertThat(pluginRepositories.get(0).getSnapshots().getUpdatePolicy()).isEqualTo("always");
        assertThat(pluginRepositories.get(0).getSnapshots().getChecksumPolicy()).isEqualTo("ignore");
        
        assertThat(pluginRepositories.get(0).getName()).isEqualTo("Plugin repository 1");
        assertThat(pluginRepositories.get(0).getId()).isEqualTo("plugin_repository_1");
        assertThat(pluginRepositories.get(0).getUrl()).isEqualTo("https://plugin.repository1");
        assertThat(pluginRepositories.get(0).getLayout()).isEqualTo("classic");

        assertThat(pluginRepositories.get(1).getReleases().getEnabled()).isFalse();
        assertThat(pluginRepositories.get(1).getReleases().getUpdatePolicy()).isEqualTo("daily");
        assertThat(pluginRepositories.get(1).getReleases().getChecksumPolicy()).isEqualTo("ignore");

        assertThat(pluginRepositories.get(1).getSnapshots().getEnabled()).isTrue();
        assertThat(pluginRepositories.get(1).getSnapshots().getUpdatePolicy()).isEqualTo("interval:60");
        assertThat(pluginRepositories.get(1).getSnapshots().getChecksumPolicy()).isEqualTo("warn");
        
        assertThat(pluginRepositories.get(1).getName()).isEqualTo("Plugin repository 2");
        assertThat(pluginRepositories.get(1).getId()).isEqualTo("plugin_repository_2");
        assertThat(pluginRepositories.get(1).getUrl()).isEqualTo("https://plugin.repository2");
        assertThat(pluginRepositories.get(1).getLayout()).isEqualTo("default");
    }
}
