package com.neaterbits.build.buildsystem.maven.effective;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.w3c.dom.Document;

import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;
import com.neaterbits.build.buildsystem.maven.xml.dom.DOMModel;
import com.neaterbits.build.buildsystem.maven.xml.dom.DOMReaderFactory;

public final class EffectivePOMReader {

    private final XMLReaderFactory<Document> xmlReaderFactory;
    private final MavenResolveContext resolveContext;
    
    public static MavenModuleId getProjectModuleId(MavenProject project) {

        final String groupId;
        
        if (project.getModuleId().getGroupId() != null) {
            groupId = project.getModuleId().getGroupId();
        }
        else {
            groupId = project.getParentModuleId().getGroupId();
        }
        
        final String version;
        
        if (project.getModuleId().getVersion() != null) {
            version = project.getModuleId().getVersion();
        }
        else {
            version = project.getParentModuleId().getVersion();
        }
        
        final MavenModuleId moduleId = new MavenModuleId(
                        groupId,
                        project.getModuleId().getArtifactId(),
                        version);

        return moduleId;
    }

    public EffectivePOMReader() {
        this.xmlReaderFactory = new DOMReaderFactory();
        this.resolveContext = MavenResolveContext.now();
    }
    
    public XMLReaderFactory<Document> getXMLReaderFactory() {
        return xmlReaderFactory;
    }

    public List<MavenProject> computeEffectiveProjects(List<DocumentModule<Document>> modules) {
        
        return EffectivePOMsHelper.computeEffectiveProjects(
                modules,
                DOMModel.INSTANCE,
                xmlReaderFactory,
                null,
                resolveContext);
    }

    public MavenProject computeEffectiveProject(
                                    DocumentModule<Document> module,
                                    Function<MavenModuleId, DocumentModule<Document>> getModule) {
        
        final MavenModuleId projectModuleId = module.getModuleId();
        
        final List<DocumentModule<Document>> modules = new ArrayList<>();
        
        final Set<MavenModuleId> distinctModuleIds = new HashSet<>();
        
        for (DocumentModule<Document> toCompute = module; toCompute != null;) {
            
            final MavenModuleId moduleId = toCompute.getModuleId();
            
            if (distinctModuleIds.contains(moduleId)) {
                throw new IllegalStateException("Non distinct moduleId " + moduleId);
            }
            
            distinctModuleIds.add(moduleId);
            
            modules.add(toCompute);
            
            final MavenModuleId parentModuleId = toCompute.getXMLProject().getProject().getParentModuleId();
            
            toCompute = parentModuleId != null
                            ? getModule.apply(parentModuleId)
                            : null;
        }

        final List<MavenProject> computedProjects = computeEffectiveProjects(modules);
    
        return computedProjects.stream()
                .filter(p -> p.getModuleId().getArtifactId().equals(projectModuleId.getArtifactId()))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }
}
