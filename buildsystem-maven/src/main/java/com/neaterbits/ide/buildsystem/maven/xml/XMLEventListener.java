package com.neaterbits.ide.buildsystem.maven.xml;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;

public interface XMLEventListener<T> {

	void onStartDocument(StartDocument event, T param);

	void onStartElement(StartElement event, T param);

	void onEndElement(EndElement event, T param);

	void onText(Characters event, T param);

	void onEndDocument(EndDocument event, T param);
}
