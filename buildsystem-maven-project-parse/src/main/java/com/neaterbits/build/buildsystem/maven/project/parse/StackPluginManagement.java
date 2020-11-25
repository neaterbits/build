package com.neaterbits.build.buildsystem.maven.project.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.build.buildsystem.maven.project.model.MavenBuildPlugin;
import com.neaterbits.util.parse.context.Context;

final class StackPluginManagement extends StackBase implements PluginsSetter {

    private List<MavenBuildPlugin> plugins;
    
    StackPluginManagement(Context context) {
        super(context);
    }

    List<MavenBuildPlugin> getPlugins() {
        return plugins;
    }

    @Override
    public void setPlugins(List<MavenBuildPlugin> plugins) {
        this.plugins = plugins;
    }
}
