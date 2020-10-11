package com.neaterbits.build.buildsystem.maven.xml.dom;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.neaterbits.build.buildsystem.maven.xml.XMLReader;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;

public final class DOMReaderFactory implements XMLReaderFactory {

	@Override
	public XMLReader createReader(InputStream inputStream, String filePath) throws XMLReaderException {
	
		final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		
		final DocumentBuilder documentBuilder;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
			throw new XMLReaderException("Failed to create DocumentBuilder", ex);
		}
		
		return new DOMReader(documentBuilder, inputStream);
	}
}
