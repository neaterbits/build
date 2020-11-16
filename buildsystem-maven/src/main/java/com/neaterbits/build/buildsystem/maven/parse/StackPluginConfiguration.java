package com.neaterbits.build.buildsystem.maven.parse;

import com.neaterbits.build.buildsystem.maven.elements.MavenPluginConfiguration;
import com.neaterbits.util.parse.context.Context;

final class StackPluginConfiguration extends StackConfigurationLevel {

    StackPluginConfiguration(Context context) {
        super(context, "configuration");
    }

    MavenPluginConfiguration getConfiguration() {
        return getObject();
    }
}
