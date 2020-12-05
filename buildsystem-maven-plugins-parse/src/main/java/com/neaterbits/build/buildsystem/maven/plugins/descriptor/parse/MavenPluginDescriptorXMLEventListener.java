package com.neaterbits.build.buildsystem.maven.plugins.descriptor.parse;

import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.common.parse.listeners.BaseXMLEventListener;
import com.neaterbits.build.buildsystem.maven.xml.XMLAttribute;
import com.neaterbits.util.parse.context.Context;

final class MavenPluginDescriptorXMLEventListener extends BaseXMLEventListener<Void> {

    private final PluginDescriptorEventListener delegate;

    MavenPluginDescriptorXMLEventListener(PluginDescriptorEventListener delegate) {
        super(delegate);
        
        Objects.requireNonNull(delegate);
        
        this.delegate = delegate;
    }

    @Override
    protected boolean withinUserUnknownTag() {
        return false;
    }

    @Override
    protected boolean allowTextForUnknownTag() {
        return withinUserUnknownTag();
    }
    
    @Override
    public void onStartElement(Context context, String localPart, List<XMLAttribute> attributes, Void param) {
        
        if (delegate.inConfigurationElement(true)) {
            super.onStartElement(context, localPart, attributes, param);
        }
        else {
            switch (localPart) {
            
            case "plugin":
                delegate.onPluginStart(context);
                break;
                
            case "name":
                delegate.onNameStart(context);
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
                
            case "description":
                delegate.onDescriptionStart(context);
                break;
    
            case "goalPrefix":
                delegate.onGoalPrefixStart(context);
                break;
                
            case "isolatedRealm":
                delegate.onIsolatedRealmStart(context);
                break;
                
            case "inheritedByDefault":
                delegate.onInheritedByDefaultStart(context);
                break;
                
            case "mojos":
                delegate.onMojosStart(context);
                break;
                
            case "mojo":
                delegate.onMojoStart(context);
                break;
                
            case "goal":
                delegate.onGoalStart(context);
                break;

            case "executePhase":
                delegate.onExecutePhaseStart(context);
                break;
                
            case "executeGoal":
                delegate.onExecuteGoalStart(context);
                break;
                
            case "executeLifecycle":
                delegate.onExecuteLifecycleStart(context);
                break;
                
            case "requiresDependencyResolution":
                delegate.onRequiresDependencyResolutionStart(context);
                break;
                
            case "requiresDependencyCollection":
                delegate.onRequiresDependencyCollectionStart(context);
                break;

            case "requiresDirectInvocation":
                delegate.onRequiresDirectInvocationStart(context);
                break;
    
            case "requiresProject":
                delegate.onRequiresProjectStart(context);
                break;
                
            case "requiresReports":
                delegate.onRequiresReportsStart(context);
                break;
    
            case "aggregator":
                delegate.onAggregatorStart(context);
                break;
    
            case "requiresOnline":
                delegate.onRequiresOnlineStart(context);
                break;
                
            case "phase":
                delegate.onPhaseStart(context);
                break;
                
            case "implementation":
                delegate.onImplementationStart(context);
                break;
                
            case "language":
                delegate.onLanguageStart(context);
                break;
    
            case "instantiationStrategy":
                delegate.onInstantiationStrategyStart(context);
                break;
                
            case "executionStrategy":
                delegate.onExecutionStrategyStart(context);
                break;
    
            case "since":
                delegate.onSinceStart(context);
                break;
                
            case "threadSafe":
                delegate.onThreadSafeStart(context);
                break;
                
            case "configurator":
                delegate.onConfiguratorStart(context);
                break;
                
            case "composer":
                delegate.onComposerStart(context);
                break;
                
            case "parameters":
                delegate.onParametersStart(context);
                break;
    
            case "parameter":
                delegate.onParameterStart(context);
                break;
    
            case "alias":
                delegate.onAliasStart(context);
                break;

            case "type":
                delegate.onTypeStart(context);
                break;
                
            case "required":
                delegate.onRequiredStart(context);
                break;
    
            case "editable":
                delegate.onEditableStart(context);
                break;
                
            case "deprecated":
                delegate.onDeprecatedStart(context);
                break;
                
            case "configuration":
                delegate.onConfigurationStart(context);
                break;
                
            case "requirements":
                delegate.onRequirementsStart(context);
                break;
                
            case "requirement":
                delegate.onRequirementStart(context);
                break;
                
            case "role":
                delegate.onRoleStart(context);
                break;
    
            case "role-hint":
                delegate.onRoleHintStart(context);
                break;
                
            case "field-name":
                delegate.onFieldNameStart(context);
                break;
    
            case "dependencies":
                delegate.onDependenciesStart(context);
                break;
                
            case "dependency":
                delegate.onDependencyStart(context);
                break;
                
            default:
                super.onStartElement(context, localPart, attributes, param);
                break;
            }
        }
    }

