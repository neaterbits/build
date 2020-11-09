package com.neaterbits.build.buildsystem.maven.plugins;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.maven.plugin.Mojo;

import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoDescriptor;

final class MavenDescriptorPluginInfo implements MavenPluginInfo {

    private final MavenPluginDescriptor pluginDescriptor;
    private final Map<String, Class<? extends Mojo>> mojoImplementations;

    MavenDescriptorPluginInfo(
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
    
    @Override
    public Mojo instantiate(String plugin, String goal) {
        
        final MojoDescriptor mojoDescriptor = getPluginDescriptor().getMojos().stream()
                .filter(mj -> PluginFinder.isMojoForGoal(getPluginDescriptor(), mj, plugin, goal))
                .findFirst()
                .orElse(null);

        return instantiate(mojoDescriptor);
    }

    private Mojo instantiate(MojoDescriptor mojoDescriptor) {

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
