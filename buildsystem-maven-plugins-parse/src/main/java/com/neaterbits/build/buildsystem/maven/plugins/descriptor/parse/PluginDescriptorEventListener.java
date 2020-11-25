package com.neaterbits.build.buildsystem.maven.plugins.descriptor.parse;

import com.neaterbits.build.buildsystem.maven.common.parse.configuration.PlexusConfigurationListener;
import com.neaterbits.build.buildsystem.maven.common.parse.listeners.BaseEventListener;
import com.neaterbits.build.buildsystem.maven.common.parse.listeners.EntityEventListener;
import com.neaterbits.build.buildsystem.maven.plexus.components.parse.common.PlexusRequirementsListener;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginDescriptor;
import com.neaterbits.util.parse.context.Context;

interface PluginDescriptorEventListener
    extends BaseEventListener,
            EntityEventListener,
            PlexusRequirementsListener,
            PlexusConfigurationListener {

    boolean inConfigurationElement(boolean startTag);
    
    void onPluginStart(Context context);

    void onNameStart(Context context);
    
    void onNameEnd(Context context);
    
    void onDescriptionStart(Context context);
    
    void onDescriptionEnd(Context context);

    void onGoalPrefixStart(Context context);

    void onGoalPrefixEnd(Context context);

    void onIsolatedRealmStart(Context context);

    void onIsolatedRealmEnd(Context context);

    void onInheritedByDefaultStart(Context context);

    void onInheritedByDefaultEnd(Context context);

    void onMojosStart(Context context);

    void onMojoStart(Context context);

    void onGoalStart(Context context);
    
    void onGoalEnd(Context context);
    
    void onImplementationStart(Context context);
    
    void onImplementationEnd(Context context);

    void onLanguageStart(Context context);
    
    void onLanguageEnd(Context context);
    
    void onPhaseStart(Context context);
    
    void onPhaseEnd(Context context);
    
    void onExecutePhaseStart(Context context);
    
    void onExecutePhaseEnd(Context context);

    void onExecuteGoalStart(Context context);
    
    void onExecuteGoalEnd(Context context);

    void onExecuteLifecycleStart(Context context);
    
    void onExecuteLifecycleEnd(Context context);

    void onRequiresDependencyResolutionStart(Context context);
    
    void onRequiresDependencyResolutionEnd(Context context);

    void onRequiresDependencyCollectionStart(Context context);
    
    void onRequiresDependencyCollectionEnd(Context context);
    
    void onRequiresDirectInvocationStart(Context context);

    void onRequiresDirectInvocationEnd(Context context);

    void onRequiresProjectStart(Context context);
    
    void onRequiresProjectEnd(Context context);
    
    void onRequiresReportsStart(Context context);
    
    void onRequiresReportsEnd(Context context);

    void onRequiresOnlineStart(Context context);
    
    void onRequiresOnlineEnd(Context context);

    void onAggregatorStart(Context context);
    
    void onAggregatorEnd(Context context);

    void onThreadSafeStart(Context context);
    
    void onThreadSafeEnd(Context context);
    
    void onInstantiationStrategyStart(Context context);

    void onInstantiationStrategyEnd(Context context);

    void onExecutionStrategyStart(Context context);

    void onExecutionStrategyEnd(Context context);

    void onSinceStart(Context context);
    
    void onSinceEnd(Context context);

    void onDeprecatedStart(Context context);
    
    void onDeprecatedEnd(Context context);

    void onConfiguratorStart(Context context);
    
    void onConfiguratorEnd(Context context);
    
    void onComposerStart(Context context);
    
    void onComposerEnd(Context context);

    void onParametersStart(Context context);
    
    void onParameterStart(Context context);

    void onAliasStart(Context context);
    
    void onAliasEnd(Context context);

    void onTypeStart(Context context);
    
    void onTypeEnd(Context context);

    void onRequiredStart(Context context);
    
    void onRequiredEnd(Context context);

    void onEditableStart(Context context);
    
    void onEditableEnd(Context context);

    void onParameterEnd(Context context);

    void onParametersEnd(Context context);

    void onMojoEnd(Context context);

    void onMojosEnd(Context context);

    void onDependenciesStart(Context context);

    void onDependencyStart(Context context);

    void onDependencyEnd(Context context);

    void onDependenciesEnd(Context context);

    MavenPluginDescriptor onPluginEnd(Context context);
}