    @Override
    public void onEndElement(Context context, String localPart, Void param) {

        if (delegate.inConfigurationElement(false)) {
            super.onEndElement(context, localPart, param);
        }
        else {
            switch (localPart) {
            case "plugin":
                delegate.onPluginEnd(context);
                break;
                
            case "name":
                delegate.onNameEnd(context);
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
                
            case "description":
                delegate.onDescriptionEnd(context);
                break;
    
            case "goalPrefix":
                delegate.onGoalPrefixEnd(context);
                break;
    
            case "isolatedRealm":
                delegate.onIsolatedRealmEnd(context);
                break;
    
            case "inheritedByDefault":
                delegate.onInheritedByDefaultEnd(context);
                break;
    
            case "mojos":
                delegate.onMojosEnd(context);
                break;
                
            case "mojo":
                delegate.onMojoEnd(context);
                break;
    
            case "goal":
                delegate.onGoalEnd(context);
                break;
    
            case "executePhase":
                delegate.onExecutePhaseEnd(context);
                break;
                
            case "executeGoal":
                delegate.onExecuteGoalEnd(context);
                break;
                
            case "executeLifecycle":
                delegate.onExecuteLifecycleEnd(context);
                break;

            case "requiresDependencyResolution":
                delegate.onRequiresDependencyResolutionEnd(context);
                break;
    
            case "requiresDependencyCollection":
                delegate.onRequiresDependencyCollectionEnd(context);
                break;

            case "requiresDirectInvocation":
                delegate.onRequiresDirectInvocationEnd(context);
                break;
    
            case "requiresProject":
                delegate.onRequiresProjectEnd(context);
                break;
    
            case "requiresReports":
                delegate.onRequiresReportsEnd(context);
                break;
    
            case "aggregator":
                delegate.onAggregatorEnd(context);
                break;
    
            case "requiresOnline":
                delegate.onRequiresOnlineEnd(context);
                break;
    
            case "phase":
                delegate.onPhaseEnd(context);
                break;
    
            case "implementation":
                delegate.onImplementationEnd(context);
                break;
    
            case "language":
                delegate.onLanguageEnd(context);
                break;
    
            case "instantiationStrategy":
                delegate.onInstantiationStrategyEnd(context);
                break;
    
            case "executionStrategy":
                delegate.onExecutionStrategyEnd(context);
                break;
    
            case "since":
                delegate.onSinceEnd(context);
                break;
    
            case "threadSafe":
                delegate.onThreadSafeEnd(context);
                break;
    
            case "configurator":
                delegate.onConfiguratorEnd(context);
                break;
                
            case "composer":
                delegate.onComposerEnd(context);
                break;

            case "parameters":
                delegate.onParametersEnd(context);
                break;
    
            case "parameter":
                delegate.onParameterEnd(context);
                break;
    
            case "alias":
                delegate.onAliasEnd(context);
                break;

            case "type":
                delegate.onTypeEnd(context);
                break;
    
            case "required":
                delegate.onRequiredEnd(context);
                break;
    
            case "editable":
                delegate.onEditableEnd(context);
                break;
    
            case "deprecated":
                delegate.onDeprecatedEnd(context);
                break;
    
            case "configuration":
                delegate.onConfigurationEnd(context);
                break;
    
            case "requirements":
                delegate.onRequirementsEnd(context);
                break;
    
            case "requirement":
                delegate.onRequirementEnd(context);
                break;
    
            case "role":
                delegate.onRoleEnd(context);
                break;
    
            case "role-hint":
                delegate.onRoleHintEnd(context);
                break;
    
            case "field-name":
                delegate.onFieldNameEnd(context);
                break;
    
            case "dependencies":
                delegate.onDependenciesEnd(context);
                break;
                
            case "dependency":
                delegate.onDependencyEnd(context);
                break;
    
            default:
                super.onEndElement(context, localPart, param);
                break;
            }
        }
    }
}
