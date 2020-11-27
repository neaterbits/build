package com.neaterbits.build.buildsystem.maven.plugins.execute;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Objects;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;

import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInfo;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginsEnvironment;
import com.neaterbits.build.buildsystem.maven.plugins.PluginExecutionException;
import com.neaterbits.build.buildsystem.maven.plugins.PluginFailureException;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.initialize.MojoInitializer;
import com.neaterbits.build.buildsystem.maven.plugins.instantiate.MavenPluginInstantiator;
import com.neaterbits.build.buildsystem.maven.plugins.instantiate.MojoFinder;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;

public final class MavenPluginsEnvironmentImpl implements MavenPluginsEnvironment {

    private final MavenPluginInstantiator pluginInstantiator;
    
    public MavenPluginsEnvironmentImpl(
            MavenPluginInstantiator pluginInstantiator) {
        
        Objects.requireNonNull(pluginInstantiator);
        
        this.pluginInstantiator = pluginInstantiator;
    }

    @Override
    public void executePluginGoal(
                MavenPluginInfo pluginInfo,
                MojoDescriptor mojoDescriptor,
                Collection<MavenProject> allProjects,
                String plugin,
                String goal,
                MavenProject module)
            throws PluginExecutionException, PluginFailureException, IOException {

        try {
            
            executePluginGoal(pluginInfo, mojoDescriptor, allProjects, pluginInstantiator, plugin, goal, module);
            
        } catch (MojoExecutionException ex) {
            throw new PluginExecutionException("Exception while executing", ex);
        } catch (MojoFailureException ex) {
            throw new PluginFailureException("Execution failure", ex);
        }
    }

    private static void executePluginGoal(
            MavenPluginInfo pluginInfo,
            MojoDescriptor mojoDescriptor,
            Collection<MavenProject> allProjects,
            MavenPluginInstantiator pluginInstantiator,
            String plugin,
            String goal,
            MavenProject module) throws IOException, MojoExecutionException, MojoFailureException {

        final ClassLoader classLoader = MojoFinder.makeClassLoader(pluginInfo);
        
        final Mojo mojo = pluginInstantiator.instantiate(pluginInfo, classLoader, plugin, goal);
        
        if (mojo == null) {
            throw new IllegalStateException("No mojo for plugin '" + plugin + "', goal '" + goal + "'");
        }

        final Class<? extends MojoInitializer> initializerClass;
        
        try {
            @SuppressWarnings("unchecked")
            final Class<? extends MojoInitializer> cl
                = (Class<? extends MojoInitializer>) classLoader.loadClass(MojoInitializer.class.getName());
            
            initializerClass = cl;
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException(ex);
        }
        
        try {
            initializerClass.getConstructor().newInstance().initializeMojo(mojo, mojoDescriptor, allProjects, module);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ExpressionEvaluationException ex) {
            throw new IllegalStateException(ex);
        }
        
        mojo.execute();
    }
}
