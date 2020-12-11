package com.neaterbits.build.buildsystem.maven.repositoryaccess;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.project.model.BaseMavenRepository;
import com.neaterbits.util.StringUtils;

public interface MavenRepositoryAccess {

    @FunctionalInterface    
    interface PathAdder<T> {
        
        T add(T path, String toAdd);
    }
    
    public static Path mavenRepositoryDirectory(MavenModuleId moduleId) {

        return mavenRepositoryDirectory(Path.of(System.getProperty("user.home")), moduleId);
    }

    public static Path mavenRepositoryDirectory(Path baseDir, MavenModuleId moduleId) {
        
        final Path mavenRepositoryDir = baseDir.resolve(".m2").resolve("repository");
                
        return repositoryDirectory(mavenRepositoryDir, moduleId);
    }

    public static Path repositoryDirectory(Path mavenRepositoryDir, MavenModuleId moduleId) {
        
        return repositoryDirectoryPart(mavenRepositoryDir, moduleId, Path::resolve);
    }

    public static String repositoryDirectory(StringBuilder mavenRepositoryDir, MavenModuleId moduleId) {
        
        return repositoryDirectoryPart(
                mavenRepositoryDir,
                moduleId,
                (sb, path) -> {
                    if (sb.length() > 0) {
                        sb.append('/');
                    }
                    
                    sb.append(path);
                   
                    return sb;
                }).toString();
    }

    public static String repositoryDirectoryPart(MavenModuleId moduleId) {
        
        return repositoryDirectory(new StringBuilder(), moduleId);
    }

    public static <T> T repositoryDirectoryPart(T mavenRepositoryDir, MavenModuleId moduleId, PathAdder<T> adder) {
        
        final String [] groupIdParts = StringUtils.split(moduleId.getGroupId(), '.');
        
        T path = mavenRepositoryDir;
        
        for (String part : groupIdParts) {
            path = adder.add(path, part);
        }
        
        path = adder.add(path, moduleId.getArtifactId());
        path = adder.add(path, moduleId.getVersion());

        return path;
    }

    void downloadModulePomIfNotPresent(
            MavenModuleId mavenModule,
            List<? extends BaseMavenRepository> repositories) throws IOException;

    boolean isModulePomPresent(MavenModuleId moduleId);

    void downloadModuleFileIfNotPresent(
            MavenModuleId mavenModule,
            String classifier,
            String fileSuffix,
            List<? extends BaseMavenRepository> repositories) throws IOException;

    boolean isModuleFilePresent(MavenModuleId moduleId, String classifier, String fileSuffix);

    File repositoryJarFile(MavenModuleId mavenModuleId, String classifier);

    File repositoryExternalPomFile(MavenModuleId moduleId);
}
