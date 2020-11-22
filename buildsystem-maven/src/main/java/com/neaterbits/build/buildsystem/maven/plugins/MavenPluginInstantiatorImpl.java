package com.neaterbits.build.buildsystem.maven.plugins;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.maven.plugin.Mojo;

import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoDescriptor;

public class MavenPluginInstantiatorImpl implements MavenPluginInstantiator {

    @Override
    public Mojo instantiate(MavenPluginInfo pluginInfo, String plugin, String goal) {

        final Map<String, Class<? extends Mojo>> map = MojoFinder.loadClasses(pluginInfo); 
        
        final MavenPluginDescriptor pluginDescriptor = pluginInfo.getPluginDescriptor();
        
        final MojoDescriptor mojoDescriptor = pluginDescriptor.getMojos().stream()
                .filter(mj -> PluginFinder.isMojoForGoal(pluginDescriptor, mj, plugin, goal))
                .findFirst()
                .orElse(null);

        return instantiate(mojoDescriptor, map);
    }

    private Mojo instantiate(MojoDescriptor mojoDescriptor, Map<String, Class<? extends Mojo>> mojoImplementations) {

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
