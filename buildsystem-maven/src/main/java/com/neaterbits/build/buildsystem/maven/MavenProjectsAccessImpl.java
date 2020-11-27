package com.neaterbits.build.buildsystem.maven;

import java.io.File;

import com.neaterbits.build.buildsystem.common.ScanException;
import com.neaterbits.build.buildsystem.maven.project.model.xml.MavenXMLProject;
import com.neaterbits.build.buildsystem.maven.project.parse.PomTreeParser;
import com.neaterbits.build.buildsystem.maven.projects.MavenProjectsAccess;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;

final class MavenProjectsAccessImpl implements MavenProjectsAccess {

    @Override
    public <DOCUMENT> MavenXMLProject<DOCUMENT> readModule(File pomFile, XMLReaderFactory<DOCUMENT> readerFactory) throws ScanException {
        
        final MavenXMLProject<DOCUMENT> mavenXmlProject;
        
        try {
            mavenXmlProject = PomTreeParser.readModule(pomFile, readerFactory);
        } catch (Exception ex) {
           throw new ScanException("Failed to parse dependencies pom file for " + pomFile.getAbsolutePath(), ex);
        }
 
        return mavenXmlProject;
    }
}
