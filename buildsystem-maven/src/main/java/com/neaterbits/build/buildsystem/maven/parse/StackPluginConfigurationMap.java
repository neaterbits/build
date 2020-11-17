package com.neaterbits.build.buildsystem.maven.parse;

import com.neaterbits.build.buildsystem.maven.elements.MavenPluginConfigurationMap;
import com.neaterbits.util.parse.context.Context;

final class StackPluginConfigurationMap extends StackConfigurationLevel {

    StackPluginConfigurationMap(Context context) {
        super(context, "configuration");
    }

    MavenPluginConfigurationMap getConfiguration() {
        return getObject();
    }
}
