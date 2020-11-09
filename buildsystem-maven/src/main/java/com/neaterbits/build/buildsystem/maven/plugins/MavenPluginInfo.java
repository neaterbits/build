package com.neaterbits.build.buildsystem.maven.plugins;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.maven.plugin.Mojo;

import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoDescriptor;

public final class MavenPluginInfo {

    private final MavenPluginDescriptor pluginDescriptor;
    private final Map<String, Class<? extends Mojo>> mojoImplementations;

    MavenPluginInfo(
            MavenPluginDescriptor pluginDescriptor,
            Map<String, Class<? extends Mojo>> mojoImplementations) {
        
        Objects.requireNonNull(pluginDescriptor);
        Objects.requireNonNull(mojoImplementations);

        if (pluginDescriptor.getMojos().size() != mojoImplementations.size()) {
            throw new IllegalArgumentException();
        }
        
        this.pluginDescriptor = pluginDescriptor;
        this.mojoImplementations = new HashMap<>(mojoImplementations);
    }

    public MavenPluginDescriptor getPluginDescriptor() {
        return pluginDescriptor;
    }
    
    public Mojo instantiate(MojoDescriptor mojoDescriptor) {

        final Class<? extends Mojo> implementationClass
            = mojoImplementations.get(mojoDescriptor.getImplementation());
        
        try {
            return implementationClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException ex) {

            throw new IllegalStateException(ex);
        }
    }
}
