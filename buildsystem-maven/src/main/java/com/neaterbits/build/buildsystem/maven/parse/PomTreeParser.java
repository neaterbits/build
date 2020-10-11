package com.neaterbits.build.buildsystem.maven.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.neaterbits.build.buildsystem.maven.elements.MavenProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReader;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;
import com.neaterbits.build.buildsystem.maven.xml.stream.JavaxXMLStreamReaderFactory;

public final class PomTreeParser {

	public static MavenProject readModule(File pomFile) throws XMLReaderException, IOException {
		
		final XMLReaderFactory xmlReaderFactory = new JavaxXMLStreamReaderFactory();

		final StackPomEventListener pomEventListener = new StackPomEventListener(pomFile.getParentFile());

		try (InputStream inputStream = new FileInputStream(pomFile)) {

			final XMLReader xmlReader = xmlReaderFactory.createReader(inputStream, pomFile.getAbsolutePath());
			
			xmlReader.readXML(
					new PomXMLEventListener(pomFile, pomEventListener),
					null);
		}

		return pomEventListener.getMavenProject();
	}
}
