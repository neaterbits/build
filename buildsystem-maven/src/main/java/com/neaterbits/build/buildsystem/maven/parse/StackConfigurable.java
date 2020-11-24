package com.neaterbits.build.buildsystem.maven.parse;

import com.neaterbits.build.buildsystem.maven.components.plexus.parse.common.configuration.ConfigurationSetter;
import com.neaterbits.build.buildsystem.maven.components.plexus.parse.common.configuration.StackBaseConfigurable;
import com.neaterbits.build.buildsystem.maven.elements.MavenConfiguration;
import com.neaterbits.util.parse.context.Context;

abstract class StackConfigurable
        extends StackBaseConfigurable
        implements InheritedSetter, ConfigurationSetter {

    private Boolean inherited;

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

    final MavenConfiguration makeMavenConfiguration() {
        return new MavenConfiguration(inherited, getConfiguration());
    }
}
