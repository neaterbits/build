package com.neaterbits.build.buildsystem.maven.effective;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.w3c.dom.Document;

import com.neaterbits.build.buildsystem.maven.elements.MavenProject;
import com.neaterbits.build.buildsystem.maven.parse.PomTreeParser;
import com.neaterbits.build.buildsystem.maven.xml.MavenXMLProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.dom.DOMModel;
import com.neaterbits.build.buildsystem.maven.xml.dom.DOMReaderFactory;
import com.neaterbits.util.IOUtils;

public class EffectivePOMsHelperTest {

    final String superPomDependencyGroupId = "superPomDependencyGroupId";
    final String superPomDependencyArtifactId = "superPomDependencyArtifactId";
    final String superPomDependencyVersion = "superPomDependencyVersion";
    
    final String superPom =
              "<project>"
            + "  <dependencies>"
            + "    <dependency>"
            + "      <groupId>" + superPomDependencyGroupId + "</groupId>"
            + "      <artifactId>" + superPomDependencyArtifactId + "</artifactId>"
            + "      <version>" + superPomDependencyVersion + "</version>"
            + "    </dependency>"
            + "  </dependencies>"
            + "</project>";
    
    @Test
    public void testMergePom() throws IOException, XMLReaderException {

        final String rootGroupId = "rootGroupId";
        final String rootArtifactId = "rootArtifactId";
        final String rootVersion = "rootVersion";

        final String rootPomDependencyGroupId = "rootPomDependencyGroupId";
        final String rootPomDependencyArtifactId = "rootPomDependencyArtifactId";
        final String rootPomDependencyVersion = "rootPomDependencyVersion";

        final String rootPomString =
                "<project>"

              + "  <groupId>" + rootGroupId + "</groupId>"
              + "  <artifactId>" + rootArtifactId + "</artifactId>"
              + "  <version>" + rootVersion + "</version>"

              + "  <dependencies>"
              + "    <dependency>"
              + "      <groupId>" + rootPomDependencyGroupId + "</groupId>"
              + "      <artifactId>" + rootPomDependencyArtifactId + "</artifactId>"
              + "      <version>" + rootPomDependencyVersion + "</version>"
              + "    </dependency>"
              + "  </dependencies>"
              + "</project>";

        final String subGroupId = "subGroupId";
        final String subArtifactId = "subArtifactId";
        final String subVersion = "subVersion";

        final String subPomDependencyGroupId = "subPomDependencyGroupId";
        final String subPomDependencyArtifactId = "subPomDependencyArtifactId";
        final String subPomDependencyVersion = "subPomDependencyVersion";

        final String subPomString =
                "<project>"

              + "  <parent>"
              + "    <groupId>" + rootGroupId + "</groupId>"
              + "    <artifactId>" + rootArtifactId + "</artifactId>"
              + "    <version>" + rootVersion + "</version>"
              + "  </parent>"

              + "  <groupId>" + subGroupId + "</groupId>"
              + "  <artifactId>" + subArtifactId + "</artifactId>"
              + "  <version>" + subVersion + "</version>"

              + "  <dependencies>"
              + "    <dependency>"
              + "      <groupId>" + subPomDependencyGroupId + "</groupId>"
              + "      <artifactId>" + subPomDependencyArtifactId + "</artifactId>"
              + "      <version>" + subPomDependencyVersion + "</version>"
              + "    </dependency>"
              + "  </dependencies>"
              + "</project>";

        final MavenXMLProject<Document> rootPom;
        final MavenXMLProject<Document> subPom;

        final File rootFile = File.createTempFile("rootpom", "xml");
        final File subFile = File.createTempFile("subpom", "xml");

        final DOMReaderFactory xmlReaderFactory = new DOMReaderFactory();

        try {
            rootFile.deleteOnExit();
            subFile.deleteOnExit();
        
            IOUtils.write(rootFile, rootPomString);
            IOUtils.write(subFile, subPomString);
            
            rootPom = PomTreeParser.readModule(rootFile, xmlReaderFactory);
            subPom = PomTreeParser.readModule(subFile, xmlReaderFactory);
        }
        finally {
            rootFile.delete();
            subFile.delete();
        }
        
        final List<MavenXMLProject<Document>> xmlProjects = Arrays.asList(rootPom, subPom);
        
        final List<MavenProject> effectiveProjects = EffectivePOMsHelper.computeEffectiveProjects(
                        xmlProjects,
                        DOMModel.INSTANCE,
                        xmlReaderFactory,
                        superPom);
        
        assertThat(effectiveProjects.size()).isEqualTo(2);
        
        final MavenProject rootProject = effectiveProjects.get(0);

        assertThat(rootProject.getModuleId().getGroupId()).isEqualTo(rootGroupId);
        assertThat(rootProject.getModuleId().getArtifactId()).isEqualTo(rootArtifactId);
        assertThat(rootProject.getModuleId().getVersion()).isEqualTo(rootVersion);

        assertThat(rootProject.getDependencies().size()).isEqualTo(2);
        
        assertThat(rootProject.getDependencies().get(0).getModuleId().getGroupId())
                .isEqualTo(superPomDependencyGroupId);

        assertThat(rootProject.getDependencies().get(0).getModuleId().getArtifactId())
                .isEqualTo(superPomDependencyArtifactId);

        assertThat(rootProject.getDependencies().get(0).getModuleId().getVersion())
                .isEqualTo(superPomDependencyVersion);

        assertThat(rootProject.getDependencies().get(1).getModuleId().getGroupId())
                .isEqualTo(rootPomDependencyGroupId);

        assertThat(rootProject.getDependencies().get(1).getModuleId().getArtifactId())
                .isEqualTo(rootPomDependencyArtifactId);

        assertThat(rootProject.getDependencies().get(1).getModuleId().getVersion())
                .isEqualTo(rootPomDependencyVersion);

        final MavenProject subProject = effectiveProjects.get(1);

        assertThat(subProject.getModuleId().getGroupId()).isEqualTo(subGroupId);
        assertThat(subProject.getModuleId().getArtifactId()).isEqualTo(subArtifactId);
        assertThat(subProject.getModuleId().getVersion()).isEqualTo(subVersion);

        assertThat(subProject.getDependencies().size()).isEqualTo(3);
        
        assertThat(subProject.getDependencies().get(0).getModuleId().getGroupId())
                .isEqualTo(superPomDependencyGroupId);

        assertThat(subProject.getDependencies().get(0).getModuleId().getArtifactId())
                .isEqualTo(superPomDependencyArtifactId);

        assertThat(subProject.getDependencies().get(0).getModuleId().getVersion())
                .isEqualTo(superPomDependencyVersion);

        assertThat(subProject.getDependencies().get(1).getModuleId().getGroupId())
                .isEqualTo(rootPomDependencyGroupId);

        assertThat(subProject.getDependencies().get(1).getModuleId().getArtifactId())
                .isEqualTo(rootPomDependencyArtifactId);

        assertThat(subProject.getDependencies().get(1).getModuleId().getVersion())
                .isEqualTo(rootPomDependencyVersion);

        assertThat(subProject.getDependencies().get(2).getModuleId().getGroupId())
            .isEqualTo(subPomDependencyGroupId);

        assertThat(subProject.getDependencies().get(2).getModuleId().getArtifactId())
            .isEqualTo(subPomDependencyArtifactId);

        assertThat(subProject.getDependencies().get(2).getModuleId().getVersion())
            .isEqualTo(subPomDependencyVersion);
        
    }

	@Test
	public void testMergeMavenBuildSystemPom() throws XMLReaderException, IOException {

		final DOMReaderFactory xmlReaderFactory = new DOMReaderFactory();
	
		final MavenXMLProject<Document> rootPom
			= PomTreeParser.readModule(new File("../pom.xml"), xmlReaderFactory);

		final MavenXMLProject<Document> buildsystemMavenPOM
			= PomTreeParser.readModule(new File("./pom.xml"), xmlReaderFactory);
		
		final List<MavenXMLProject<Document>> xmlProjects = Arrays.asList(rootPom, buildsystemMavenPOM);
		
		final List<MavenProject> effectiveProjects = EffectivePOMsHelper.computeEffectiveProjects(
				        xmlProjects,
				        DOMModel.INSTANCE,
				        xmlReaderFactory,
				        superPom);

		assertThat(effectiveProjects.size()).isEqualTo(2);
	}
}
