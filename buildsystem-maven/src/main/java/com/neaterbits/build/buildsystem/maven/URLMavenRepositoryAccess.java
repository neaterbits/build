package com.neaterbits.build.buildsystem.maven;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.common.http.HTTPDownloader;
import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.project.model.BaseMavenRepository;
import com.neaterbits.build.buildsystem.maven.repositoryaccess.MavenRepositoryAccess;
import com.neaterbits.util.IOUtils;

final class URLMavenRepositoryAccess implements MavenRepositoryAccess {

    private final Path repository;
    private final HTTPDownloader downloader;
    
    URLMavenRepositoryAccess(Path repository, HTTPDownloader downloader) {
    
        Objects.requireNonNull(repository);
        Objects.requireNonNull(downloader);
        
        this.repository = repository;
        this.downloader = downloader;
    }

    private Path repositoryDirectory(MavenModuleId moduleId) {
        
        return MavenRepositoryAccess.repositoryDirectory(repository, moduleId);
    }

    private static String getPomFileName(MavenModuleId moduleId) {

        return moduleId.getArtifactId() + '-' + moduleId.getVersion() + '.' + "pom";
    }
    
    private File repositoryExternalPomFile(Path repositoryDirectory, MavenModuleId moduleId) {

        return repositoryDirectory.resolve(getPomFileName(moduleId)).toFile();
    }

    @Override
    public File repositoryExternalPomFile(MavenModuleId moduleId) {
        
        final Path repositoryDirectory = repositoryDirectory(moduleId);
    
        return repositoryExternalPomFile(repositoryDirectory, moduleId);
    }

    private File repositoryExternalPomSha1File(MavenModuleId moduleId) {
        
        return repositoryDirectory(moduleId).resolve(getPomFileName(moduleId) + ".sha1").toFile();
    }

    private static File getFile(Path path) {

        try {
            return path.toFile().getCanonicalFile();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    static String getFileName(MavenModuleId moduleId, String classifier, String packaging) {
        return MavenBuildRoot.getCompiledFileName(moduleId, classifier, packaging);
    }

    private static String getSha1FileName(MavenModuleId moduleId, String classifier, String packaging) {
        return getFileName(moduleId, classifier, packaging) + ".sha1";
    }

    private File repositoryFile(MavenModuleId moduleId, String classifier, String packaging) {

        final Path path = repositoryDirectory(moduleId).resolve(getFileName(moduleId, classifier, packaging));

        return getFile(path);
    }

    private File repositorySha1File(MavenModuleId moduleId, String classifier, String packaging) {

        final Path path = repositoryDirectory(moduleId).resolve(getSha1FileName(moduleId, classifier, packaging));

        return getFile(path);
    }

    @Override
    public File repositoryJarFile(MavenModuleId moduleId, String classifier) {
        
        return repositoryFile(moduleId, classifier, "jar");
    }

    private static boolean isPresent(File file) {
        
        return file.exists();
    }
    
    @Override
    public boolean isModulePomPresent(MavenModuleId moduleId) {
        // Must verify that all jar are present, sha1 files only necessary for remote repositories
        return isPresent(repositoryExternalPomFile(moduleId));
    }

    @Override
    public boolean isModuleFilePresent(MavenModuleId moduleId, String classifier, String fileSuffix) {
        return isPresent(repositoryFile(moduleId, classifier, fileSuffix));
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
        }
        
        return ok;
    }

    private BaseMavenRepository downloadModuleFileIfNotPresent(
                                                MavenModuleId moduleId,
                                                File file,
                                                File sha1File,
                                                List<? extends BaseMavenRepository> repositories) throws IOException {

        BaseMavenRepository downloadedOk = null;
        
        for (BaseMavenRepository repository : repositories) {
            
            final URL url = new URL(repository.getUrl());
            
            if (    downloadFileIfNotPresent(moduleId, file, url)
                 && downloadFileIfNotPresent(moduleId, sha1File, url)) {
                
                // TODO validate sha1
                downloadedOk = repository;
                break;
            }
        }

        if (downloadedOk == null) {
            throw new IOException("Failed to download module " + moduleId);
        }
        
        return downloadedOk;
    }

    @Override
    public void downloadModulePomIfNotPresent(
                                            MavenModuleId mavenModule,
                                            List<? extends BaseMavenRepository> repositories) throws IOException {
        downloadModuleFileIfNotPresent(
                mavenModule,
                repositoryExternalPomFile(mavenModule),
                repositoryExternalPomSha1File(mavenModule),
                repositories);
    }

    @Override
    public void downloadModuleFileIfNotPresent(
            MavenModuleId mavenModule,
            String classifier,
            String fileSuffix,
            List<? extends BaseMavenRepository> repositories)
                    throws IOException {
        
        downloadModuleFileIfNotPresent(
                mavenModule,
                repositoryFile(mavenModule, classifier, fileSuffix),
                repositorySha1File(mavenModule, classifier, fileSuffix),
                repositories);
    }
}
