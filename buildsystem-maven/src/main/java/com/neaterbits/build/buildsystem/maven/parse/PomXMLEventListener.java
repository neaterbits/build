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
			
		case "exclusions":
		    delegate.onExclusionsStart(context);
		    break;
	
		case "exclusion":
		    delegate.onExclusionStart(context);
		    break;

		case "reporting":
			delegate.onReportingStart(context);
			break;

		case "build":
			delegate.onBuildStart(context);
			break;
			
		case "directory":
		    delegate.onDirectoryStart(context);
		    break;
		    
		case "defaultGoal":
		    delegate.onDefaultGoalStart(context);
		    break;

		case "outputDirectory":
            delegate.onOutputDirectoryStart(context);
            break;

        case "finalName":
            delegate.onFinalNameStart(context);
            break;

        case "sourceDirectory":
            delegate.onSourceDirectoryStart(context);
            break;
            
        case "scriptSourceDirectory":
            delegate.onScriptSourceDirectoryStart(context);
            break;

        case "testSourceDirectory":
            delegate.onTestSourceDirectoryStart(context);
            break;
            
        case "resources":
            delegate.onResourcesStart(context);
            break;
            
        case "resource":
            delegate.onResourceStart(context);
            break;
            
        case "targetPath":
            delegate.onTargetPathStart(context);
            break;
            
        case "filtering":
            delegate.onFilteringStart(context);
            break;

        case "includes":
            delegate.onIncludesStart(context);
            break;
            
        case "include":
            delegate.onIncludeStart(context);
            break;
            
        case "excludes":
            delegate.onExcludesStart(context);
            break;
            
        case "exclude":
            delegate.onExcludeStart(context);
            break;
            
        case "testResources":
            delegate.onTestResourcesStart(context);
            break;
            
        case "testResource":
            delegate.onTestResourceStart(context);
            break;
            
        case "pluginManagement":
            delegate.onPluginManagementStart(context);
            break;

        case "plugins":
			delegate.onPluginsStart(context);
			break;

		case "plugin":
			delegate.onPluginStart(context);
			break;

        case "executions":
            delegate.onExecutionsStart(context);
            break;
            
        case "execution":
            delegate.onExecutionStart(context);
            break;

        case "extensions":
			delegate.onExtensionsStart(context);
			break;

		case "extension":
			delegate.onExtensionStart(context);
			break;

		case "issueManagement":
		    delegate.onIssueManagementStart(context);
		    break;
		    
		case "system":
		    delegate.onSystemStart(context);
		    break;
			
        case "repositories":
            delegate.onRepositoriesStart(context);
            break;
            
        case "repository":
            delegate.onRepositoryStart(context);
            break;

        case "pluginRepositories":
		    delegate.onPluginRepositoriesStart(context);
		    break;
			
        case "pluginRepository":
            delegate.onPluginRepositoryStart(context);
            break;
            
        case "releases":
            delegate.onReleasesStart(context);
            break;
            
        case "enabled":
            delegate.onEnabledStart(context);
            break;
            
        case "updatePolicy":
            delegate.onUpdatePolicyStart(context);
            break;
            
        case "checksumPolicy":
            delegate.onChecksumPolicyStart(context);
            break;
            
        case "snapshots":
            delegate.onSnapshotsStart(context);
            break;
            
        case "name":
            delegate.onNameStart(context);
            break;
            
        case "id":
            delegate.onIdStart(context);
            break;
            
        case "url":
            delegate.onUrlStart(context);
            break;
            
        case "layout":
            delegate.onLayoutStart(context);
            break;

        case "profiles":
            delegate.onProfilesStart(context);
            break;
            
        case "profile":
            delegate.onProfileStart(context);
            break;
            
        case "activation":
            delegate.onActivationStart(context);
            break;

        case "activeByDefault":
            delegate.onActiveByDefaultStart(context);
            break;
            
        case "jdk":
            delegate.onJdkStart(context);
            break;
            
        case "os":
            delegate.onOsStart(context);
            break;
            
        case "family":
            delegate.onFamilyStart(context);
            break;
            
        case "arch":
            delegate.onArchStart(context);
            break;
            
        case "property":
            delegate.onPropertyStart(context);
            break;

        case "value":
            delegate.onValueStart(context);
            break;
            
        case "file":
            delegate.onFileStart(context);
            break;
            
        case "exists":
            delegate.onExistsStart(context);
            break;
            
        case "missing":
            delegate.onMissingStart(context);
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

        case "exclusions":
            delegate.onExclusionsEnd(context);
            break;
    
        case "exclusion":
            delegate.onExclusionEnd(context);
            break;

        case "reporting":
			delegate.onReportingEnd(context);
			break;

		case "build":
			delegate.onBuildEnd(context);
			break;

        case "directory":
            delegate.onDirectoryEnd(context);
            break;
            
        case "defaultGoal":
            delegate.onDefaultGoalEnd(context);
            break;

        case "outputDirectory":
            delegate.onOutputDirectoryEnd(context);
            break;

        case "finalName":
            delegate.onFinalNameEnd(context);
            break;

        case "sourceDirectory":
            delegate.onSourceDirectoryEnd(context);
            break;
            
        case "scriptSourceDirectory":
            delegate.onScriptSourceDirectoryEnd(context);
            break;

        case "testSourceDirectory":
            delegate.onTestSourceDirectoryEnd(context);
            break;

        case "resources":
            delegate.onResourcesEnd(context);
            break;
            
        case "resource":
            delegate.onResourceEnd(context);
            break;
            
        case "targetPath":
            delegate.onTargetPathEnd(context);
            break;
            
        case "filtering":
            delegate.onFilteringEnd(context);
            break;

        case "includes":
            delegate.onIncludesEnd(context);
            break;
            
        case "include":
            delegate.onIncludeEnd(context);
            break;
            
        case "excludes":
            delegate.onExcludesEnd(context);
            break;
            
        case "exclude":
            delegate.onExcludeEnd(context);
            break;
            
        case "testResources":
            delegate.onTestResourcesEnd(context);
            break;
            
        case "testResource":
            delegate.onTestResourceEnd(context);
            break;

        case "pluginManagement":
            delegate.onPluginManagementEnd(context);
            break;

        case "plugins":
			delegate.onPluginsEnd(context);
			break;

		case "plugin":
			delegate.onPluginEnd(context);
			break;

        case "executions":
            delegate.onExecutionsEnd(context);
            break;
            
        case "execution":
            delegate.onExecutionEnd(context);
            break;

        case "extensions":
			delegate.onExtensionsEnd(context);
			break;

		case "extension":
			delegate.onExtensionEnd(context);
			break;
			
        case "issueManagement":
            delegate.onIssueManagementEnd(context);
            break;
            
        case "system":
            delegate.onSystemEnd(context);
            break;

        case "repositories":
            delegate.onRepositoriesEnd(context);
            break;

        case "repository":
            delegate.onRepositoryEnd(context);
            break;

        case "pluginRepositories":
            delegate.onPluginRepositoriesEnd(context);
            break;

        case "pluginRepository":
            delegate.onPluginRepositoryEnd(context);
            break;
            
        case "releases":
            delegate.onReleasesEnd(context);
            break;
            
        case "enabled":
            delegate.onEnabledEnd(context);
            break;
            
        case "updatePolicy":
            delegate.onUpdatePolicyEnd(context);
            break;
            
        case "checksumPolicy":
            delegate.onChecksumPolicyEnd(context);
            break;
            
        case "snapshots":
            delegate.onSnapshotsEnd(context);
            break;
            
        case "name":
            delegate.onNameEnd(context);
            break;
            
        case "id":
            delegate.onIdEnd(context);
            break;
            
        case "url":
            delegate.onUrlEnd(context);
            break;
            
        case "layout":
            delegate.onLayoutEnd(context);
            break;
            
        case "profiles":
            delegate.onProfilesEnd(context);
            break;
            
        case "profile":
            delegate.onProfileEnd(context);
            break;
            
        case "activation":
            delegate.onActivationEnd(context);
            break;

        case "activeByDefault":
            delegate.onActiveByDefaultEnd(context);
            break;
            
        case "jdk":
            delegate.onJdkEnd(context);
            break;

        case "os":
            delegate.onOsEnd(context);
            break;
            
        case "family":
            delegate.onFamilyEnd(context);
            break;
            
        case "arch":
            delegate.onArchEnd(context);
            break;
            
        case "property":
            delegate.onPropertyEnd(context);
            break;

        case "value":
            delegate.onValueEnd(context);
            break;
            
        case "file":
            delegate.onFileEnd(context);
            break;
            
        case "exists":
            delegate.onExistsEnd(context);
            break;
            
        case "missing":
            delegate.onMissingEnd(context);
            break;

        default:
		    super.onEndElement(context, localPart, param);
			break;
		}
	}
}
