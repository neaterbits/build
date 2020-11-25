package com.neaterbits.build.buildsystem.maven.xml;

import java.util.List;

import com.neaterbits.util.parse.context.Context;

public interface XMLEventListener<T> {

	void onStartDocument(T param);

	void onStartElement(Context context, String localPart, List<XMLAttribute> attributes, T param);

	void onEndElement(Context context, String localPart, T param);

	void onText(Context context, String data, T param);

	void onEndDocument(T param);
}
