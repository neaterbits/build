package com.neaterbits.build.buildsystem.maven.effective;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.w3c.dom.Document;

import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.project.model.xml.MavenXMLProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;
import com.neaterbits.build.buildsystem.maven.xml.dom.DOMModel;
import com.neaterbits.build.buildsystem.maven.xml.dom.DOMReaderFactory;

public final class EffectivePOMReader {

    private final XMLReaderFactory<Document> xmlReaderFactory;
    private final MavenResolveContext resolveContext;
    
    public EffectivePOMReader() {
        this.xmlReaderFactory = new DOMReaderFactory();
        this.resolveContext = MavenResolveContext.now();
    }
    
    public XMLReaderFactory<Document> getXMLReaderFactory() {
        return xmlReaderFactory;
    }

    public List<MavenProject> computeEffectiveProjects(List<MavenXMLProject<Document>> projects) {
        
        return EffectivePOMsHelper.computeEffectiveProjects(
                projects,
                DOMModel.INSTANCE,
                xmlReaderFactory,
                null,
                resolveContext);
    }

    public MavenProject computeEffectiveProject(
                                    MavenXMLProject<Document> project,
                                    Function<MavenModuleId, MavenXMLProject<Document>> getProject) {
        
        final List<MavenXMLProject<Document>> projects = new ArrayList<>();
        
        for (MavenXMLProject<Document> toCompute = project; toCompute != null;) {
            
            projects.add(toCompute);
            
            final MavenModuleId parentModuleId = toCompute.getProject().getParentModuleId();
            
            toCompute = parentModuleId != null
                            ? getProject.apply(parentModuleId)
                            : null;
        }
        
        final List<MavenProject> computedProjects = computeEffectiveProjects(projects);
    
        return computedProjects.stream()
                .filter(p -> p.getModuleId().equals(project.getProject().getModuleId()))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }
}
