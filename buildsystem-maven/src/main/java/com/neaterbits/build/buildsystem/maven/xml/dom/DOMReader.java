package com.neaterbits.build.buildsystem.maven.xml.dom;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.neaterbits.build.buildsystem.maven.xml.XMLEventListener;
import com.neaterbits.build.buildsystem.maven.xml.XMLReader;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;

final class DOMReader implements XMLReader {
	
	private final DocumentBuilder documentBuilder;
	private final InputStream inputStream;
	
	DOMReader(DocumentBuilder documentBuilder, InputStream inputStream) {

		Objects.requireNonNull(documentBuilder);
		Objects.requireNonNull(inputStream);
		
		this.documentBuilder = documentBuilder;
		this.inputStream = inputStream;
	}

	@Override
	public <T> void readXML(XMLEventListener<T> eventListener, T param) throws XMLReaderException, IOException {

		try {
			final Document document = documentBuilder.parse(inputStream);
			
			eventListener.onStartDocument(param);
			
			iterate(document.getChildNodes(), eventListener, param);
			
			eventListener.onEndDocument(param);
		} catch (SAXException ex) {
			throw new XMLReaderException("Caught exception", ex);
		}
	}
	
	private <T> void iterate(NodeList nodeList, XMLEventListener<T> eventListener, T param) {

		final int length = nodeList.getLength();
		
		for (int i = 0; i < length; ++ i) {

			final Node node = nodeList.item(i);
			
			switch (node.getNodeType()) {
			
			case Node.ELEMENT_NODE:
				
				final String localPart = node.getNodeName();
				
				eventListener.onStartElement(null, localPart, param);
				
				if (node.hasChildNodes()) {
					iterate(node.getChildNodes(), eventListener, param);
				}

				eventListener.onEndElement(null, localPart, param);
				break;
			
			case Node.TEXT_NODE:
				eventListener.onText(null, node.getTextContent(), param);
				break;
				
				
			}
			
		}
	}
}
