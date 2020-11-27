package com.neaterbits.build.buildsystem.maven.projects;

import java.io.File;

import com.neaterbits.build.buildsystem.common.ScanException;
import com.neaterbits.build.buildsystem.maven.project.model.xml.MavenXMLProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;

public interface MavenProjectsAccess {

    <DOCUMENT> MavenXMLProject<DOCUMENT>
    readModule(File pomFile, XMLReaderFactory<DOCUMENT> readerFactory) throws ScanException;

}
