package com.neaterbits.build.buildsystem.maven.parse;

import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.xml.XMLEventListener;
import com.neaterbits.util.parse.context.Context;

public final class PomXMLEventListener implements XMLEventListener<Void> {

	private final PomEventListener delegate;
	
	private int unknownTag;

	public PomXMLEventListener(PomEventListener delegate) {

		Objects.requireNonNull(delegate);

		this.delegate = delegate;
	}

	@Override
	public void onStartDocument(Void param) {

		this.unknownTag = 0;
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
			
		default:
			++ unknownTag;
			break;
		}
	}
	@Override
	public void onText(Context context, String data, Void param) {

		if (unknownTag == 0) {
			delegate.onText(context, data);
		}
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
			
		default:
			if (unknownTag == 0) {
				throw new IllegalStateException();
			}
		
			-- unknownTag;
			break;
		}
	}

	@Override
	public void onEndDocument(Void param) {

		if (unknownTag != 0) {
			throw new IllegalStateException();
		}
	}
}
