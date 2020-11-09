package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.xml.XMLAttribute;
import com.neaterbits.util.parse.context.Context;

public final class PomXMLEventListener
    extends BaseXMLEventListener<Void> {

	private final PomEventListener delegate;
	
	private boolean inProperties;

	public PomXMLEventListener(PomEventListener delegate) {
	    super(delegate);
	    
		Objects.requireNonNull(delegate);

		this.delegate = delegate;
	}

	@Override
    protected boolean allowTextForUnknownTag() {
	    
	    // Within <properties>, call onText() for unknown tags
	    // since property tag names may be user defined and do not follow any schema
	    
        return inProperties;
    }

    @Override
	public void onStartElement(Context context, String localPart, List<XMLAttribute> attributes, Void param) {

		switch (localPart) {

		case "project":
			delegate.onProjectStart(context);
			break;

		case "parent":
			delegate.onParentStart(context);
			break;

		case "properties":
		    delegate.onPropertiesStart(context);
            this.inProperties = true;
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
		    super.onStartElement(context, localPart, attributes, param);
		    break;
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
			
		case "properties":
		    delegate.onPropertiesEnd(context);
            this.inProperties = false;
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
		    super.onEndElement(context, localPart, param);
			break;
		}
	}
}
