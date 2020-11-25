package com.neaterbits.build.buildsystem.maven.effective;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.w3c.dom.Document;

import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.project.model.xml.MavenXMLProject;
import com.neaterbits.build.buildsystem.maven.project.parse.PomTreeParser;
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
    public void testMergePomDependencies() throws IOException, XMLReaderException {

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
                        superPom,
                        MavenResolveContext.now());
        
        assertThat(effectiveProjects.size()).isEqualTo(2);
        
        final MavenProject rootProject = effectiveProjects.get(0);

        assertThat(rootProject.getModuleId().getGroupId()).isEqualTo(rootGroupId);
        assertThat(rootProject.getModuleId().getArtifactId()).isEqualTo(rootArtifactId);
        assertThat(rootProject.getModuleId().getVersion()).isEqualTo(rootVersion);

        final List<MavenDependency> rootDependencies = rootProject.getCommon().getDependencies();
        
        assertThat(rootDependencies.size()).isEqualTo(2);
        
        assertThat(rootDependencies.get(0).getModuleId().getGroupId())
                .isEqualTo(superPomDependencyGroupId);

        assertThat(rootDependencies.get(0).getModuleId().getArtifactId())
                .isEqualTo(superPomDependencyArtifactId);

        assertThat(rootDependencies.get(0).getModuleId().getVersion())
                .isEqualTo(superPomDependencyVersion);

        assertThat(rootDependencies.get(1).getModuleId().getGroupId())
                .isEqualTo(rootPomDependencyGroupId);

        assertThat(rootDependencies.get(1).getModuleId().getArtifactId())
                .isEqualTo(rootPomDependencyArtifactId);

        assertThat(rootDependencies.get(1).getModuleId().getVersion())
                .isEqualTo(rootPomDependencyVersion);

        final MavenProject subProject = effectiveProjects.get(1);

        assertThat(subProject.getModuleId().getGroupId()).isEqualTo(subGroupId);
        assertThat(subProject.getModuleId().getArtifactId()).isEqualTo(subArtifactId);
        assertThat(subProject.getModuleId().getVersion()).isEqualTo(subVersion);

        final List<MavenDependency> subDependencies = subProject.getCommon().getDependencies();

        assertThat(subDependencies.size()).isEqualTo(3);
        
        assertThat(subDependencies.get(0).getModuleId().getGroupId())
                .isEqualTo(superPomDependencyGroupId);

        assertThat(subDependencies.get(0).getModuleId().getArtifactId())
                .isEqualTo(superPomDependencyArtifactId);

        assertThat(subDependencies.get(0).getModuleId().getVersion())
                .isEqualTo(superPomDependencyVersion);

        assertThat(subDependencies.get(1).getModuleId().getGroupId())
                .isEqualTo(rootPomDependencyGroupId);

        assertThat(subDependencies.get(1).getModuleId().getArtifactId())
                .isEqualTo(rootPomDependencyArtifactId);

        assertThat(subDependencies.get(1).getModuleId().getVersion())
                .isEqualTo(rootPomDependencyVersion);

        assertThat(subDependencies.get(2).getModuleId().getGroupId())
            .isEqualTo(subPomDependencyGroupId);

        assertThat(subDependencies.get(2).getModuleId().getArtifactId())
            .isEqualTo(subPomDependencyArtifactId);

        assertThat(subDependencies.get(2).getModuleId().getVersion())
            .isEqualTo(subPomDependencyVersion);
    }

    @Test
    public void testMergePomProperties() throws IOException, XMLReaderException {

        final String rootGroupId = "rootGroupId";
        final String rootArtifactId = "rootArtifactId";
        final String rootVersion = "rootVersion";

        final String rootPomString =
                "<project>"

              + "  <groupId>" + rootGroupId + "</groupId>"
              + "  <artifactId>" + rootArtifactId + "</artifactId>"
              + "  <version>" + rootVersion + "</version>"

              + "  <properties>"
              + "    <rootProperty>rootValue</rootProperty>"
              + "    <overrideProperty>overridable</overrideProperty>"
              + "  </properties>"
              
              + "</project>";

        final String subGroupId = "subGroupId";
        final String subArtifactId = "subArtifactId";
        final String subVersion = "subVersion";

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

              + "  <properties>"
              + "    <subProperty>subValue</subProperty>"
              + "    <overrideProperty>overridden</overrideProperty>"
              + "  </properties>"

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
                        superPom,
                        MavenResolveContext.now());
        
        assertThat(effectiveProjects.size()).isEqualTo(2);
        
        final MavenProject rootProject = effectiveProjects.get(0);

        assertThat(rootProject.getModuleId().getGroupId()).isEqualTo(rootGroupId);
        assertThat(rootProject.getModuleId().getArtifactId()).isEqualTo(rootArtifactId);
        assertThat(rootProject.getModuleId().getVersion()).isEqualTo(rootVersion);

        assertThat(rootProject.getProperties().size()).isEqualTo(2);
        assertThat(rootProject.getProperties().get("rootProperty")).isEqualTo("rootValue");
        assertThat(rootProject.getProperties().get("overrideProperty")).isEqualTo("overridable");
        
        final MavenProject subProject = effectiveProjects.get(1);

        assertThat(subProject.getModuleId().getGroupId()).isEqualTo(subGroupId);
        assertThat(subProject.getModuleId().getArtifactId()).isEqualTo(subArtifactId);
        assertThat(subProject.getModuleId().getVersion()).isEqualTo(subVersion);

        assertThat(subProject.getProperties().size()).isEqualTo(3);
        assertThat(subProject.getProperties().get("rootProperty")).isEqualTo("rootValue");
        assertThat(subProject.getProperties().get("overrideProperty")).isEqualTo("overridden");
        assertThat(subProject.getProperties().get("subProperty")).isEqualTo("subValue");
    }

    @Test
    public void testReplacePomProperties() throws IOException, XMLReaderException {

        final String rootGroupId = "rootGroupId";
        final String rootArtifactId = "rootArtifactId";
        final String rootVersion = "rootVersion";

        final String rootPomString =
                "<project>"

              + "  <groupId>" + rootGroupId + "</groupId>"
              + "  <artifactId>" + rootArtifactId + "</artifactId>"
              + "  <version>" + rootVersion + "</version>"

              + "  <properties>"
              + "    <rootProperty>rootValue</rootProperty>"
              + "    <replaceProperty>replaceWith</replaceProperty>"
              + "  </properties>"
              
              + "</project>";

        final String subGroupId = "subGroupId";
        final String subArtifactId = "subArtifactId";
        final String subVersion = "subVersion";

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

              + "  <properties>"
              + "    <subProperty>subValue${replaceProperty}</subProperty>"
              + "    <envProperty>${env.PATH}</envProperty>"
              + "  </properties>"

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
                        superPom,
                        MavenResolveContext.now());
        
        assertThat(effectiveProjects.size()).isEqualTo(2);
        
        final MavenProject rootProject = effectiveProjects.get(0);

        assertThat(rootProject.getModuleId().getGroupId()).isEqualTo(rootGroupId);
        assertThat(rootProject.getModuleId().getArtifactId()).isEqualTo(rootArtifactId);
        assertThat(rootProject.getModuleId().getVersion()).isEqualTo(rootVersion);

        assertThat(rootProject.getProperties().size()).isEqualTo(2);
        assertThat(rootProject.getProperties().get("rootProperty")).isEqualTo("rootValue");
        assertThat(rootProject.getProperties().get("replaceProperty")).isEqualTo("replaceWith");
        
        final MavenProject subProject = effectiveProjects.get(1);

        assertThat(subProject.getModuleId().getGroupId()).isEqualTo(subGroupId);
        assertThat(subProject.getModuleId().getArtifactId()).isEqualTo(subArtifactId);
        assertThat(subProject.getModuleId().getVersion()).isEqualTo(subVersion);

        assertThat(subProject.getProperties().size()).isEqualTo(4);
        assertThat(subProject.getProperties().get("rootProperty")).isEqualTo("rootValue");
        assertThat(subProject.getProperties().get("replaceProperty")).isEqualTo("replaceWith");
        assertThat(subProject.getProperties().get("subProperty")).isEqualTo("subValuereplaceWith");
        assertThat(subProject.getProperties().get("envProperty")).isEqualTo(System.getenv("PATH"));
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
				        superPom,
				        MavenResolveContext.now());

		assertThat(effectiveProjects.size()).isEqualTo(2);
	}
}
