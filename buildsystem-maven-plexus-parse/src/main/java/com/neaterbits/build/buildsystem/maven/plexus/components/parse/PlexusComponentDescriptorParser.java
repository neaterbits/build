package com.neaterbits.build.buildsystem.maven.plexus.components.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.neaterbits.build.buildsystem.maven.plexus.components.model.PlexusComponentSet;
import com.neaterbits.build.buildsystem.maven.xml.XMLReader;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;

public final class PlexusComponentDescriptorParser {

    public static
    PlexusComponentSet readDescriptor(File file, XMLReaderFactory<?> xmlReaderFactory) throws XMLReaderException, IOException {
        
        final PlexusComponentSet componentSet;
        
        try (InputStream inputStream = new FileInputStream(file)) {
    
            componentSet = readDescriptor(inputStream, xmlReaderFactory, file.getAbsolutePath());
        }
        
        return componentSet;
    }
    
    public static
    PlexusComponentSet readDescriptor(InputStream inputStream, XMLReaderFactory<?> xmlReaderFactory, String filePath) throws XMLReaderException, IOException {

        final StackPlexusComponentDescriptorEventListener eventListener
            = new StackPlexusComponentDescriptorEventListener();

        final XMLReader<?> xmlReader = xmlReaderFactory.createReader(inputStream, filePath);
        
        xmlReader.readXML(
                new PlexusComponentDescriptorXMLEventListener(eventListener),
                null);
    
        return eventListener.getComponentSet();
    }
}
