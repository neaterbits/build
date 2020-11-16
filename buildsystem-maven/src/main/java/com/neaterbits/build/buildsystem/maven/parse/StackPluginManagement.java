package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenConfiguredPlugin;
import com.neaterbits.util.parse.context.Context;

final class StackPluginManagement extends StackBase implements PluginsSetter {

    private List<MavenConfiguredPlugin> plugins;
    
    StackPluginManagement(Context context) {
        super(context);
    }

    List<MavenConfiguredPlugin> getPlugins() {
        return plugins;
    }

    @Override
    public void setPlugins(List<MavenConfiguredPlugin> plugins) {
        this.plugins = plugins;
    }
}
