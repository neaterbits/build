package com.neaterbits.build.buildsystem.maven.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.neaterbits.build.buildsystem.maven.effective.POMModel;
import com.neaterbits.build.buildsystem.maven.elements.MavenProject;
import com.neaterbits.build.buildsystem.maven.xml.MavenXMLProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReader;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;

public final class PomTreeParser {

	public static <DOCUMENT>
		MavenXMLProject<DOCUMENT> readModule(File pomFile, XMLReaderFactory<DOCUMENT> xmlReaderFactory) throws XMLReaderException, IOException {
		
		final StackFilePomEventListener pomEventListener = new StackFilePomEventListener(pomFile.getParentFile());

		final DOCUMENT document;
		
		try (InputStream inputStream = new FileInputStream(pomFile)) {

			final XMLReader<DOCUMENT> xmlReader = xmlReaderFactory.createReader(inputStream, pomFile.getAbsolutePath());
			
			document = xmlReader.readXML(
					new PomXMLEventListener(pomEventListener),
					null);
		}

		return new MavenXMLProject<>(document, pomEventListener.getMavenProject());
	}

   public static <DOCUMENT>
       MavenXMLProject<DOCUMENT> readModule(
               InputStream inputStream,
               XMLReaderFactory<DOCUMENT> xmlReaderFactory,
               String filePath) throws XMLReaderException, IOException {
       
       final PomEventListener pomEventListener = new StackInputStreamPomEventListener();

       final DOCUMENT document;
       
       final XMLReader<DOCUMENT> xmlReader = xmlReaderFactory.createReader(inputStream, filePath);
       
       document = xmlReader.readXML(
               new PomXMLEventListener(pomEventListener),
               null);

       return new MavenXMLProject<>(document, null);
   }

	
	public static <NODE, ELEMENT extends NODE, DOCUMENT extends NODE>
		MavenProject parseToProject(DOCUMENT document, POMModel<NODE, ELEMENT, DOCUMENT> pomModel, File pomRootDirectory) {

		final StackFilePomEventListener pomEventListener = new StackFilePomEventListener(pomRootDirectory);
		final PomXMLEventListener eventListener = new PomXMLEventListener(pomEventListener);

		pomModel.iterate(
				document,
				eventListener,
				null,
				true);
		
		return pomEventListener.getMavenProject();
	}
}
