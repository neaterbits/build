package com.neaterbits.build.buildsystem.maven.project.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.build.buildsystem.maven.project.model.MavenReportPlugin;
import com.neaterbits.util.parse.context.Context;

final class StackReportingPlugins extends StackBase {

    private final List<MavenReportPlugin> plugins;
    
    public StackReportingPlugins(Context context) {
        super(context);
        
        this.plugins = new ArrayList<>();
    }

    void add(MavenReportPlugin plugin) {
        
        Objects.requireNonNull(plugin);
        
        plugins.add(plugin);
    }
    
    List<MavenReportPlugin> getPlugins() {
        return plugins;
    }
}
