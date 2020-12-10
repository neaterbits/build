package com.neaterbits.build.buildsystem.maven;

import java.io.File;
import java.io.IOException;

import org.mockito.Mockito;
import org.w3c.dom.Document;

import com.neaterbits.build.buildsystem.common.ArgumentException;
import com.neaterbits.build.buildsystem.common.BuildSystemMain;
import com.neaterbits.build.buildsystem.common.ScanException;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginsEnvironment;
import com.neaterbits.build.buildsystem.maven.plugins.access.MavenPluginsAccess;
import com.neaterbits.build.buildsystem.maven.plugins.execute.MavenPluginsEnvironmentImpl;
import com.neaterbits.build.buildsystem.maven.plugins.instantiate.MavenPluginInstantiator;
import com.neaterbits.build.buildsystem.maven.project.parse.PomProjectParser;
import com.neaterbits.build.buildsystem.maven.repositoryaccess.MavenRepositoryAccess;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.util.Files;
import com.neaterbits.util.IOUtils;
import com.neaterbits.util.concurrency.dependencyresolution.executor.logger.PrintlnTargetExecutorLogger;
import com.neaterbits.util.concurrency.dependencyresolution.executor.logger.TargetExecutorLogState;
import com.neaterbits.util.concurrency.dependencyresolution.executor.logger.TargetExecutorLogger;
import com.neaterbits.util.concurrency.dependencyresolution.model.TargetDefinition;

public abstract class BaseMavenBuildTest {

    static class BuildState {

        final File tempDirectory;
        final MavenRepositoryAccess repositoryAccess;
        final PomProjectParser<Document> pomProjectParser;
        private final boolean pomProjectParserIsMock;
        final MavenPluginsAccess pluginsAccess;
        final MavenPluginInstantiator pluginInstantiator;
        final MavenPluginsEnvironment pluginsEnvironment;

        BuildState(
                File tempDirectory,
                MavenRepositoryAccess repositoryAccess,
                PomProjectParser<Document> pomProjectParser,
                boolean pomProjectParserIsMock,
                MavenPluginsAccess pluginsAccess,
                MavenPluginInstantiator pluginInstantiator,
                MavenPluginsEnvironment pluginsEnvironment) {

            this.tempDirectory = tempDirectory;
            this.repositoryAccess = repositoryAccess;
            this.pomProjectParser = pomProjectParser;
            this.pomProjectParserIsMock = pomProjectParserIsMock;
            this.pluginsAccess = pluginsAccess;
            this.pluginInstantiator = pluginInstantiator;
            this.pluginsEnvironment = pluginsEnvironment;
        }
    }

    final BuildState prepareBuild() {

        @SuppressWarnings("unchecked")
        final PomProjectParser<Document> pomProjectParser = Mockito.mock(PomProjectParser.class);

        return prepareBuild(pomProjectParser, true);
    }
    
    final BuildState prepareBuild(PomProjectParser<Document> pomProjectParser) {
        
        return prepareBuild(pomProjectParser, false);
    }

    private BuildState prepareBuild(PomProjectParser<Document> pomProjectParser, boolean pomProjectParserIsMock) {
        
        final File tempDirectory;
        try {
            tempDirectory = java.nio.file.Files.createTempDirectory("mavenbuildtest").toFile();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }

        final MavenRepositoryAccess repositoryAccess = Mockito.mock(MavenRepositoryAccess.class);

        final MavenPluginsAccess pluginsAccess = Mockito.mock(MavenPluginsAccess.class);
        
        final MavenPluginInstantiator pluginInstantiator = Mockito.mock(MavenPluginInstantiator.class);
        
        final MavenPluginsEnvironment pluginsEnvironment = new MavenPluginsEnvironmentImpl(pluginInstantiator);
    
        return new BuildState(
                tempDirectory,
                repositoryAccess,
                pomProjectParser,
                pomProjectParserIsMock,
                pluginsAccess,
                pluginInstantiator,
                pluginsEnvironment);
    }
    
    final void build(String phase, BuildState buildState) {
        
        build(phase, buildState, null, null);
    }

    final void build(
            String phase,
            BuildState buildState,
            String rootExtra,
            String subExtra) {
        
        try {
            writePoms(buildState.tempDirectory, rootExtra, subExtra);
        } catch (IOException | XMLReaderException ex) {
            throw new IllegalStateException(ex);
        }

        final MavenBuildSystem buildSystem = new MavenBuildSystem(
                                                    buildState.pluginsAccess,
                                                    buildState.pluginsEnvironment,
                                                    buildState.repositoryAccess,
                                                    buildState.pomProjectParser);
        
        final TargetExecutorLogger targetExecutorLogger = new PrintlnTargetExecutorLogger() {

            @Override
            public void onActionException(
                    TargetDefinition<?> target,
                    TargetExecutorLogState logState,
                    Exception exception) {
                
                super.onActionException(target, logState, exception);
                
                throw new IllegalStateException("Exeception while processing target " + target, exception);
            }
        };

        try {
            BuildSystemMain.build(
                    buildSystem,
                    buildState.tempDirectory,
                    new String [] { phase },
                    logContext -> targetExecutorLogger,
                    null);
        } catch (ScanException | ArgumentException ex) {
            throw new IllegalStateException(ex);
        }
    }

    final void cleanup(BuildState buildState) {

        Mockito.verifyNoMoreInteractions(
                buildState.pluginsAccess,
                buildState.repositoryAccess,
                buildState.pluginInstantiator);
    
        if (buildState.pomProjectParserIsMock) {
            Mockito.verifyNoMoreInteractions(buildState.pomProjectParser);
        }

        Files.deleteRecursively(buildState.tempDirectory);
    }
    
    private final File [] writePoms(File directory, String rootExtra, String subExtra) throws IOException, XMLReaderException {
        
        final String rootGroupId = "rootGroupId";
        final String rootArtifactId = "rootArtifactId";
        final String rootVersion = "rootVersion";

        final String rootPomString =
                "<project>"

              + "  <groupId>" + rootGroupId + "</groupId>"
              + "  <artifactId>" + rootArtifactId + "</artifactId>"
              + "  <version>" + rootVersion + "</version>"
              
              + "  <modules>"
              + "    <module>subdir</module>"
              + "  </modules>"

              + "  <properties>"
              + "    <rootProperty>rootValue</rootProperty>"
              + "    <overrideProperty>overridable</overrideProperty>"
              + "  </properties>"
              
              + (rootExtra != null ? rootExtra : "")
              
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
              
              + (subExtra != null ? subExtra : "")
              
              + "</project>";

        final File rootFile = new File(directory, "pom.xml");
        
        final File subDir = new File(directory, "subdir");
        subDir.mkdir();
        
        final File subFile = new File(subDir, "pom.xml");

        rootFile.deleteOnExit();
        subFile.deleteOnExit();
    
        IOUtils.write(rootFile, rootPomString);
        IOUtils.write(subFile, subPomString);
        
        return new File [] { rootFile, subFile };
    }
}
