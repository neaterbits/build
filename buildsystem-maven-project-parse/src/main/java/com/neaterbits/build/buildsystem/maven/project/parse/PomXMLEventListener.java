package com.neaterbits.build.buildsystem.maven.project.parse;

import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.common.parse.listeners.BaseXMLEventListener;
import com.neaterbits.build.buildsystem.maven.xml.XMLAttribute;
import com.neaterbits.util.parse.context.Context;

public final class PomXMLEventListener
    extends BaseXMLEventListener<Void> {

	private final PomEventListener delegate;
	
	private boolean inProperties;
	private boolean inConfiguration;

	public PomXMLEventListener(PomEventListener delegate) {
	    super(delegate);
	    
		Objects.requireNonNull(delegate);

		this.delegate = delegate;
	}

	@Override
    protected boolean allowTextForUnknownTag() {
	    
	    // Within <properties>, call onText() for unknown tags
	    // since property tag names may be user defined and do not follow any schema
	    
        return withinUserUnknownTag();
    }

	
    @Override
    protected boolean withinUserUnknownTag() {
        return inProperties || inConfiguration;
    }

    @Override
	public void onStartElement(Context context, String localPart, List<XMLAttribute> attributes, Void param) {
        
        if (withinUnknownTag() || withinUserUnknownTag()) {
            super.onStartElement(context, localPart, attributes, param);
        }
        else {
    		switch (localPart) {
    
    		case "project":
    			delegate.onProjectStart(context);
    			break;
    
    		case "parent":
    			delegate.onParentStart(context);
    			break;
    
            case "relativePath":
                delegate.onRelativePathStart(context);
                break;
    
            case "description":
                delegate.onDescriptionStart(context);
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
    
            case "packaging":
                delegate.onPackagingStart(context);
                break;

            case "dependencyManagement":
    		    delegate.onDependencyManagementStart(context);
    		    break;
    			
    		case "dependencies":
    			delegate.onDependenciesStart(context);
    			break;
    
    		case "dependency":
    			delegate.onDependencyStart(context);
    			break;
    			
    		case "classifier":
    		    delegate.onClassifierStart(context);
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
    			
    		case "reportSets":
    		    delegate.onReportSetsStart(context);
    		    break;
    
            case "reportSet":
                delegate.onReportSetStart(context);
                break;
    
            case "reports":
                delegate.onReportsStart(context);
                break;
    
            case "report":
                delegate.onReportStart(context);
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
                
            case "filters":
                delegate.onFiltersStart(context);
                break;
                
            case "filter":
                delegate.onFilterStart(context);
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
    			
    		case "inherited":
    		    delegate.onInheritedStart(context);
    		    break;
    		    
    		case "configuration":
    		    delegate.onConfigurationStart(context);
    		    this.inConfiguration = true;
    		    break;
    
            case "phase":
                delegate.onPhaseStart(context);
                break;
                
            case "goals":
                delegate.onGoalsStart(context);
                break;
                
            case "goal":
                delegate.onGoalStart(context);
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
    
            case "organization":
                delegate.onOrganizationStart(context);
                break;
                
    		case "issueManagement":
    		    delegate.onIssueManagementStart(context);
    		    break;
    		    
    		case "system":
    		    delegate.onSystemStart(context);
    		    break;
    			
            case "ciManagement":
                delegate.onCiManagementStart(context);
                break;
                
            case "notifiers":
                delegate.onNotifiersStart(context);
                break;
                
            case "notifier":
                delegate.onNotifierStart(context);
                break;
                
            case "type":
                delegate.onTypeStart(context);
                break;
                
            case "sendOnError":
                delegate.onSendOnErrorStart(context);
                break;
    
            case "sendOnFailure":
                delegate.onSendOnFailureStart(context);
                break;
                
            case "sendOnSuccess":
                delegate.onSendOnSuccessStart(context);
                break;
                
            case "sendOnWarning":
                delegate.onSendOnWarningStart(context);
                break;
    
            case "mailingLists":
                delegate.onMailingListsStart(context);
                break;
    
            case "mailingList":
                delegate.onMailingListStart(context);
                break;
    
            case "subscribe":
                delegate.onSubscribeStart(context);
                break;
    
            case "unsubscribe":
                delegate.onUnsubscribeStart(context);
                break;
    
            case "post":
                delegate.onPostStart(context);
                break;
                
            case "archive":
                delegate.onArchiveStart(context);
                break;
    
            case "otherArchives":
                delegate.onOtherArchivesStart(context);
                break;
    
            case "otherArchive":
                delegate.onOtherArchiveStart(context);
                break;
                
            case "scm":
                delegate.onScmStart(context);
                break;
    
            case "connection":
                delegate.onConnectionStart(context);
                break;
    
            case "developerConnection":
                delegate.onDeveloperConnectionStart(context);
                break;
    
            case "tag":
                delegate.onTagStart(context);
                break;
    
            case "distributionManagement":
                delegate.onDistributionManagementStart(context);
                break;
    
            case "downloadUrl":
                delegate.onDownloadUrlStart(context);
                break;
    
            case "status":
                delegate.onStatusStart(context);
                break;
                
            case "uniqueVersion":
                delegate.onUniqueVersionStart(context);
                break;
                
            case "snapshotRepository":
                delegate.onSnapshotRepositoryStart(context);
                break;
                
            case "site":
                delegate.onSiteStart(context);
                break;
                
            case "relocation":
                delegate.onRelocationStart(context);
                break;
                
            case "message":
                delegate.onMessageStart(context);
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
	}

	@Override
	public void onEndElement(Context context, String localPart, Void param) {

	    if (withinUnknownTag()) {
	        super.onEndElement(context, localPart, param);
	    }
	    else {
            switch (localPart) {
    
    		case "project":
    			delegate.onProjectEnd(context);
    			break;
    
    		case "parent":
    			delegate.onParentEnd(context);
    			break;
    			
    		case "relativePath":
    		    delegate.onRelativePathEnd(context);
    		    break;
    			
            case "description":
                delegate.onDescriptionEnd(context);
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
    
            case "packaging":
                delegate.onPackagingEnd(context);
                break;

            case "dependencyManagement":
                delegate.onDependencyManagementEnd(context);
                break;
    
            case "dependencies":
    			delegate.onDependenciesEnd(context);
    			break;
    
            case "classifier":
                delegate.onClassifierEnd(context);
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
    
            case "reportSets":
                delegate.onReportSetsEnd(context);
                break;
    
            case "reportSet":
                delegate.onReportSetEnd(context);
                break;
    
            case "reports":
                delegate.onReportsEnd(context);
                break;
    
            case "report":
                delegate.onReportEnd(context);
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
    
            case "filters":
                delegate.onFiltersEnd(context);
                break;
                
            case "filter":
                delegate.onFilterEnd(context);
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
    
            case "inherited":
                delegate.onInheritedEnd(context);
                break;
    
            case "configuration":
                delegate.onConfigurationEnd(context);
                this.inConfiguration = false;
                break;
    
            case "phase":
                delegate.onPhaseEnd(context);
                break;
                
            case "goals":
                delegate.onGoalsEnd(context);
                break;
                
            case "goal":
                delegate.onGoalEnd(context);
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
    			
            case "organization":
                delegate.onOrganizationEnd(context);
                break;
                
            case "issueManagement":
                delegate.onIssueManagementEnd(context);
                break;
                
            case "system":
                delegate.onSystemEnd(context);
                break;
                
            case "ciManagement":
                delegate.onCiManagementEnd(context);
                break;
                
            case "notifiers":
                delegate.onNotifiersEnd(context);
                break;
                
            case "notifier":
                delegate.onNotifierEnd(context);
                break;
                
            case "type":
                delegate.onTypeEnd(context);
                break;
    
            case "sendOnError":
                delegate.onSendOnErrorEnd(context);
                break;
    
            case "sendOnFailure":
                delegate.onSendOnFailureEnd(context);
                break;
                
            case "sendOnSuccess":
                delegate.onSendOnSuccessEnd(context);
                break;
                
            case "sendOnWarning":
                delegate.onSendOnWarningEnd(context);
                break;
    
            case "mailingLists":
                delegate.onMailingListsEnd(context);
                break;
    
            case "mailingList":
                delegate.onMailingListEnd(context);
                break;
    
            case "subscribe":
                delegate.onSubscribeEnd(context);
                break;
    
            case "unsubscribe":
                delegate.onUnsubscribeEnd(context);
                break;
    
            case "post":
                delegate.onPostEnd(context);
                break;
                
            case "archive":
                delegate.onArchiveEnd(context);
                break;
    
            case "otherArchives":
                delegate.onOtherArchivesEnd(context);
                break;
    
            case "otherArchive":
                delegate.onOtherArchiveEnd(context);
                break;
    
            case "scm":
                delegate.onScmEnd(context);
                break;
    
            case "connection":
                delegate.onConnectionEnd(context);
                break;
    
            case "developerConnection":
                delegate.onDeveloperConnectionEnd(context);
                break;
    
            case "tag":
                delegate.onTagEnd(context);
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
                
            case "distributionManagement":
                delegate.onDistributionManagementEnd(context);
                break;
    
            case "downloadUrl":
                delegate.onDownloadUrlEnd(context);
                break;
    
            case "status":
                delegate.onStatusEnd(context);
                break;
                
            case "uniqueVersion":
                delegate.onUniqueVersionEnd(context);
                break;
                
            case "snapshotRepository":
                delegate.onSnapshotRepositoryEnd(context);
                break;
    
            case "site":
                delegate.onSiteEnd(context);
                break;
                
            case "relocation":
                delegate.onRelocationEnd(context);
                break;
                
            case "message":
                delegate.onMessageEnd(context);
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
}
