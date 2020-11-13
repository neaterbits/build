package com.neaterbits.build.buildsystem.maven.parse;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.elements.MavenProfile;
import com.neaterbits.build.buildsystem.maven.elements.MavenProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;

public class ProfilesParserTest extends BasePomParserTest {

    @Test
    public void testProfiles() throws IOException, XMLReaderException {

        final String groupId = "theGroupId";
        final String artifactId = "theArtifactId";
        final String version = "theVersion";

        final String pomString =
                "<project>"

              + "  <groupId>" + groupId + "</groupId>"
              + "  <artifactId>" + artifactId + "</artifactId>"
              + "  <version>" + version + "</version>"

              + "  <profiles>"
              + "    <profile>"
              + "      <id>testId</id>"
              + "      <activation>"
              + "        <activeByDefault>true</activeByDefault>"
              + "        <jdk>1.5</jdk>"
              + "        <os>"
              + "          <name>Windows 10</name>"
              + "          <family>Windows</family>"
              + "          <arch>x86</arch>"
              + "          <version>theVersion</version>"
              + "        </os>"
              + "        <property>"
              + "          <name>theName</name>"
              + "          <value>theValue</value>"
              + "        </property>"
              + "        <file>"
              + "          <exists>existsFile</exists>"
              + "          <missing>missingFile</missing>"
              + "        </file>"
              + "      </activation>"
              + "      <build></build>"
              + "      <modules></modules>"
              + "      <repositories></repositories>"
              + "      <pluginRepositories></pluginRepositories>"
              + "      <dependencies></dependencies>"
              + "    </profile>"
              + "  </profiles>"

              + "</project>";

        final MavenProject project = parse(pomString);
        
        assertThat(project.getProfiles().size()).isEqualTo(1);
        
        final MavenProfile profile = project.getProfiles().get(0);

        assertThat(profile.getActivation().getActiveByDefault()).isEqualTo(Boolean.TRUE);
        assertThat(profile.getActivation().getJdk()).isEqualTo("1.5");
        
        assertThat(profile.getActivation().getOs().getName()).isEqualTo("Windows 10");
        assertThat(profile.getActivation().getOs().getFamily()).isEqualTo("Windows");
        assertThat(profile.getActivation().getOs().getArch()).isEqualTo("x86");
        assertThat(profile.getActivation().getOs().getVersion()).isEqualTo("theVersion");
        
        assertThat(profile.getActivation().getProperty().getName()).isEqualTo("theName");
        assertThat(profile.getActivation().getProperty().getValue()).isEqualTo("theValue");

        assertThat(profile.getActivation().getFile().getExists()).isEqualTo("existsFile");
        assertThat(profile.getActivation().getFile().getMissing()).isEqualTo("missingFile");

        assertThat(profile.getBuild()).isNotNull();
        assertThat(profile.getModules()).isEmpty();
        assertThat(profile.getRepositories()).isEmpty();
        assertThat(profile.getPluginRepositories()).isEmpty();
        assertThat(profile.getDependencies()).isEmpty();
    }    
}
