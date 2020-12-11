package com.neaterbits.build.buildsystem.maven;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.eq;

import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.effective.EffectivePOMsHelper;
import com.neaterbits.build.buildsystem.maven.project.model.MavenRepository;
import com.neaterbits.build.buildsystem.maven.project.parse.PomTreeParser;
import com.neaterbits.build.buildsystem.maven.repositoryaccess.MavenRepositoryAccess;
import com.neaterbits.util.IOUtils;

public class MavenValidateTest extends BaseMavenBuildTest {

    private static final List<MavenRepository> REPOSITORIES;
    
    static {
        final List<MavenRepository> list = new ArrayList<>();
        
        list.add(EffectivePOMsHelper.CENTRAL_REPOSITORY);
        
        REPOSITORIES = Collections.unmodifiableList(list);
    }

    @Test
    public void testValidatePhase() throws IOException {

        final BuildState buildState = prepareBuild(PomTreeParser::readModule);

        final String rootDepGroupId = "rootDepGroupId";
        final String rootDepArtifactId = "rootDepArtifactId";
        final String rootDepVersion = "rootDepVersion";
        
        final MavenModuleId rootDepId = new MavenModuleId(
                                                rootDepGroupId,
                                                rootDepArtifactId,
                                                rootDepVersion);

        final String rootExtra =

                  "  <dependencies>"
                + "     <dependency>"
                + "        <groupId>" + rootDepGroupId + "</groupId>"
                + "        <artifactId>" + rootDepArtifactId + "</artifactId>"
                + "        <version>" + rootDepVersion + "</version>"
                + "     </dependency>"
                + "  </dependencies>";
        
        final File rootDepFile = File.createTempFile("rootdep", "pom");

        final String rootDepParentGroupId = "rootDepParentGroupId";
        final String rootDepParentArtifactId = "rootDepParentArtifactId";
        final String rootDepParentVersion = "rootDepParentVersion";

        final String rootDepString =
                "<project>"

              + "  <groupId>" + rootDepGroupId + "</groupId>"
              + "  <artifactId>" + rootDepArtifactId + "</artifactId>"
              + "  <version>" + rootDepVersion + "</version>"

              + "  <parent>"
              + "    <groupId>" + rootDepParentGroupId + "</groupId>"
              + "    <artifactId>" + rootDepParentArtifactId + "</artifactId>"
              + "    <version>" + rootDepParentVersion + "</version>"
              + "  </parent>"

              + "</project>";

        final MavenModuleId rootDepParentId = new MavenModuleId(
                rootDepParentGroupId,
                rootDepParentArtifactId,
                rootDepParentVersion);

        final File rootDepParentFile = File.createTempFile("rootdepparent", "pom");
        
        final String rootDepParentString =
                "<project>"

              + "  <groupId>" + rootDepParentGroupId + "</groupId>"
              + "  <artifactId>" + rootDepParentArtifactId + "</artifactId>"
              + "  <version>" + rootDepParentVersion + "</version>"

              + "</project>";

        final String subDepGroupId = "subDepGroupId";
        final String subDepArtifactId = "subDepArtifactId";
        final String subDepVersion = "subDepVersion";

        final MavenModuleId subDepId = new MavenModuleId(
                                                subDepGroupId,
                                                subDepArtifactId,
                                                subDepVersion);

        final String subExtra =
                
                  "  <dependencies>"
                + "     <dependency>"
                + "        <groupId>" + subDepGroupId + "</groupId>"
                + "        <artifactId>" + subDepArtifactId + "</artifactId>"
                + "        <version>" + subDepVersion + "</version>"
                + "     </dependency>"
                + "  </dependencies>";
        
        final File subDepFile = File.createTempFile("rootdep", "pom");

        final String subDepString =
                "<project>"

              + "  <groupId>" + subDepGroupId + "</groupId>"
              + "  <artifactId>" + subDepArtifactId + "</artifactId>"
              + "  <version>" + subDepVersion + "</version>"

              + "</project>";

        try {
            
            IOUtils.write(rootDepFile, rootDepString);
            IOUtils.write(rootDepParentFile, rootDepParentString);
            IOUtils.write(subDepFile, subDepString);
            
            prepareBuild(buildState.repositoryAccess, rootDepId, rootDepFile, rootDepParentId, rootDepParentFile, subDepId, subDepFile);
            
            build("validate", buildState, rootExtra, subExtra);
            
            Mockito.verify(buildState.repositoryAccess).isModulePomPresent(rootDepId);
            Mockito.verify(buildState.repositoryAccess).downloadModulePomIfNotPresent(rootDepId, REPOSITORIES);
            Mockito.verify(buildState.repositoryAccess).repositoryExternalPomFile(rootDepId);

            Mockito.verify(buildState.repositoryAccess).isModuleFilePresent(rootDepId, null, "jar");
            Mockito.verify(buildState.repositoryAccess).downloadModuleFileIfNotPresent(rootDepId, null, "jar", REPOSITORIES);

            Mockito.verify(buildState.repositoryAccess).isModulePomPresent(subDepId);
            Mockito.verify(buildState.repositoryAccess).downloadModulePomIfNotPresent(subDepId, REPOSITORIES);
            Mockito.verify(buildState.repositoryAccess).repositoryExternalPomFile(subDepId);

            Mockito.verify(buildState.repositoryAccess).isModuleFilePresent(subDepId, null, "jar");
            Mockito.verify(buildState.repositoryAccess).downloadModuleFileIfNotPresent(subDepId, null, "jar", REPOSITORIES);

            Mockito.verify(buildState.repositoryAccess).isModulePomPresent(rootDepParentId);
            Mockito.verify(buildState.repositoryAccess).downloadModulePomIfNotPresent(rootDepParentId, REPOSITORIES);
            Mockito.verify(buildState.repositoryAccess).repositoryExternalPomFile(rootDepParentId);

            Mockito.verify(buildState.repositoryAccess).isModuleFilePresent(rootDepParentId, null, "jar");
            Mockito.verify(buildState.repositoryAccess).downloadModuleFileIfNotPresent(rootDepParentId, null, "jar", REPOSITORIES);
        }
        finally {
            
            rootDepFile.delete();
            subDepFile.delete();

            cleanup(buildState);
        }
    }

