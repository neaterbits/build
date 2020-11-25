package com.neaterbits.build.buildsystem.maven.common.parse.configuration;

import com.neaterbits.build.buildsystem.maven.common.model.configuration.PlexusConfigurationMap;
import com.neaterbits.util.parse.context.Context;

final class StackPlexusConfigurationMap extends StackConfigurationLevel {

    StackPlexusConfigurationMap(Context context) {
        super(context, "configuration");
    }

    PlexusConfigurationMap getConfiguration() {
        return getObject();
    }
}
