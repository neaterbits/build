package com.neaterbits.build.buildsystem.maven.parse;

import com.neaterbits.build.buildsystem.maven.elements.MavenConfiguration;
import com.neaterbits.build.buildsystem.maven.elements.MavenConfigurationMap;
import com.neaterbits.util.parse.context.Context;

abstract class StackConfigurable
        extends StackBase
        implements InheritedSetter, ConfigurationSetter {

    private Boolean inherited;

    private MavenConfigurationMap configurationMap;

    StackConfigurable(Context context) {
        super(context);
    }

    final Boolean getInherited() {
        return inherited;
    }

    @Override
    public final void setInherited(Boolean inherited) {
        this.inherited = inherited;
    }

    final MavenConfigurationMap getConfiguration() {
        return configurationMap;
    }

    @Override
    public final void setConfiguration(MavenConfigurationMap configuration) {
        this.configurationMap = configuration;
    }

    final MavenConfiguration makeConfiguration() {
        return new MavenConfiguration(inherited, configurationMap);
    }
}