    private void prepareBuild(
            MavenRepositoryAccess repositoryAccess,
            MavenModuleId rootDepId,
            File rootDepFile,
            MavenModuleId rootDepParentId,
            File rootDepParentFile,
            MavenModuleId subDepId,
            File subDepFile) {
        
        Objects.requireNonNull(rootDepFile);
        Objects.requireNonNull(rootDepParentFile);
        Objects.requireNonNull(subDepFile);
        
        Mockito.when(repositoryAccess.isModulePomPresent(rootDepId)).thenReturn(false);
        Mockito.when(repositoryAccess.isModulePomPresent(rootDepParentId)).thenReturn(false);
        Mockito.when(repositoryAccess.isModulePomPresent(subDepId)).thenReturn(false);

        Mockito.when(repositoryAccess.repositoryExternalPomFile(eq(rootDepId))).thenReturn(rootDepFile);
        Mockito.when(repositoryAccess.repositoryExternalPomFile(eq(rootDepParentId))).thenReturn(rootDepParentFile);
        Mockito.when(repositoryAccess.repositoryExternalPomFile(eq(subDepId))).thenReturn(subDepFile);

        Mockito.when(repositoryAccess.isModuleFilePresent(rootDepId, null, "jar")).thenReturn(false);
        Mockito.when(repositoryAccess.isModuleFilePresent(rootDepParentId, null, "jar")).thenReturn(false);
        Mockito.when(repositoryAccess.isModuleFilePresent(subDepId, null, "jar")).thenReturn(false);
    }
}
