package com.neaterbits.build.buildsystem.maven.plugins.builtin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;

public final class BuiltinPlugins {

    private static final List<MavenPlugin> BUILTIN_PLUGINS;
    
    private static final String GROUP_ID = "org.apache.maven.plugins";
    
    static {
        final List<MavenPlugin> builtinPlugins = new ArrayList<>();
        
        builtinPlugins.add(new MavenPlugin(GROUP_ID, "maven-compiler-plugin", "3.8.1"));
        builtinPlugins.add(new MavenPlugin(GROUP_ID, "maven-resources-plugin", "3.2.0"));
        builtinPlugins.add(new MavenPlugin(GROUP_ID, "maven-clean-plugin", "3.1.0"));
        builtinPlugins.add(new MavenPlugin(GROUP_ID, "maven-surefire-plugin", "3.0.0-M5"));
        builtinPlugins.add(new MavenPlugin(GROUP_ID, "maven-install-plugin", "3.0.0-M1"));
        builtinPlugins.add(new MavenPlugin(GROUP_ID, "maven-deploy-plugin", "3.0.0-M1"));
        
        BUILTIN_PLUGINS = Collections.unmodifiableList(builtinPlugins);
    }
    
    public static Collection<MavenPlugin> getPlugins() {
        return BUILTIN_PLUGINS;
    }
}
