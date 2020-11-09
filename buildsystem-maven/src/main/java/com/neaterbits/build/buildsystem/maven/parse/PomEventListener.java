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
}
