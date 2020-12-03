package com.neaterbits.build.buildsystem.maven;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.effective.EffectivePOMsHelper;
import com.neaterbits.build.buildsystem.maven.project.model.MavenRepository;
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

        final BuildState buildState = prepareBuild();

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
            
            final MavenDependency rootDep = createDependency(rootDepId);
            final MavenDependency rootDepParent = createDependency(rootDepParentId);
            final MavenDependency subDep = createDependency(subDepId);
            
            Mockito.verify(buildState.repositoryAccess).isModulePresent(rootDepId);

            Mockito.verify(buildState.repositoryAccess).downloadModuleIfNotPresent(rootDepId, REPOSITORIES);

            Mockito.verify(buildState.repositoryAccess).repositoryExternalPomFile(rootDep);

            Mockito.verify(buildState.repositoryAccess).isModulePresent(subDepId);

            Mockito.verify(buildState.repositoryAccess).downloadModuleIfNotPresent(subDepId, REPOSITORIES);

            Mockito.verify(buildState.repositoryAccess).repositoryExternalPomFile(subDep);

            Mockito.verify(buildState.repositoryAccess).isModulePresent(rootDepParentId);

            Mockito.verify(buildState.repositoryAccess).downloadModuleIfNotPresent(rootDepParentId, REPOSITORIES);

            Mockito.verify(buildState.repositoryAccess).repositoryExternalPomFile(rootDepParent);
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
        
        Mockito.when(repositoryAccess.isModulePresent(rootDepId)).thenReturn(false);
        Mockito.when(repositoryAccess.isModulePresent(rootDepParentId)).thenReturn(false);
        Mockito.when(repositoryAccess.isModulePresent(subDepId)).thenReturn(false);
        
        final MavenDependency rootDep = createDependency(rootDepId);
        final MavenDependency rootDepParent = createDependency(rootDepParentId);
        final MavenDependency subDep = createDependency(subDepId);
        
        Mockito.when(repositoryAccess.repositoryExternalPomFile(rootDep)).thenReturn(rootDepFile);
        Mockito.when(repositoryAccess.repositoryExternalPomFile(rootDepParent)).thenReturn(rootDepParentFile);
        Mockito.when(repositoryAccess.repositoryExternalPomFile(subDep)).thenReturn(subDepFile);
    }
    
    private static MavenDependency createDependency(MavenModuleId moduleId) {
        
        return new MavenDependency(
                moduleId,
                null,
                null,
                null,
                null,
                null);
    }
}
