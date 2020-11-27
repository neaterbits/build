package com.neaterbits.build.buildsystem.maven.plugins.instantiate;

import java.lang.reflect.InvocationTargetException;

import org.apache.maven.plugin.Mojo;

import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInfo;
import com.neaterbits.build.buildsystem.maven.plugins.access.PluginFinder;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.instantiate.MojoFinder.LoadedClasses;

public class MavenPluginInstantiatorImpl implements MavenPluginInstantiator {

    @Override
    public Mojo instantiate(MavenPluginInfo pluginInfo, ClassLoader classLoader, String plugin, String goal) {

        final LoadedClasses loadedClasses = MojoFinder.loadClasses(pluginInfo, classLoader); 
        
        final MavenPluginDescriptor pluginDescriptor = pluginInfo.getPluginDescriptor();

        final MojoDescriptor mojoDescriptor = PluginFinder.findMojoDescriptor(pluginDescriptor, plugin, goal);
        
        return instantiate(mojoDescriptor, loadedClasses);
    }

    private Mojo instantiate(MojoDescriptor mojoDescriptor, LoadedClasses mojoImplementations) {

        final Class<? extends Mojo> implementationClass
            = mojoImplementations.getMojoClasses().get(mojoDescriptor.getImplementation());
        
        final Mojo mojo;
        
        try {
            mojo = implementationClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException ex) {

            throw new IllegalStateException(ex);
        }

        return mojo;
    }
}
