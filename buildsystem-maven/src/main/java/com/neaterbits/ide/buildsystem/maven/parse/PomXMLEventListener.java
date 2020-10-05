package com.neaterbits.ide.buildsystem.maven.parse;

import java.io.File;
import java.util.Objects;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ImmutableFullContext;
import com.neaterbits.ide.buildsystem.maven.xml.XMLEventListener;

public final class PomXMLEventListener implements XMLEventListener<Void> {

	private final File file;
	private final PomEventListener delegate;
	
	public PomXMLEventListener(File file, PomEventListener delegate) {
		
		Objects.requireNonNull(file);
		Objects.requireNonNull(delegate);

		this.file = file;
		this.delegate = delegate;
	}

	private Context context(XMLEvent event) {
		
		return new ImmutableFullContext(
				file.getPath(),
				event.getLocation().getLineNumber(),
				event.getLocation().getColumnNumber(),
				event.getLocation().getCharacterOffset(),
				event.getLocation().getLineNumber(),
				event.getLocation().getColumnNumber(),
				event.getLocation().getCharacterOffset(),
				null);
		
	}
	
	@Override
	public void onStartDocument(StartDocument event, Void param) {
		
	}

	@Override
	public void onStartElement(StartElement event, Void param) {
		
		switch (event.getName().getLocalPart()) {

		case "project":
			delegate.onProjectStart(context(event));
			break;
			
		case "parent":
			delegate.onParentStart(context(event));
			break;
			
		case "modules":
			delegate.onModulesStart(context(event));
			break;
			
		case "module":
			delegate.onModuleStart(context(event));
			break;

		case "groupId":
			delegate.onGroupIdStart(context(event));
			break;
			
		case "artifactId":
			delegate.onArtifactIdStart(context(event));
			break;
			
		case "version":
			delegate.onVersionStart(context(event));
			break;
		
		case "dependencies":
			delegate.onDependenciesStart(context(event));
			break;
			
		case "dependency":
			delegate.onDependencyStart(context(event));
			break;
			
		case "scope":
			delegate.onScopeStart(context(event));
			break;
			
		case "optional":
			delegate.onOptionalStart(context(event));
			break;
			
		case "reporting":
			delegate.onReportingStart(context(event));
			break;
			
		case "build":
			delegate.onBuildStart(context(event));
			break;
			
		case "plugins":
			delegate.onPluginsStart(context(event));
			break;
			
		case "plugin":
			delegate.onPluginStart(context(event));
			break;
		
		case "extensions":
			delegate.onExtensionsStart(context(event));
			break;
			
		case "extension":
			delegate.onExtensionStart(context(event));
			break;
		}
	}
	@Override
	public void onText(Characters event, Void param) {
		
		final String text = event.getData();
		
		delegate.onText(context(event), text);
	}

	@Override
	public void onEndElement(EndElement event, Void param) {
		switch (event.getName().getLocalPart()) {

		case "project":
			delegate.onProjectEnd(context(event));
			break;
			
		case "parent":
			delegate.onParentEnd(context(event));
			break;
			
		case "modules":
			delegate.onModulesEnd(context(event));
			break;
			
		case "module":
			delegate.onModuleEnd(context(event));
			break;

		case "groupId":
			delegate.onGroupIdEnd(context(event));
			break;
			
		case "artifactId":
			delegate.onArtifactIdEnd(context(event));
			break;
			
		case "version":
			delegate.onVersionEnd(context(event));
			break;
		
		case "dependencies":
			delegate.onDependenciesEnd(context(event));
			break;
			
		case "scope":
			delegate.onScopeEnd(context(event));
			break;

		case "dependency":
			delegate.onDependencyEnd(context(event));
			break;
			
		case "optional":
			delegate.onOptionalEnd(context(event));
			break;

		case "reporting":
			delegate.onReportingEnd(context(event));
			break;

		case "build":
			delegate.onBuildEnd(context(event));
			break;
			
		case "plugins":
			delegate.onPluginsEnd(context(event));
			break;
			
		case "plugin":
			delegate.onPluginEnd(context(event));
			break;

		case "extensions":
			delegate.onExtensionsEnd(context(event));
			break;
			
		case "extension":
			delegate.onExtensionEnd(context(event));
			break;
		}
	}

	@Override
	public void onEndDocument(EndDocument event, Void param) {
		
	}
}
