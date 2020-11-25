package com.neaterbits.build.buildsystem.maven.project.parse;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.project.model.MavenScm;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;

public class ScmParserTest extends BasePomParserTest {

    @Test
    public void testScm() throws IOException, XMLReaderException {

        final String groupId = "theGroupId";
        final String artifactId = "theArtifactId";
        final String version = "theVersion";

        final String pomString =
                "<project>"

              + "  <groupId>" + groupId + "</groupId>"
              + "  <artifactId>" + artifactId + "</artifactId>"
              + "  <version>" + version + "</version>"

              + "  <scm>"
              + "    <connection>theConnection</connection>"
              + "    <developerConnection>theDeveloperConnection</developerConnection>"
              + "    <tag>theTag</tag>"
              + "    <url>https://scm.url</url>"
              + "  </scm>"

              + "</project>";

        final MavenProject project = parse(pomString);
        
        final MavenScm scm = project.getScm();
        
        assertThat(scm.getConnection()).isEqualTo("theConnection");
        assertThat(scm.getDeveloperConnection()).isEqualTo("theDeveloperConnection");
        assertThat(scm.getTag()).isEqualTo("theTag");
        assertThat(scm.getUrl()).isEqualTo("https://scm.url");
    }    
}
