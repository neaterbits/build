package com.neaterbits.build.buildsystem.maven.project.parse;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.w3c.dom.Document;

import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.project.model.xml.MavenXMLProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.dom.DOMReaderFactory;
import com.neaterbits.util.IOUtils;

public class PropertiesParserTest {

    @Test
    public void testParseProperties() throws IOException, XMLReaderException {

        final String groupId = "rootGroupId";
        final String artifactId = "rootArtifactId";
        final String version = "rootVersion";

        final String rootPomString =
                "<project>"

              + "  <groupId>" + groupId + "</groupId>"
              + "  <artifactId>" + artifactId + "</artifactId>"
              + "  <version>" + version + "</version>"

              + "  <properties>"
              + "     <property1>value1</property1>"
              + "     <property2>value2</property2>"
              + "     <property3>value3</property3>"
              + "  </properties>"
              
              + "</project>";

        final MavenXMLProject<Document> pom;

        final File rootFile = File.createTempFile("pom", "xml");

        final DOMReaderFactory xmlReaderFactory = new DOMReaderFactory();

        try {
            rootFile.deleteOnExit();
        
            IOUtils.write(rootFile, rootPomString);
            
            pom = PomTreeParser.readModule(rootFile, xmlReaderFactory);
        }
        finally {
            rootFile.delete();
        }

        final MavenProject project = pom.getProject();

        assertThat(project.getProperties()).isNotNull();
        assertThat(project.getProperties().size()).isEqualTo(3);
        assertThat(project.getProperties().get("property1")).isEqualTo("value1");
        assertThat(project.getProperties().get("property2")).isEqualTo("value2");
        assertThat(project.getProperties().get("property3")).isEqualTo("value3");
        
    }
}
