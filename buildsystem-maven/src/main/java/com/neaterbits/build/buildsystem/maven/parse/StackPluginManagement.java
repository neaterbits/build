package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;
import com.neaterbits.util.parse.context.Context;

final class StackPluginManagement extends StackBase implements PluginsSetter {

    private List<MavenPlugin> plugins;
    
    StackPluginManagement(Context context) {
        super(context);
    }

    List<MavenPlugin> getPlugins() {
        return plugins;
    }

    @Override
    public void setPlugins(List<MavenPlugin> plugins) {
        this.plugins = plugins;
    }
}
