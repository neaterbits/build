package com.neaterbits.build.buildsystem.maven.xml;

import java.io.InputStream;

public interface XMLReaderFactory<DOCUMENT> {
	
	XMLReader<DOCUMENT> createReader(InputStream inputStream, String filePath) throws XMLReaderException;

}
