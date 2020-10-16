package com.neaterbits.build.buildsystem.maven.xml.stream;

import java.util.Objects;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.neaterbits.build.buildsystem.maven.xml.XMLEventListener;
import com.neaterbits.build.buildsystem.maven.xml.XMLReader;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.util.parse.context.Context;
import com.neaterbits.util.parse.context.ImmutableFullContext;

final class JavaxXMLStreamReader implements XMLReader<Void> {

	private final String file;
	private final XMLEventReader xmlEventReader;
	
	JavaxXMLStreamReader(String file, XMLEventReader xmlEventReader) {
	
		Objects.requireNonNull(xmlEventReader);
	
		this.file = file;
		this.xmlEventReader = xmlEventReader;
	}

	@Override
	public <T> Void readXML(XMLEventListener<T> eventListener, T param) throws XMLReaderException {

		try {
			while (xmlEventReader.hasNext()) {
				
				final XMLEvent xmlEvent = xmlEventReader.nextEvent();
				
				switch (xmlEvent.getEventType()) {
				case XMLEvent.START_ELEMENT:
					
					final StartElement startElement = (StartElement)xmlEvent;
					
					eventListener.onStartElement(
							context(startElement),
							startElement.getName().getLocalPart(),
							param);
					break;
					
				case XMLEvent.END_ELEMENT:
				
					final EndElement endElement = (EndElement)xmlEvent;
					
					eventListener.onEndElement(
							context(endElement),
							endElement.getName().getLocalPart(),
							param);
					break;
					
				case XMLEvent.CHARACTERS:

					final Characters characters = (Characters)xmlEvent;
					
					eventListener.onText(
							context(characters),
							characters.getData(),
							param);
					break;
				}
			}
		}
		catch (XMLStreamException ex) {
			throw new XMLReaderException("Could not read XML", ex);
		}

		return null;
	}

	private Context context(XMLEvent event) {

		return new ImmutableFullContext(
				file,
				event.getLocation().getLineNumber(),
				event.getLocation().getColumnNumber(),
				event.getLocation().getCharacterOffset(),
				event.getLocation().getLineNumber(),
				event.getLocation().getColumnNumber(),
				event.getLocation().getCharacterOffset(),
				null);

	}
}
