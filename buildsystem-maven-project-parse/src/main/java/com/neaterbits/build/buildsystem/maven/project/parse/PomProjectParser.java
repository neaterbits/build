package com.neaterbits.build.buildsystem.maven.project.parse;

import java.io.File;
import java.io.IOException;

import com.neaterbits.build.buildsystem.maven.project.model.xml.MavenXMLProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;

public interface PomProjectParser<DOCUMENT> {

    MavenXMLProject<DOCUMENT> parse(File file, XMLReaderFactory<DOCUMENT> xmlReaderFactory)
        throws IOException, XMLReaderException;
    
}
