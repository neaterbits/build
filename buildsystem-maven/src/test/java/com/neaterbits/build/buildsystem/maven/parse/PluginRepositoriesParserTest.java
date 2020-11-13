package com.neaterbits.build.buildsystem.maven.parse;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.elements.MavenProject;
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
        
        assertThat(project.getPluginRepositories()).isNotNull();
        assertThat(project.getPluginRepositories().size()).isEqualTo(2);
        
        assertThat(project.getPluginRepositories().get(0).getReleases().getEnabled()).isTrue();
        assertThat(project.getPluginRepositories().get(0).getReleases().getUpdatePolicy()).isEqualTo("never");
        assertThat(project.getPluginRepositories().get(0).getReleases().getChecksumPolicy()).isEqualTo("warn");

        assertThat(project.getPluginRepositories().get(0).getSnapshots().getEnabled()).isFalse();
        assertThat(project.getPluginRepositories().get(0).getSnapshots().getUpdatePolicy()).isEqualTo("always");
        assertThat(project.getPluginRepositories().get(0).getSnapshots().getChecksumPolicy()).isEqualTo("ignore");
        
        assertThat(project.getPluginRepositories().get(0).getName()).isEqualTo("Plugin repository 1");
        assertThat(project.getPluginRepositories().get(0).getId()).isEqualTo("plugin_repository_1");
        assertThat(project.getPluginRepositories().get(0).getUrl()).isEqualTo("https://plugin.repository1");
        assertThat(project.getPluginRepositories().get(0).getLayout()).isEqualTo("classic");

        assertThat(project.getPluginRepositories().get(1).getReleases().getEnabled()).isFalse();
        assertThat(project.getPluginRepositories().get(1).getReleases().getUpdatePolicy()).isEqualTo("daily");
        assertThat(project.getPluginRepositories().get(1).getReleases().getChecksumPolicy()).isEqualTo("ignore");

        assertThat(project.getPluginRepositories().get(1).getSnapshots().getEnabled()).isTrue();
        assertThat(project.getPluginRepositories().get(1).getSnapshots().getUpdatePolicy()).isEqualTo("interval:60");
        assertThat(project.getPluginRepositories().get(1).getSnapshots().getChecksumPolicy()).isEqualTo("warn");
        
        assertThat(project.getPluginRepositories().get(1).getName()).isEqualTo("Plugin repository 2");
        assertThat(project.getPluginRepositories().get(1).getId()).isEqualTo("plugin_repository_2");
        assertThat(project.getPluginRepositories().get(1).getUrl()).isEqualTo("https://plugin.repository2");
        assertThat(project.getPluginRepositories().get(1).getLayout()).isEqualTo("default");
    }
}
