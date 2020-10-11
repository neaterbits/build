package com.neaterbits.build.buildsystem.maven.xml;

public interface XMLReader {

	<T> void readXML(XMLEventListener<T> eventListener, T param) throws XMLReaderException;

}
