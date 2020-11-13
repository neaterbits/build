package com.neaterbits.build.buildsystem.maven.parse;

import com.neaterbits.util.parse.context.Context;

public interface PomEventListener
    extends
        BaseEventListener,
        EntityEventListener,
        DependenciesListener {

	void onProjectStart(Context context);

	void onParentStart(Context context);

	void onParentEnd(Context context);

	void onPropertiesStart(Context context);
	
	void onPropertiesEnd(Context context);
	
	void onModulesStart(Context context);

	void onModuleStart(Context context);

	void onModuleEnd(Context context);

	void onModulesEnd(Context context);

	void onReportingStart(Context context);

	void onReportingEnd(Context context);

	void onBuildStart(Context context);

    void onDirectoryStart(Context context);
    
    void onDirectoryEnd(Context context);
    
    void onOutputDirectoryStart(Context context);
    
    void onOutputDirectoryEnd(Context context);

    void onFinalNameStart(Context context);
    
    void onFinalNameEnd(Context context);

    void onSourceDirectoryStart(Context context);
    
    void onSourceDirectoryEnd(Context context);

    void onScriptSourceDirectoryStart(Context context);
    
    void onScriptSourceDirectoryEnd(Context context);

    void onTestSourceDirectoryStart(Context context);
    
    void onTestSourceDirectoryEnd(Context context);
    
    void onResourcesStart(Context context);
    
    void onResourceStart(Context context);

    void onTargetPathStart(Context context);
    
    void onTargetPathEnd(Context context);
    
    void onFilteringStart(Context context);
    
    void onFilteringEnd(Context context);
    
    void onIncludesStart(Context context);
    
    void onIncludeStart(Context context);
    
    void onIncludeEnd(Context context);

    void onIncludesEnd(Context context);

    void onExcludesStart(Context context);
    
    void onExcludeStart(Context context);
    
    void onExcludeEnd(Context context);

    void onExcludesEnd(Context context);

    void onResourceEnd(Context context);

    void onResourcesEnd(Context context);
    
    void onTestResourcesStart(Context context);
    
    void onTestResourceStart(Context context);

    void onTestResourceEnd(Context context);

    void onTestResourcesEnd(Context context);

    void onPluginsStart(Context context);

	void onPluginStart(Context context);
	
	void onExecutionsStart(Context context);
	
	void onExecutionStart(Context context);

	void onExecutionEnd(Context context);

    void onExecutionsEnd(Context context);

    void onPluginEnd(Context context);

	void onPluginsEnd(Context context);

	void onExtensionsStart(Context context);

	void onExtensionStart(Context context);

	void onExtensionEnd(Context context);

	void onExtensionsEnd(Context context);

	void onBuildEnd(Context context);

	void onRepositoriesStart(Context context);

    void onRepositoryStart(Context context);

    void onReleasesStart(Context context);
    
    void onReleasesEnd(Context context);

    void onEnabledStart(Context context);
    
    void onEnabledEnd(Context context);
    
    void onUpdatePolicyStart(Context context);
    
    void onUpdatePolicyEnd(Context context);

    void onChecksumPolicyStart(Context context);
    
    void onChecksumPolicyEnd(Context context);
    
    void onSnapshotsStart(Context context);
    
    void onSnapshotsEnd(Context context);
    
    void onNameStart(Context context);
    
    void onNameEnd(Context context);
    
    void onIdStart(Context context);
    
    void onIdEnd(Context context);
    
    void onUrlStart(Context context);
    
    void onUrlEnd(Context context);
    
    void onLayoutStart(Context context);
    
    void onLayoutEnd(Context context);
    
    void onRepositoryEnd(Context context);

    void onRepositoriesEnd(Context context);

    void onPluginRepositoriesStart(Context context);

    void onPluginRepositoryStart(Context context);

    void onPluginRepositoryEnd(Context context);

    void onPluginRepositoriesEnd(Context context);
    
	void onProjectEnd(Context context);
}
