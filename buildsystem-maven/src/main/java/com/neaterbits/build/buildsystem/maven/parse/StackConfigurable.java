package com.neaterbits.build.buildsystem.maven.parse;

import com.neaterbits.build.buildsystem.maven.elements.MavenPluginConfiguration;
import com.neaterbits.build.buildsystem.maven.elements.MavenPluginConfigurationMap;
import com.neaterbits.util.parse.context.Context;

abstract class StackConfigurable
        extends StackBase
        implements InheritedSetter, ConfigurationSetter {

    private Boolean inherited;

    private MavenPluginConfigurationMap configurationMap;

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

    final MavenPluginConfigurationMap getConfiguration() {
        return configurationMap;
    }

    @Override
    public final void setConfiguration(MavenPluginConfigurationMap configuration) {
        this.configurationMap = configuration;
    }

    final MavenPluginConfiguration makeConfiguration() {
        return new MavenPluginConfiguration(inherited, configurationMap);
    }
}
