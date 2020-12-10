package com.neaterbits.build.buildsystem.maven.variables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.w3c.dom.Document;

import com.neaterbits.build.buildsystem.maven.project.model.xml.MavenXMLProject;
import com.neaterbits.build.buildsystem.maven.project.parse.PomTreeParser;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.dom.DOMModel;
import com.neaterbits.build.buildsystem.maven.xml.dom.DOMReaderFactory;

public class VariableExpansionTest {

    @Test
    public void testOneVariable() {

        final String expanded = VariableExpansion.expandVariables(
                "${var}",
                name -> {
                    assertThat(name).isEqualTo("var");
                    
                    return "value";
                });
                
        assertThat(expanded).isEqualTo("value");
        
    }

    @Test
    public void testOneVariableWithoutValue() {

        final String expanded = VariableExpansion.expandVariables(
                "${var}",
                name -> {
                    assertThat(name).isEqualTo("var");
                    
                    return null;
                });
                
        assertThat(expanded).isEqualTo("${var}");
        
    }

    @Test
    public void testOneVariableWithoutName() {

        final String expanded = VariableExpansion.expandVariables(
                "${}",
                name -> {
                    fail("Should not be called");
                    
                    return null;
                });
                
        assertThat(expanded).isEqualTo("${}");
    }

    @Test
    public void testVariableOfNameLength1() {

        final String expanded = VariableExpansion.expandVariables(
                "${x}",
                name -> {
                    assertThat(name).isEqualTo("x");
                    
                    return "value";
                });
                
        assertThat(expanded).isEqualTo("value");
        
    }

    @Test
    public void testOneVariableWithPrefix() {

        final String expanded = VariableExpansion.expandVariables(
                "x${var}",
                name -> {
                    assertThat(name).isEqualTo("var");
                    
                    return "value";
                });
                
        assertThat(expanded).isEqualTo("xvalue");
        
    }

    @Test
    public void testOneVariableWithPrefixWithoutValue() {

        final String expanded = VariableExpansion.expandVariables(
                "x${var}",
                name -> {
                    assertThat(name).isEqualTo("var");
                    
                    return null;
                });
                
        assertThat(expanded).isEqualTo("x${var}");
        
    }

    @Test
    public void testMultipleVariablesWithSuffix() {

        final String expanded = VariableExpansion.expandVariables(
                "${var1}x${var2}y",
                name -> {

                    final String value;
                    
                    switch (name) {
                    case "var1":
                        value = "value1";
                        break;
                        
                    case "var2":
                        value = "value2";
                        break;
                        
                    default:
                        throw new IllegalArgumentException();
                    }
                    
                    return value;
                });
                
        assertThat(expanded).isEqualTo("value1xvalue2y");
        
    }

    @Test
    public void testMultipleVariablesWithSuffixWithoutValue() {

        final String expanded = VariableExpansion.expandVariables(
                "${var1}x${var2}y",
                name -> {
                    return null;
                });
                
        assertThat(expanded).isEqualTo("${var1}x${var2}y");
    }

    @Test
    public void testModel() throws XMLReaderException, IOException {
        
        final String groupId = "theGroupId";
        final String artifactId = "theArtifactId";
        final String version = "theVersion";

        final String rootPomString =
                "<project>"

              + "  <groupId>" + groupId + "</groupId>"
              + "  <artifactId>" + artifactId + "</artifactId>"
              + "  <version>" + version + "</version>"

              + "  <build>"
              + "     <directory>file-${theProperty}</directory>"
              + "     <outputDirectory>output-${project.build.directory}</outputDirectory>"
              + "  </build>"

              + "</project>";

        final DOMReaderFactory xmlReaderFactory = new DOMReaderFactory();

        final MavenXMLProject<Document> rootPom
            = PomTreeParser.readModule(new ByteArrayInputStream(rootPomString.getBytes()), xmlReaderFactory, "./pom.xml");

        assertThat(VariableExpansion.replaceVariable("test-${project.groupId}-value", null, null, DOMModel.INSTANCE, rootPom.getDocument()))
            .isEqualTo("test-" + groupId + "-value");

        assertThat(VariableExpansion.replaceVariable("${project.version}", null, null, DOMModel.INSTANCE, rootPom.getDocument()))
            .isEqualTo(version);
        
        final Map<String, String> properties = new HashMap<>();

        properties.put("theProperty", "theValue-${project.groupId}-directory");
        
        assertThat(VariableExpansion.replaceVariable("${project.build.outputDirectory}", null, properties, DOMModel.INSTANCE, rootPom.getDocument()))
            .isEqualTo("output-file-theValue-" + groupId + "-directory");
    }

    @Test
    public void testBuiltinVariables() {

        final String rootDirectoryString = "/the/root/directory";
        final File rootDirectory = new File(rootDirectoryString);
        
        final String nowString = "2019-01-02T01:02:03Z";
        
        final ZonedDateTime now = ZonedDateTime.parse(nowString);
        
        final MavenBuiltinVariables builtinVariables = new MavenBuiltinVariables(rootDirectory, now);
        
        assertThat(VariableExpansion.replaceVariable("${project.basedir}", builtinVariables, null, null, null))
            .isEqualTo(rootDirectory.getAbsolutePath());

        assertThat(VariableExpansion.replaceVariable("${project.baseUri}", builtinVariables, null, null, null))
            .isEqualTo("file:/" + rootDirectory.getAbsolutePath());

        assertThat(VariableExpansion.replaceVariable("${maven.build.timestamp}", builtinVariables, null, null, null))
            .isEqualTo(nowString);
        
        final Map<String, String> properties = new HashMap<>();
        
        properties.put("maven.build.timestamp.format", "yyyy-MM-dd HH:mm:ss");
        
        assertThat(VariableExpansion.replaceVariable("${maven.build.timestamp}", builtinVariables, properties, null, null))
            .isEqualTo("2019-01-02 01:02:03");
    }
}
