package com.neaterbits.build.buildsystem.maven.effective;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.w3c.dom.Document;

import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
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
        
        final List<DocumentModule<Document>> modules = Arrays.asList(
                new DocumentModule<>(
                        new MavenModuleId(rootGroupId, rootArtifactId, rootVersion),
                        rootPom),
                
                new DocumentModule<>(
                        new MavenModuleId(subGroupId, subArtifactId, subVersion),
                        subPom));
        
        final List<MavenProject> effectiveProjects = EffectivePOMsHelper.computeEffectiveProjects(
                        modules,
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
        
        final List<DocumentModule<Document>> modules = Arrays.asList(
                new DocumentModule<>(
                        new MavenModuleId(rootGroupId, rootArtifactId, rootVersion),
                        rootPom),
                
                new DocumentModule<>(
                        new MavenModuleId(subGroupId, subArtifactId, subVersion),
                        subPom));
        
        final List<MavenProject> effectiveProjects = EffectivePOMsHelper.computeEffectiveProjects(
                        modules,
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
        
        final List<DocumentModule<Document>> modules = Arrays.asList(
                new DocumentModule<>(
                        new MavenModuleId(rootGroupId, rootArtifactId, rootVersion),
                        rootPom),
                
                new DocumentModule<>(
                        new MavenModuleId(subGroupId, subArtifactId, subVersion),
                        subPom));
        
        final List<MavenProject> effectiveProjects = EffectivePOMsHelper.computeEffectiveProjects(
                        modules,
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
    public void testPropsDependencies() throws IOException, XMLReaderException {

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

              + "  <properties>"
              + "    <rootPomDependencyGroupId>" + rootPomDependencyGroupId + "</rootPomDependencyGroupId>"
              + "    <rootPomDependencyVersion>" + rootPomDependencyVersion + "</rootPomDependencyVersion>"
              + "  </properties>"

              + "  <dependencies>"
              + "    <dependency>"
              + "      <groupId>${rootPomDependencyGroupId}</groupId>"
              + "      <artifactId>" + rootPomDependencyArtifactId + "</artifactId>"
              + "      <version>${rootPomDependencyVersion}</version>"
              + "    </dependency>"
              + "  </dependencies>"
              + "</project>";

        final String subGroupId = "subGroupId";
        final String subArtifactId = "subArtifactId";
        final String subVersion = "subVersion";

        final String subPomDependencyArtifactId = "subPomDependencyArtifactId";
        final String subPomDependencyOtherArtifactId = "subPomDependencyOtherArtifactId";
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

              + "  <properties>"
              + "    <subPomDependencyVersion>" + subPomDependencyVersion + "</subPomDependencyVersion>"
              + "  </properties>"

              + "  <dependencies>"
              + "    <dependency>"
              + "      <groupId>${rootPomDependencyGroupId}</groupId>"
              + "      <artifactId>" + subPomDependencyArtifactId + "</artifactId>"
              + "      <version>${subPomDependencyVersion}</version>"
              + "    </dependency>"
              + "    <dependency>"
              + "      <groupId>${rootPomDependencyGroupId}</groupId>"
              + "      <artifactId>" + subPomDependencyOtherArtifactId + "</artifactId>"
              + "      <version>1.1</version>"
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
        
        final List<DocumentModule<Document>> modules = Arrays.asList(
                new DocumentModule<>(
                        new MavenModuleId(rootGroupId, rootArtifactId, rootVersion),
                        rootPom),
                
                new DocumentModule<>(
                        new MavenModuleId(subGroupId, subArtifactId, subVersion),
                        subPom));
        
        final List<MavenProject> effectiveProjects = EffectivePOMsHelper.computeEffectiveProjects(
                        modules,
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

        assertThat(subDependencies.size()).isEqualTo(4);
        
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
            .isEqualTo(rootPomDependencyGroupId);

        assertThat(subDependencies.get(2).getModuleId().getArtifactId())
            .isEqualTo(subPomDependencyArtifactId);

        assertThat(subDependencies.get(2).getModuleId().getVersion())
            .isEqualTo(subPomDependencyVersion);

        assertThat(subDependencies.get(3).getModuleId().getGroupId())
            .isEqualTo(rootPomDependencyGroupId);

        assertThat(subDependencies.get(3).getModuleId().getArtifactId())
            .isEqualTo(subPomDependencyOtherArtifactId);
    
        assertThat(subDependencies.get(3).getModuleId().getVersion())
            .isEqualTo("1.1");
    }

    @Test
	public void testMergeMavenBuildSystemPom() throws XMLReaderException, IOException {

		final DOMReaderFactory xmlReaderFactory = new DOMReaderFactory();
	
		final MavenXMLProject<Document> rootPom
			= PomTreeParser.readModule(new File("../pom.xml"), xmlReaderFactory);

		final MavenXMLProject<Document> buildsystemMavenCorePOM
			= PomTreeParser.readModule(new File("./pom.xml"), xmlReaderFactory);
		
        final List<DocumentModule<Document>> modules = Arrays.asList(
                new DocumentModule<>(
                        new MavenModuleId("com.neaterbits.build", "root", "0.0.1-SNAPSHOT"),
                        rootPom),
                
                new DocumentModule<>(
                        new MavenModuleId("com.neaterbits.build", "buildsystem-maven-core", "0.0.1-SNAPSHOT"),
                        buildsystemMavenCorePOM));
		
		final List<MavenProject> effectiveProjects = EffectivePOMsHelper.computeEffectiveProjects(
				        modules,
				        DOMModel.INSTANCE,
				        xmlReaderFactory,
				        superPom,
				        MavenResolveContext.now());

		assertThat(effectiveProjects.size()).isEqualTo(2);
	}

    @Test
    public void testSubPomInheritsGroupIdAndVersion() throws IOException, XMLReaderException {

        final String rootGroupId = "rootGroupId";
        final String rootArtifactId = "rootArtifactId";
        final String rootVersion = "rootVersion";

        final String rootPomString =
                "<project>"

              + "  <groupId>" + rootGroupId + "</groupId>"
              + "  <artifactId>" + rootArtifactId + "</artifactId>"
              + "  <version>" + rootVersion + "</version>"

              + "</project>";

        final String subArtifactId = "subArtifactId";

        final String subPomString =
                "<project>"

              + "  <parent>"
              + "    <groupId>" + rootGroupId + "</groupId>"
              + "    <artifactId>" + rootArtifactId + "</artifactId>"
              + "    <version>" + rootVersion + "</version>"
              + "  </parent>"

              + "  <artifactId>" + subArtifactId + "</artifactId>"
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
        
        final String superPom =
                "<project>"
              + "</project>";

        final List<DocumentModule<Document>> modules = Arrays.asList(
                new DocumentModule<>(
                        new MavenModuleId(rootGroupId, rootArtifactId, rootVersion),
                        rootPom),
                
                new DocumentModule<>(
                        new MavenModuleId(rootGroupId, subArtifactId, rootVersion),
                        subPom));
        
        final List<MavenProject> effectiveProjects = EffectivePOMsHelper.computeEffectiveProjects(
                        modules,
                        DOMModel.INSTANCE,
                        xmlReaderFactory,
                        superPom,
                        MavenResolveContext.now());
        
        assertThat(effectiveProjects.size()).isEqualTo(2);
        
        final MavenProject rootProject = effectiveProjects.get(0);

        assertThat(rootProject.getModuleId().getGroupId()).isEqualTo(rootGroupId);
        assertThat(rootProject.getModuleId().getArtifactId()).isEqualTo(rootArtifactId);
        assertThat(rootProject.getModuleId().getVersion()).isEqualTo(rootVersion);

        assertThat(rootProject.getCommon().getDependencies()).isNullOrEmpty();
        
        final MavenProject subProject = effectiveProjects.get(1);

        assertThat(subProject.getModuleId().getGroupId()).isEqualTo(rootGroupId);
        assertThat(subProject.getModuleId().getArtifactId()).isEqualTo(subArtifactId);
        assertThat(subProject.getModuleId().getVersion()).isEqualTo(rootVersion);

        assertThat(subProject.getCommon().getDependencies()).isNullOrEmpty();
    }

    @Test
    public void testSubOfSubPomInheritsGroupIdAndVersion() throws IOException, XMLReaderException {

        final String rootGroupId = "rootGroupId";
        final String rootArtifactId = "rootArtifactId";
        final String rootVersion = "rootVersion";

        final String rootPomString =
                "<project>"

              + "  <groupId>" + rootGroupId + "</groupId>"
              + "  <artifactId>" + rootArtifactId + "</artifactId>"
              + "  <version>" + rootVersion + "</version>"

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
              + "</project>";

        final String subOfSubArtifactId = "subOfSubArtifactId";
        
        final String subOfSubPomDependencyArtifactId = "subOfSubPomDependencyArtifactId";

        final String subOfSubPomString =
                "<project>"

              + "  <parent>"
              + "    <groupId>" + subGroupId + "</groupId>"
              + "    <artifactId>" + subArtifactId + "</artifactId>"
              + "    <version>" + subVersion + "</version>"
              + "  </parent>"

              + "  <artifactId>" + subOfSubArtifactId + "</artifactId>"
              + "  <dependencies>"
              + "    <dependency>"
              + "      <groupId>${project.groupId}</groupId>"
              + "      <artifactId>" + subOfSubPomDependencyArtifactId + "</artifactId>"
              + "      <version>${project.version}</version>"
              + "    </dependency>"
              + "  </dependencies>"
              + "</project>";

        final MavenXMLProject<Document> rootPom;
        final MavenXMLProject<Document> subPom;
        final MavenXMLProject<Document> subOfSubPom;

        final File rootFile = File.createTempFile("rootpom", "xml");
        final File subFile = File.createTempFile("subpom", "xml");
        final File subOfSubFile = File.createTempFile("subofsubpom", "xml");

        final DOMReaderFactory xmlReaderFactory = new DOMReaderFactory();

        try {
            rootFile.deleteOnExit();
            subFile.deleteOnExit();
            subOfSubFile.deleteOnExit();
        
            IOUtils.write(rootFile, rootPomString);
            IOUtils.write(subFile, subPomString);
            IOUtils.write(subOfSubFile, subOfSubPomString);
            
            rootPom = PomTreeParser.readModule(rootFile, xmlReaderFactory);
            subPom = PomTreeParser.readModule(subFile, xmlReaderFactory);
            subOfSubPom = PomTreeParser.readModule(subOfSubFile, xmlReaderFactory);
        }
        finally {
            rootFile.delete();
            subFile.delete();
            subOfSubFile.delete();
        }
        
        final String superPom =
                "<project>"
              + "</project>";

        final List<DocumentModule<Document>> modules = Arrays.asList(
                new DocumentModule<>(
                        new MavenModuleId(rootGroupId, rootArtifactId, rootVersion),
                        rootPom),
                
                new DocumentModule<>(
                        new MavenModuleId(subGroupId, subArtifactId, subVersion),
                        subPom),

                new DocumentModule<>(
                        new MavenModuleId(subGroupId, subOfSubArtifactId, subVersion),
                        subOfSubPom));
        
        final List<MavenProject> effectiveProjects = EffectivePOMsHelper.computeEffectiveProjects(
                        modules,
                        DOMModel.INSTANCE,
                        xmlReaderFactory,
                        superPom,
                        MavenResolveContext.now());
        
        assertThat(effectiveProjects.size()).isEqualTo(3);
        
        final MavenProject rootProject = effectiveProjects.get(0);

        assertThat(rootProject.getModuleId().getGroupId()).isEqualTo(rootGroupId);
        assertThat(rootProject.getModuleId().getArtifactId()).isEqualTo(rootArtifactId);
        assertThat(rootProject.getModuleId().getVersion()).isEqualTo(rootVersion);

        assertThat(rootProject.getCommon().getDependencies()).isNullOrEmpty();
        
        final MavenProject subProject = effectiveProjects.get(1);

        assertThat(subProject.getModuleId().getGroupId()).isEqualTo(subGroupId);
        assertThat(subProject.getModuleId().getArtifactId()).isEqualTo(subArtifactId);
        assertThat(subProject.getModuleId().getVersion()).isEqualTo(subVersion);

        assertThat(subProject.getCommon().getDependencies()).isNullOrEmpty();

        final MavenProject subOfSubProject = effectiveProjects.get(2);

        assertThat(subOfSubProject.getModuleId().getGroupId()).isEqualTo(subGroupId);
        assertThat(subOfSubProject.getModuleId().getArtifactId()).isEqualTo(subOfSubArtifactId);
        assertThat(subOfSubProject.getModuleId().getVersion()).isEqualTo(subVersion);

        assertThat(subOfSubPom.getProject().getCommon().getDependencies().size()).isEqualTo(1);
        
        final List<MavenDependency> subOfSubDependencies = subOfSubProject.getCommon().getDependencies();

        assertThat(subOfSubDependencies.size()).isEqualTo(1);
        
        assertThat(subOfSubDependencies.get(0).getModuleId().getGroupId())
                .isEqualTo(subGroupId);

        assertThat(subOfSubDependencies.get(0).getModuleId().getArtifactId())
                .isEqualTo(subOfSubPomDependencyArtifactId);

        assertThat(subOfSubDependencies.get(0).getModuleId().getVersion())
                .isEqualTo(subVersion);
    }

    @Test
    public void testSubPomDependenciesInheritGroupIdAndVersion() throws IOException, XMLReaderException {

        final String rootGroupId = "rootGroupId";
        final String rootArtifactId = "rootArtifactId";
        final String rootVersion = "rootVersion";

        final String rootPomString =
                "<project>"

              + "  <groupId>" + rootGroupId + "</groupId>"
              + "  <artifactId>" + rootArtifactId + "</artifactId>"
              + "  <version>" + rootVersion + "</version>"

              + "</project>";

        final String subArtifactId = "subArtifactId";

        final String subPomDependencyArtifactId = "subPomDependencyArtifactId";

        final String subPomString =
                "<project>"

              + "  <parent>"
              + "    <groupId>" + rootGroupId + "</groupId>"
              + "    <artifactId>" + rootArtifactId + "</artifactId>"
              + "    <version>" + rootVersion + "</version>"
              + "  </parent>"

              + "  <artifactId>" + subArtifactId + "</artifactId>"

              + "  <dependencies>"
              + "    <dependency>"
              + "      <groupId>${project.groupId}</groupId>"
              + "      <artifactId>" + subPomDependencyArtifactId + "</artifactId>"
              + "      <version>${project.version}</version>"
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

        final String superPom =
                "<project>"
              + "</project>";

        final List<DocumentModule<Document>> modules = Arrays.asList(
                new DocumentModule<>(
                        new MavenModuleId(rootGroupId, rootArtifactId, rootVersion),
                        rootPom),
                
                new DocumentModule<>(
                        new MavenModuleId(rootGroupId, subArtifactId, rootVersion),
                        subPom));
        
        final List<MavenProject> effectiveProjects = EffectivePOMsHelper.computeEffectiveProjects(
                        modules,
                        DOMModel.INSTANCE,
                        xmlReaderFactory,
                        superPom,
                        MavenResolveContext.now());
        
        assertThat(effectiveProjects.size()).isEqualTo(2);
        
        final MavenProject rootProject = effectiveProjects.get(0);

        assertThat(rootProject.getModuleId().getGroupId()).isEqualTo(rootGroupId);
        assertThat(rootProject.getModuleId().getArtifactId()).isEqualTo(rootArtifactId);
        assertThat(rootProject.getModuleId().getVersion()).isEqualTo(rootVersion);

        assertThat(rootProject.getCommon().getDependencies()).isNullOrEmpty();

        final MavenProject subProject = effectiveProjects.get(1);

        assertThat(subProject.getModuleId().getGroupId()).isEqualTo(rootGroupId);
        assertThat(subProject.getModuleId().getArtifactId()).isEqualTo(subArtifactId);
        assertThat(subProject.getModuleId().getVersion()).isEqualTo(rootVersion);

        final List<MavenDependency> subDependencies = subProject.getCommon().getDependencies();

        assertThat(subDependencies.size()).isEqualTo(1);
        
        assertThat(subDependencies.get(0).getModuleId().getGroupId())
                .isEqualTo(rootGroupId);

        assertThat(subDependencies.get(0).getModuleId().getArtifactId())
                .isEqualTo(subPomDependencyArtifactId);

        assertThat(subDependencies.get(0).getModuleId().getVersion())
                .isEqualTo(rootVersion);
    }

    @Test
    public void testDependencyManagement() throws IOException, XMLReaderException {

        final String rootGroupId = "rootGroupId";
        final String rootArtifactId = "rootArtifactId";
        final String rootVersion = "rootVersion";

        final String rootPomDependencyGroupId = "rootPomDependencyGroupId";
        
        final String rootPomDependencyArtifactId = "rootPomDependencyArtifactId";
        final String rootPomDependencyVersion = "rootPomDependencyVersion";

        final String rootPomDependencyOtherArtifactId = "rootPomDependencyOtherArtifactId";
        final String rootPomDependencyOtherVersion = "rootPomDependencyOtherVersion";

        final String subPomDependencyArtifactId = "subPomDependencyArtifactId";

        final String rootPomString =
                "<project>"

              + "  <groupId>" + rootGroupId + "</groupId>"
              + "  <artifactId>" + rootArtifactId + "</artifactId>"
              + "  <version>" + rootVersion + "</version>"

              + "  <dependencyManagement>"
              + "    <dependencies>"
              + "      <dependency>"
              + "        <groupId>" + rootPomDependencyGroupId + "</groupId>"
              + "        <artifactId>" + rootPomDependencyArtifactId + "</artifactId>"
              + "        <version>" + rootPomDependencyVersion + "</version>"
              + "        <scope>runtime</scope>"
              + "      </dependency>"
              + "      <dependency>"
              + "        <groupId>" + rootPomDependencyGroupId + "</groupId>"
              + "        <artifactId>" + rootPomDependencyOtherArtifactId + "</artifactId>"
              + "        <version>" + rootPomDependencyOtherVersion + "</version>"
              + "        <scope>test</scope>"
              + "      </dependency>"
              + "      <dependency>"
              + "        <groupId>" + rootPomDependencyGroupId + "</groupId>"
              + "        <artifactId>" + subPomDependencyArtifactId + "</artifactId>"
              + "        <scope>compile</scope>"
              + "      </dependency>"
              + "    </dependencies>"
              + "  </dependencyManagement>"
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

              + "  <dependencies>"
              + "    <dependency>"
              + "      <groupId>" + rootPomDependencyGroupId + "</groupId>"
              + "      <artifactId>" + rootPomDependencyArtifactId + "</artifactId>"
              + "    </dependency>"
              + "    <dependency>"
              + "      <groupId>" + rootPomDependencyGroupId + "</groupId>"
              + "      <artifactId>" + rootPomDependencyOtherArtifactId + "</artifactId>"
              + "      <version>1.0</version>"
              + "      <scope>provided</scope>"
              + "    </dependency>"

              + "    <dependency>"
              + "      <groupId>" + rootPomDependencyGroupId + "</groupId>"
              + "      <artifactId>" + subPomDependencyArtifactId + "</artifactId>"
              + "      <version>1.1</version>"
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
        
        final List<DocumentModule<Document>> modules = Arrays.asList(
                new DocumentModule<>(
                        new MavenModuleId(rootGroupId, rootArtifactId, rootVersion),
                        rootPom),
                
                new DocumentModule<>(
                        new MavenModuleId(subGroupId, subArtifactId, subVersion),
                        subPom));
        
        final List<MavenProject> effectiveProjects = EffectivePOMsHelper.computeEffectiveProjects(
                        modules,
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
        
        assertThat(rootDependencies.size()).isEqualTo(1);
        
        assertThat(rootDependencies.get(0).getModuleId().getGroupId())
                .isEqualTo(superPomDependencyGroupId);

        assertThat(rootDependencies.get(0).getModuleId().getArtifactId())
                .isEqualTo(superPomDependencyArtifactId);

        assertThat(rootDependencies.get(0).getModuleId().getVersion())
                .isEqualTo(superPomDependencyVersion);

        final MavenProject subProject = effectiveProjects.get(1);

        assertThat(subProject.getModuleId().getGroupId()).isEqualTo(subGroupId);
        assertThat(subProject.getModuleId().getArtifactId()).isEqualTo(subArtifactId);
        assertThat(subProject.getModuleId().getVersion()).isEqualTo(subVersion);

        final List<MavenDependency> subDependencies = subProject.getCommon().getDependencies();

        assertThat(subDependencies.size()).isEqualTo(4);
        
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

        assertThat(subDependencies.get(1).getScope())
            .isEqualTo("runtime");

        assertThat(subDependencies.get(2).getModuleId().getGroupId())
            .isEqualTo(rootPomDependencyGroupId);

        assertThat(subDependencies.get(2).getModuleId().getArtifactId())
            .isEqualTo(rootPomDependencyOtherArtifactId);

        assertThat(subDependencies.get(2).getModuleId().getVersion())
            .isEqualTo(rootPomDependencyOtherVersion);

        assertThat(subDependencies.get(2).getScope())
            .isEqualTo("test");

        assertThat(subDependencies.get(3).getModuleId().getGroupId())
        .isEqualTo(rootPomDependencyGroupId);

        assertThat(subDependencies.get(3).getModuleId().getArtifactId())
            .isEqualTo(subPomDependencyArtifactId);
    
        assertThat(subDependencies.get(3).getModuleId().getVersion())
            .isEqualTo("1.1");
    
        assertThat(subDependencies.get(3).getScope())
            .isEqualTo("compile");
    }
}
