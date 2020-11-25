package com.neaterbits.build.buildsystem.maven.xml.dom;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import com.neaterbits.build.buildsystem.maven.xml.XMLReader;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;

public final class DOMReaderFactory implements XMLReaderFactory<Document> {

	@Override
	public XMLReader<Document> createReader(InputStream inputStream, String filePath) throws XMLReaderException {
	
		final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

		/*
		documentBuilderFactory.setIgnoringElementContentWhitespace(true);
		
		final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		
		final URL schemaUrl;
		try {
			schemaUrl = new URL("http://maven.apache.org/xsd/maven-4.0.0.xsd");
		} catch (MalformedURLException ex) {
			throw new IllegalStateException(ex);
		}
		
		final Schema schema;
		try {
			schema = schemaFactory.newSchema(schemaUrl);
		} catch (SAXException ex) {
			throw new XMLReaderException("Failed to parse schema", ex);
		}

		documentBuilderFactory.setValidating(false);
		documentBuilderFactory.setSchema(schema);
		*/
		
		final DocumentBuilder documentBuilder;
	
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
			throw new XMLReaderException("Failed to create DocumentBuilder", ex);
		}
		
		return new DOMReader(documentBuilder, inputStream, true);
	}
}
