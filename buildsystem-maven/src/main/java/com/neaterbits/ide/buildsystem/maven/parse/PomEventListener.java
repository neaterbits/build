package com.neaterbits.ide.buildsystem.maven.parse;

import com.neaterbits.compiler.util.Context;

public interface PomEventListener {

	void onProjectStart(Context context);
	
	void onGroupIdStart(Context context);
	
	void onGroupIdEnd(Context context);
	
	void onArtifactIdStart(Context context);
	
	void onArtifactIdEnd(Context context);
	
	void onVersionStart(Context context);
	
	void onVersionEnd(Context context);

	void onParentStart(Context context);
	
	void onParentEnd(Context context);
	
	void onDependenciesStart(Context context);
	
	void onDependencyStart(Context context);
	
	void onScopeStart(Context context);
	
	void onScopeEnd(Context context);
	
	void onOptionalStart(Context context);
	
	void onOptionalEnd(Context context);
	
	void onDependencyEnd(Context context);
	
	void onDependenciesEnd(Context context);
	
	void onModulesStart(Context context);
	
	void onModuleStart(Context context);
	
	void onModuleEnd(Context context);
	
	void onModulesEnd(Context context);
	
	void onReportingStart(Context context);
	
	void onReportingEnd(Context context);
	
	void onBuildStart(Context context);
	
	void onPluginsStart(Context context);
	
	void onPluginStart(Context context);

	void onPluginEnd(Context context);
	
	void onPluginsEnd(Context context);
	
	void onExtensionsStart(Context context);
	
	void onExtensionStart(Context context);
	
	void onExtensionEnd(Context context);
	
	void onExtensionsEnd(Context context);
	
	void onBuildEnd(Context context);
	
	void onProjectEnd(Context context);

	void onText(Context context, String text);
}
