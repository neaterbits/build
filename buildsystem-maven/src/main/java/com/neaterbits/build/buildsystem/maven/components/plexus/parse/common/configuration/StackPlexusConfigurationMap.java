package com.neaterbits.build.buildsystem.maven.components.plexus.parse.common.configuration;

import com.neaterbits.build.buildsystem.maven.components.plexus.elements.common.configuration.PlexusConfigurationMap;
import com.neaterbits.util.parse.context.Context;

final class StackPlexusConfigurationMap extends StackConfigurationLevel {

    StackPlexusConfigurationMap(Context context) {
        super(context, "configuration");
    }

    PlexusConfigurationMap getConfiguration() {
        return getObject();
    }
}
