package com.neaterbits.ide.buildsystem.maven.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import com.neaterbits.ide.buildsystem.maven.elements.MavenProject;
import com.neaterbits.ide.buildsystem.maven.xml.XMLReader;

public final class PomTreeParser {

	public static MavenProject readModule(File pomFile) throws XMLStreamException, IOException {
		
		final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

		final StackPomEventListener pomEventListener = new StackPomEventListener(pomFile.getParentFile());

		try (BufferedReader reader = new BufferedReader(new FileReader(pomFile))) {
			final XMLEventReader eventReader = xmlInputFactory.createXMLEventReader(reader);
			
			XMLReader.readXML(
					eventReader,
					new PomXMLEventListener(pomFile, pomEventListener),
					null);
		}

		return pomEventListener.getMavenProject();
	}
}
