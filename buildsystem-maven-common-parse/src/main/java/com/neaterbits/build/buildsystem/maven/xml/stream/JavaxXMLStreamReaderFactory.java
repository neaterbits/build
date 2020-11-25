package com.neaterbits.build.buildsystem.maven.xml.stream;

import java.io.InputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import com.neaterbits.build.buildsystem.maven.xml.XMLReader;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;

public class JavaxXMLStreamReaderFactory implements XMLReaderFactory<Void> {

	@Override
	public XMLReader<Void> createReader(InputStream inputStream, String filePath) throws XMLReaderException {

		final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

		XMLEventReader eventReader;
		
		try {
			eventReader = xmlInputFactory.createXMLEventReader(inputStream);
		} catch (XMLStreamException ex) {
			throw new XMLReaderException("Failed to create reader", ex);
		}

		return new JavaxXMLStreamReader(filePath, eventReader, true);
	}
}
