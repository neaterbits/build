package com.neaterbits.build.buildsystem.maven.xml;

import java.io.IOException;

public interface XMLReader<DOCUMENT> {

	<T> DOCUMENT readXML(XMLEventListener<T> eventListener, T param) throws XMLReaderException, IOException;

}
