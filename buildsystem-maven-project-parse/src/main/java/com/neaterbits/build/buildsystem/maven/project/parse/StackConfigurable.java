package com.neaterbits.build.buildsystem.maven.project.parse;

import com.neaterbits.build.buildsystem.maven.common.model.configuration.MavenConfiguration;
import com.neaterbits.build.buildsystem.maven.common.parse.configuration.ConfigurationSetter;
import com.neaterbits.build.buildsystem.maven.common.parse.configuration.StackBaseConfigurable;
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
