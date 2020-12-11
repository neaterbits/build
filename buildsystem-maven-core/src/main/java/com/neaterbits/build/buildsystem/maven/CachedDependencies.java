package com.neaterbits.build.buildsystem.maven;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.w3c.dom.Document;

import com.neaterbits.build.buildsystem.common.ScanException;
import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.effective.EffectivePOMReader;
import com.neaterbits.build.buildsystem.maven.effective.DocumentModule;
import com.neaterbits.build.buildsystem.maven.project.model.BaseMavenRepository;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.project.model.xml.MavenXMLProject;
import com.neaterbits.build.buildsystem.maven.project.parse.PomProjectParser;
import com.neaterbits.build.buildsystem.maven.repositoryaccess.MavenRepositoryAccess;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;

public final class CachedDependencies {

    private static class ExternalProject {

        private final DocumentModule<Document> module;
        
        private MavenProject effective;
        
        ExternalProject(MavenModuleId moduleId, MavenXMLProject<Document> xmlProject, MavenProject effective) {
            this.module = new DocumentModule<>(moduleId, xmlProject);
            this.effective = effective;
        }
        
        MavenXMLProject<Document> getXMLProject() {
            return module.getXMLProject();
        }

        @Override
        public String toString() {
            return "ExternalProject effective=" + (effective != null ? effective.getModuleId() : "null") + "]";
        }
    }

    private final XMLReaderFactory<Document> xmlReaderFactory;
    private final PomProjectParser<Document> pomProjectParser;
    private final MavenRepositoryAccess repositoryAccess;
    private final EffectivePOMReader effectivePomReader;
    
    private final boolean debug;
    
    private final Map<MavenModuleId, ExternalProject> cachedDependencies;

    CachedDependencies(
            XMLReaderFactory<Document> xmlReaderFactory,
            PomProjectParser<Document> pomProjectParser,
            MavenRepositoryAccess repositoryAccess,
            EffectivePOMReader effectivePomReader,
            boolean debug) {
        
        Objects.requireNonNull(xmlReaderFactory);
        Objects.requireNonNull(pomProjectParser);
        Objects.requireNonNull(repositoryAccess);
        Objects.requireNonNull(effectivePomReader);

        this.xmlReaderFactory = xmlReaderFactory;
        this.pomProjectParser = pomProjectParser;
        this.repositoryAccess = repositoryAccess;
        this.effectivePomReader = effectivePomReader;
        
        this.debug = debug;
        
        this.cachedDependencies = new HashMap<>();
    }
    
    public void downloadPluginIfNotPresentAndAddToModel(
            List<BaseMavenRepository> referencedFromRepositories,
            MavenPlugin plugin) throws IOException, ScanException {
        
        downloadExternalDependencyIfNotPresentAndAddToModel(referencedFromRepositories, plugin.getModuleId(), null);
    }

