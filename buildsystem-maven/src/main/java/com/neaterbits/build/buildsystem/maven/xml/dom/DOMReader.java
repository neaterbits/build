package com.neaterbits.build.buildsystem.maven.xml.dom;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.neaterbits.build.buildsystem.maven.xml.XMLAttribute;
import com.neaterbits.build.buildsystem.maven.xml.XMLEventListener;
import com.neaterbits.build.buildsystem.maven.xml.XMLReader;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;

final class DOMReader implements XMLReader<Document> {
	
	private final DocumentBuilder documentBuilder;
	private final InputStream inputStream;
	private final boolean skipIgnoreableWhitespace;
	
	DOMReader(DocumentBuilder documentBuilder, InputStream inputStream, boolean skipIgnoreableWhitespace) {

		Objects.requireNonNull(documentBuilder);
		Objects.requireNonNull(inputStream);
		
		this.documentBuilder = documentBuilder;
		this.inputStream = inputStream;
		this.skipIgnoreableWhitespace = skipIgnoreableWhitespace;
	}

	@Override
	public <T> Document readXML(XMLEventListener<T> eventListener, T param) throws XMLReaderException, IOException {

		final Document document;
		
		try {
			document = documentBuilder.parse(inputStream);

			if (eventListener != null) {
			    iterate(document, eventListener, param, skipIgnoreableWhitespace);
			}
		} catch (SAXException ex) {
			throw new XMLReaderException("Caught exception", ex);
		}
		
		return document;
	}
	
	static <T> void iterate(Document document, XMLEventListener<T> eventListener, T param) {
		
		Objects.requireNonNull(document);
		
		iterate(document, eventListener, param, false);
	}
	
	private static List<XMLAttribute> makeAttributes(Node element) {
	    
	    final List<XMLAttribute> list;
	    
	    final int numAttributes = element.getAttributes().getLength();
	    
	    if (numAttributes == 0) {
	        list = Collections.emptyList();
	    }
	    else {
	        list = new ArrayList<>(numAttributes);
	        
	        for (int i = 0; i < numAttributes; ++ i) {
	            final Attr attribute = (Attr)element.getAttributes().item(i);
	            
	            list.add(new XMLAttribute() {
                    
                    @Override
                    public String getName() {
                        return attribute.getLocalName();
                    }

                    @Override
                    public String getValue() {
                        return attribute.getValue();
                    }
                });
	        }
	    }

	    return list;
	}
	
	static <T> void iterate(
			Document document,
			XMLEventListener<T> eventListener,
			T param,
			boolean skipIgnoreableWhitespace) {
		
		eventListener.onStartDocument(param);
		
		final Node rootElement = document.getDocumentElement();
		
		eventListener.onStartElement(null, rootElement.getNodeName(), makeAttributes(rootElement), param);

		iterate(rootElement.getChildNodes(), eventListener, param, skipIgnoreableWhitespace);
		
		eventListener.onEndElement(null, rootElement.getNodeName(), param);

		eventListener.onEndDocument(param);
	}

	private static <T> void iterate(
			NodeList nodeList,
			XMLEventListener<T> eventListener,
			T param,
			boolean skipIgnoreableWhitespace) {

		final int length = nodeList.getLength();
		
		for (int i = 0; i < length; ++ i) {

			final Node node = nodeList.item(i);
			
			switch (node.getNodeType()) {
			
			case Node.ELEMENT_NODE:
				
				final String localPart = node.getNodeName();
				
				eventListener.onStartElement(null, localPart, makeAttributes(node), param);
				
				if (node.hasChildNodes()) {
					iterate(node.getChildNodes(), eventListener, param, skipIgnoreableWhitespace);
				}

				eventListener.onEndElement(null, localPart, param);
				break;
			
			case Node.TEXT_NODE:
				
				if (!(skipIgnoreableWhitespace && (node.getNextSibling() != null || node.getPreviousSibling() != null))) {
					eventListener.onText(null, node.getTextContent(), param);
				}
				break;
			}
		}
	}
}
