package com.neaterbits.build.buildsystem.maven.parse;

import java.io.File;
import java.util.Objects;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.neaterbits.build.buildsystem.maven.xml.XMLEventListener;
import com.neaterbits.util.parse.context.Context;
import com.neaterbits.util.parse.context.ImmutableFullContext;

public final class PomXMLEventListener implements XMLEventListener<Void> {

	private final File file;
	private final PomEventListener delegate;

	public PomXMLEventListener(File file, PomEventListener delegate) {

		Objects.requireNonNull(file);
		Objects.requireNonNull(delegate);

		this.file = file;
		this.delegate = delegate;
	}

	@Override
	public void onStartDocument(Void param) {

	}

	@Override
	public void onStartElement(Context context, String localPart, Void param) {

		switch (localPart) {

		case "project":
			delegate.onProjectStart(context);
			break;

		case "parent":
			delegate.onParentStart(context);
			break;

		case "modules":
			delegate.onModulesStart(context);
			break;

		case "module":
			delegate.onModuleStart(context);
			break;

		case "groupId":
			delegate.onGroupIdStart(context);
			break;

		case "artifactId":
			delegate.onArtifactIdStart(context);
			break;

		case "version":
			delegate.onVersionStart(context);
			break;

		case "dependencies":
			delegate.onDependenciesStart(context);
			break;

		case "dependency":
			delegate.onDependencyStart(context);
			break;

		case "scope":
			delegate.onScopeStart(context);
			break;

		case "optional":
			delegate.onOptionalStart(context);
			break;

		case "reporting":
			delegate.onReportingStart(context);
			break;

		case "build":
			delegate.onBuildStart(context);
			break;

		case "plugins":
			delegate.onPluginsStart(context);
			break;

		case "plugin":
			delegate.onPluginStart(context);
			break;

		case "extensions":
			delegate.onExtensionsStart(context);
			break;

		case "extension":
			delegate.onExtensionStart(context);
			break;
		}
	}
	@Override
	public void onText(Context context, String data, Void param) {


		delegate.onText(context, data);
	}

	@Override
	public void onEndElement(Context context, String localPart, Void param) {
		switch (localPart) {

		case "project":
			delegate.onProjectEnd(context);
			break;

		case "parent":
			delegate.onParentEnd(context);
			break;

		case "modules":
			delegate.onModulesEnd(context);
			break;

		case "module":
			delegate.onModuleEnd(context);
			break;

		case "groupId":
			delegate.onGroupIdEnd(context);
			break;

		case "artifactId":
			delegate.onArtifactIdEnd(context);
			break;

		case "version":
			delegate.onVersionEnd(context);
			break;

		case "dependencies":
			delegate.onDependenciesEnd(context);
			break;

		case "scope":
			delegate.onScopeEnd(context);
			break;

		case "dependency":
			delegate.onDependencyEnd(context);
			break;

		case "optional":
			delegate.onOptionalEnd(context);
			break;

		case "reporting":
			delegate.onReportingEnd(context);
			break;

		case "build":
			delegate.onBuildEnd(context);
			break;

		case "plugins":
			delegate.onPluginsEnd(context);
			break;

		case "plugin":
			delegate.onPluginEnd(context);
			break;

		case "extensions":
			delegate.onExtensionsEnd(context);
			break;

		case "extension":
			delegate.onExtensionEnd(context);
			break;
		}
	}

	@Override
	public void onEndDocument(Void param) {

	}
}
