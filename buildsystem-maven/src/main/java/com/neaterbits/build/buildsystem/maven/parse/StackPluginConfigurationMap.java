package com.neaterbits.build.buildsystem.maven.parse;

import com.neaterbits.build.buildsystem.maven.elements.MavenConfigurationMap;
import com.neaterbits.util.parse.context.Context;

final class StackPluginConfigurationMap extends StackConfigurationLevel {

    StackPluginConfigurationMap(Context context) {
        super(context, "configuration");
    }

    MavenConfigurationMap getConfiguration() {
        return getObject();
    }
}
