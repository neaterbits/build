package com.neaterbits.build.buildsystem.maven;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.common.http.HTTPDownloader;
import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.project.model.BaseMavenRepository;
import com.neaterbits.build.buildsystem.maven.repositoryaccess.MavenRepositoryAccess;
import com.neaterbits.util.IOUtils;

final class URLMavenRepositoryAccess implements MavenRepositoryAccess {

    private final Path repository;
    private final HTTPDownloader downloader;
    
    URLMavenRepositoryAccess(File repository, HTTPDownloader downloader) {
    
        Objects.requireNonNull(repository);
        Objects.requireNonNull(downloader);
        
        this.repository = repository.toPath();
        this.downloader = downloader;
    }

    private Path repositoryDirectory(MavenDependency mavenDependency) {

        final MavenModuleId moduleId = mavenDependency.getModuleId();
    
        return repositoryDirectory(moduleId);
    }

    private Path repositoryDirectory(MavenModuleId moduleId) {
        
        return MavenRepositoryAccess.repositoryDirectory(repository, moduleId);
    }

    private Path repositoryPomFile(MavenDependency mavenDependency) {

        return repositoryDirectory(mavenDependency).resolve("pom.xml");
    }

    private static String getPomFileName(MavenModuleId moduleId) {

        return moduleId.getArtifactId() + '-' + moduleId.getVersion() + '.' + "pom";
    }
    
    private File repositoryExternalPomFile(Path repositoryDirectory, MavenModuleId moduleId) {

        return repositoryDirectory.resolve(getPomFileName(moduleId)).toFile();
    }

    private File repositoryExternalPomFile(MavenModuleId moduleId) {
        
        final Path repositoryDirectory = repositoryDirectory(moduleId);
    
        return repositoryExternalPomFile(repositoryDirectory, moduleId);
    }

    private File repositoryExternalPomSha1File(MavenModuleId moduleId) {
        
        return repositoryDirectory(moduleId).resolve(getPomFileName(moduleId) + ".sha1").toFile();
    }

    @Override
    public File repositoryExternalPomFile(MavenDependency mavenDependency) {

        final Path repositoryDirectory = repositoryDirectory(mavenDependency);
        
        return repositoryExternalPomFile(repositoryDirectory, mavenDependency.getModuleId());
    }

    
    private static File getFile(Path path) {

        try {
            return path.toFile().getCanonicalFile();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public File repositoryJarFile(MavenDependency mavenDependency) {

        final Path path = repositoryDirectory(mavenDependency).resolve(MavenBuildRoot.getCompiledFileName(mavenDependency));

        return getFile(path);
    }

    private static String getJarFileName(MavenModuleId moduleId) {

        return MavenBuildRoot.getCompiledFileName(moduleId, null);
    }
    
    @Override
    public File repositoryJarFile(MavenModuleId moduleId) {

        final Path path = repositoryDirectory(moduleId).resolve(getJarFileName(moduleId));

        return getFile(path);
    }
    
    private File repositoryJarSha1File(MavenModuleId moduleId) {

        final Path path = repositoryDirectory(moduleId).resolve(getJarFileName(moduleId) + ".sha1");

        return getFile(path);
    }

    private static boolean isPresent(File file) {
        
        return file.exists();
    }
    
    @Override
    public boolean isModulePresent(MavenModuleId moduleId) {

        // Must verify that all jar are present, sha1 files only necessary for remote repositories
        return    isPresent(repositoryJarFile(moduleId))
               && isPresent(repositoryExternalPomFile(moduleId));
    }

    private boolean downloadFileIfNotPresent(MavenModuleId moduleId, File file, URL repository) {

        final URL url;

        boolean ok = false;
        
        final String repositoryFilePath = MavenRepositoryAccess.repositoryDirectoryPart(moduleId) + '/' + file.getName();

        try {
            url = new URL(repository, repository.getFile() + '/' + repositoryFilePath);
        } catch (MalformedURLException ex) {
            throw new IllegalStateException(ex);
        }

        try (InputStream stream = downloader.download(url)) {

            file.getParentFile().mkdirs();
            
            IOUtils.write(file, stream);

            ok = true;
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return ok;
    }

    private void downloadModuleFromURLsIfNotPresent(MavenModuleId moduleId, List<URL> repositories) throws IOException {

        boolean downloadedOk = false;
        
        for (URL repository : repositories) {
            
            if (    downloadFileIfNotPresent(moduleId, repositoryJarFile(moduleId), repository)
                 && downloadFileIfNotPresent(moduleId, repositoryJarSha1File(moduleId), repository)    
                 && downloadFileIfNotPresent(moduleId, repositoryExternalPomFile(moduleId), repository)
                 && downloadFileIfNotPresent(moduleId, repositoryExternalPomSha1File(moduleId), repository)) {
                
                // TODO validate sha1
                downloadedOk = true;
                break;
            }
        }

        if (!downloadedOk) {
            throw new IOException("Failed to download module " + moduleId);
        }
    }

    @Override
    public void downloadModuleIfNotPresent(MavenModuleId mavenModule, List<? extends BaseMavenRepository> repositories) throws IOException {

        final List<URL> urls = new ArrayList<>(repositories.size());
        
        for (BaseMavenRepository repository : repositories) {
            urls.add(new URL(repository.getUrl()));
        }
        
        downloadModuleFromURLsIfNotPresent(mavenModule, urls);
    }
}