    public void downloadExternalDependencyIfNotPresentAndAddToModel(
            List<BaseMavenRepository> referencedFromRepositories,
            MavenModuleId moduleId,
            String classifier) throws IOException, ScanException {

        Objects.requireNonNull(moduleId);
        
        if (debug) {
            System.out.println("## ENTER downloadExternalDependencyIfNotPresentAndAddToModel " + moduleId);
        }
        
        if (!cachedDependencies.containsKey(moduleId)) {
            
            if (debug) {
                System.out.println("## !containsKey " + moduleId);
            }

            if (!repositoryAccess.isModulePomPresent(moduleId)) {
                
                if (debug) {
                    System.out.println("## download if not present " + moduleId + " " + referencedFromRepositories);
                }
                
                repositoryAccess.downloadModulePomIfNotPresent(moduleId, referencedFromRepositories);
            }
            
            MavenXMLProject<Document> xmlProject = null;
            
            final File file = repositoryAccess.repositoryExternalPomFile(moduleId);
            
            if (file == null) {
                throw new IllegalStateException("No file for " + moduleId);
            }
            
            try {
                xmlProject = pomProjectParser.parse(
                        file,
                        xmlReaderFactory);
            } catch (XMLReaderException ex) {
                System.err.println("Warn: Could not parse project " + moduleId.getId() + " " + ex);
            }
            
            if (xmlProject != null) {
                cachedDependencies.put(moduleId, new ExternalProject(moduleId, xmlProject, null));
                
                final String packaging;
                
                if (xmlProject.getProject().getPackaging() == null) {
                    packaging = "jar";
                }
                else {
                    packaging = xmlProject.getProject().getPackaging();
                }
                
                if (debug) {
                    System.out.println("## packaging for " + moduleId.getId() + " " + packaging);
                }
                
                final String fileSuffix;
    
                switch (packaging) {
                case "pom":
                    // Only pom file
                    fileSuffix = null;
                    break;
                    
                default:
                    fileSuffix = "jar";
                    break;
                }
    
                if (fileSuffix != null && !repositoryAccess.isModuleFilePresent(moduleId, classifier, fileSuffix)) {
                    try {
                        repositoryAccess.downloadModuleFileIfNotPresent(moduleId, classifier, fileSuffix, referencedFromRepositories);
                    }
                    catch (IOException ex) {
                        System.err.println("Warn: failed to download " + fileSuffix + " file of " + moduleId);
                    }
                }
            }
        }

        computeEffectiveWherePossible();

        if (debug) {
            System.out.println("## EXIT downloadExternalDependencyIfNotPresentAndAddToModel " + moduleId);
        }
    }
    
    public MavenProject getExternalProject(MavenModuleId moduleId) {
        
        Objects.requireNonNull(moduleId);

        final ExternalProject externalProject = cachedDependencies.get(moduleId);
        
        return externalProject != null
                ? externalProject.getXMLProject().getProject()
                : null;
    }

    public EffectiveProject getEffectiveExternalProject(MavenModuleId moduleId) {

        Objects.requireNonNull(moduleId);

        final ExternalProject externalProject = cachedDependencies.get(moduleId);
        
        return externalProject != null
                ? new EffectiveProject(externalProject.effective)
                : null;
    }

    private void computeEffectiveWherePossible() {
        
        if (debug) {
            System.out.println("## ENTER computeEffectiveWherePossible()");
        }

        for (Map.Entry<MavenModuleId, ExternalProject> externalProjectEntry : cachedDependencies.entrySet()) {

            final ExternalProject externalProject = externalProjectEntry.getValue();
            
            if (externalProject.effective == null && hasAllParentPoms(cachedDependencies, externalProject)) {
                
                final MavenProject effectiveProject = effectivePomReader.computeEffectiveProject(
                        externalProject.module,
                        moduleId -> {
                            
                            Objects.requireNonNull(moduleId);
                            
                            if (debug) {
                                System.out.println("## compute effective for " + moduleId);
                            }
                            
                            final ExternalProject ext = cachedDependencies.get(moduleId);
                        
                            if (ext == null) {
                                throw new IllegalStateException("No external project for " + moduleId);
                            }
                            
                            return ext.module;
                        });

                if (effectiveProject == null) {
                    throw new IllegalStateException();
                }

                externalProject.effective = effectiveProject;
            }
        }
        if (debug) {
            System.out.println("## EXIT computeEffectiveWherePossible()");
        }
    }
    
    private static boolean hasAllParentPoms(
            Map<MavenModuleId, ExternalProject> cachedDependencies,
            ExternalProject externalProject) {
        
        boolean hasAll = true;
        
        for (MavenModuleId parentModuleId = externalProject.getXMLProject().getProject().getParentModuleId(); parentModuleId != null;) {

            final ExternalProject parentExternal = cachedDependencies.get(parentModuleId);
            
            if (parentExternal == null) {
                hasAll = false;
                break;
            }
            else {
                parentModuleId = parentExternal.getXMLProject().getProject().getParentModuleId();
            }
        }
        
        return hasAll;
    }
}
