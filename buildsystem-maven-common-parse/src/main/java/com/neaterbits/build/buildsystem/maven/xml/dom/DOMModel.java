package com.neaterbits.build.buildsystem.maven.xml.dom;

import java.util.Objects;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.neaterbits.build.buildsystem.maven.xml.XMLEventListener;
import com.neaterbits.build.buildsystem.maven.xml.model.DocumentModel;

public final class DOMModel implements DocumentModel<Node, Element, Document> {

	public static final DOMModel INSTANCE = new DOMModel();

	private DOMModel() {
		
	}

	@Override
	public Document createDocument(Document other) {

		final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		
		final Document document;
		
		try {
			document = documentBuilderFactory.newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException ex) {
			throw new IllegalStateException(ex);
		}
		
		if (document.getDocumentElement() != null) {
		    throw new IllegalStateException();
		}
		
		return document;
	}

	@Override
	public <T> void iterate(Document document, XMLEventListener<T> eventListener, T param, boolean skipIgnoreableWhitespace) {

		Objects.requireNonNull(document);
		Objects.requireNonNull(eventListener);

		DOMReader.iterate(document, eventListener, param, skipIgnoreableWhitespace);
	}

	@Override
    public Element getElement(Node node, String tagName) {

	    return findNode(node, tagName);
    }

    @Override
	public Element addElement(Node node, String tagName, Document document) {

        Objects.requireNonNull(node);
        Objects.requireNonNull(tagName);
        Objects.requireNonNull(document);
        
		final Element element = document.createElement(tagName);
		
		if (node instanceof Document) {
		    if (node != document) {
		        throw new IllegalArgumentException();
		    }
		    
		    if (((Document) node).getDocumentElement() != null) {
		        throw new IllegalStateException();
		    }
		}
	
	    node.appendChild(element);
	
		return element;
	}

	private Element findNode(Node node, String tagName) {
		
		final NodeList nodes = node.getChildNodes();

		Element found = null;
		
		for (int i = 0; i < nodes.getLength(); ++ i) {
			
			final Node sub = nodes.item(i);
			
			if (sub.getNodeType() == Node.ELEMENT_NODE && sub.getNodeName().equals(tagName)) {
				
				found = (Element)sub;
				break;
			}
		}
		
		return found;
	}
	
	@Override
	public Element addOrReplaceElement(Node node, String tagName, Document document) {

		final Element element;
		
		final Element existingNode = findNode(node, tagName);
		
		if (existingNode != null) {

			element = document.createElement(tagName);
			
			node.replaceChild(element, existingNode);
		}
		else {
			element = addElement(node, tagName, document);
		}
		
		return element;
	}

	@Override
	public void addText(Element element, String text, Document document) {

	    final Text textNode = document.createTextNode(text);
	    
		element.appendChild(textNode);
	}

    private boolean hasText(Element element) {

        return element.getChildNodes().getLength() == 1
                && element.getFirstChild().getNodeType() == Node.TEXT_NODE;
    }

    @Override
    public String getText(Element element) {
        
        final String text;
        
        if (hasText(element)) {
           
            final Text textNode = (Text)element.getFirstChild();
            
            text = textNode.getData();
        }
        else {
            text = null;
        }

        return text;
    }
}
