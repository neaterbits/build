package com.neaterbits.build.buildsystem.maven.effective;

import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.project.model.xml.MavenXMLProject;

public final class DocumentModule<DOCUMENT> {

    private final MavenModuleId moduleId;
    private final MavenXMLProject<DOCUMENT> xmlProject;
    
    public DocumentModule(MavenModuleId moduleId, MavenXMLProject<DOCUMENT> xmlProject) {
        
        Objects.requireNonNull(moduleId);
        Objects.requireNonNull(xmlProject);
        
        if (moduleId.getGroupId() == null) {
            throw new IllegalArgumentException();
        }
        
        if (moduleId.getArtifactId() == null) {
            throw new IllegalArgumentException();
        }
        
        if (moduleId.getVersion() == null) {
            throw new IllegalArgumentException();
        }
        
        this.moduleId = moduleId;
        this.xmlProject = xmlProject;
    }

    MavenModuleId getModuleId() {
        return moduleId;
    }

    public MavenXMLProject<DOCUMENT> getXMLProject() {
        return xmlProject;
    }
}
