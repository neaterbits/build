package com.neaterbits.ide.buildsystem.maven.xml;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class XMLReader {

	public static <T> void readXML(XMLEventReader xmlEventReader, XMLEventListener<T> eventListener, T param) throws XMLStreamException {
		
		while (xmlEventReader.hasNext()) {
			
			final XMLEvent xmlEvent = xmlEventReader.nextEvent();
			
			switch (xmlEvent.getEventType()) {
			case XMLEvent.START_ELEMENT:
				eventListener.onStartElement((StartElement)xmlEvent, param);
				break;
				
			case XMLEvent.END_ELEMENT:
				eventListener.onEndElement((EndElement)xmlEvent, param);
				break;
				
			case XMLEvent.CHARACTERS:
				eventListener.onText((Characters)xmlEvent, param);
				break;
			}
		}
	}
}
