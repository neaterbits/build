package com.neaterbits.build.buildsystem.maven.effective;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.w3c.dom.Document;

import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;
import com.neaterbits.build.buildsystem.maven.xml.dom.DOMModel;
import com.neaterbits.build.buildsystem.maven.xml.dom.DOMReaderFactory;

public final class EffectivePOMReader {

    private final boolean debug;
    
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
    
    public EffectivePOMReader(boolean debug) {
        
        this.debug = debug;
        
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
        
        if (debug) {
            System.out.println("## ENTER computeEffectiveProject() " + module.getModuleId());
        }

        final MavenModuleId projectModuleId = module.getModuleId();
        
        final List<DocumentModule<Document>> modules = new ArrayList<>();
        
        final Set<MavenModuleId> distinctModuleIds = new HashSet<>();
        
        for (DocumentModule<Document> toCompute = module; toCompute != null;) {

            if (debug) {
                System.out.println("## computeEffectiveProject toCompute " + toCompute.getModuleId());

                // DOMModel.INSTANCE.printDocument(toCompute.getDocument(), System.out);
            }

            final MavenModuleId moduleId = toCompute.getModuleId();
            
            if (distinctModuleIds.contains(moduleId)) {
                throw new IllegalStateException("Non distinct moduleId " + moduleId);
            }
            
            distinctModuleIds.add(moduleId);
            
            if (debug) {
                System.out.println("## computeEffectiveProject add to compute " + moduleId);
            }
            
            modules.add(toCompute);
            
            final MavenModuleId parentModuleId = toCompute.getXMLProject().getProject().getParentModuleId();
            
            toCompute = parentModuleId != null
                            ? getModule.apply(parentModuleId)
                            : null;

        }

        if (debug) {
            System.out.println("## compute effective with " + modules.stream()
                .map(DocumentModule::getModuleId)
                .collect(Collectors.toList()));
        }

        final List<MavenProject> computedProjects = computeEffectiveProjects(modules);
    
        final MavenProject result = computedProjects.stream()
                .filter(p -> p.getModuleId().getArtifactId().equals(projectModuleId.getArtifactId()))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
        
        if (debug) {
            System.out.println("## EXIT computeEffectiveProject() " + result 
                    + (result.getCommon().getDependencies() != null
                                ? result.getCommon().getDependencies().stream()
                                        .map(dep -> dep.getModuleId())
                                        .collect(Collectors.toList())
                                : ""));
        }

        return result;
    }
}
