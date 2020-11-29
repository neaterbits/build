package com.neaterbits.build.buildsystem.maven.plugins.execute;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;

import com.neaterbits.build.buildsystem.maven.di.componentsource.plexus.PlexusComponentsSourceLoader;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInfo;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginsEnvironment;
import com.neaterbits.build.buildsystem.maven.plugins.PluginExecutionException;
import com.neaterbits.build.buildsystem.maven.plugins.PluginFailureException;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.initialize.MojoInitializer;
import com.neaterbits.build.buildsystem.maven.plugins.instantiate.MavenPluginInstantiator;
import com.neaterbits.build.buildsystem.maven.plugins.instantiate.MavenPluginInstantiatorImpl;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.util.di.AmbiguousRequirementException;
import com.neaterbits.util.di.ClassLoaderScanner;
import com.neaterbits.util.di.Resolver;
import com.neaterbits.util.di.UnknownFieldException;
import com.neaterbits.util.di.UnresolvedRequirementException;
import com.neaterbits.util.di.UnsupportedTypeException;
import com.neaterbits.util.di.componentsource.ComponentsSourceException;
import com.neaterbits.util.di.componentsource.ComponentsSourceLoader;
import com.neaterbits.util.di.componentsource.jsr330.JSR330ClassComponentsSourceLoader;

public final class MavenPluginsEnvironmentImpl implements MavenPluginsEnvironment {

    private final MavenPluginInstantiator pluginInstantiator;
    
    public MavenPluginsEnvironmentImpl() {
        this(new MavenPluginInstantiatorImpl());
    }
    
    public MavenPluginsEnvironmentImpl(MavenPluginInstantiator pluginInstantiator) {
        
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
                MavenProject module,
                URLClassLoader classLoader)
            throws PluginExecutionException, PluginFailureException, IOException {

        try {
            
            executePluginGoal(pluginInfo, mojoDescriptor, allProjects, pluginInstantiator, plugin, goal, module, classLoader);
            
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
            MavenProject module,
            URLClassLoader classLoader) throws IOException, MojoExecutionException, MojoFailureException {

        final List<ComponentsSourceLoader<?>> loaders = Arrays.asList(
                new PlexusComponentsSourceLoader(),
                new JSR330ClassComponentsSourceLoader());
        
        final ClassLoaderScanner classLoaderScanner = new ClassLoaderScanner();
        
        final Resolver resolver;
        
        try {
            resolver = classLoaderScanner.scan(classLoader, loaders);
        } catch (ClassNotFoundException | UnsupportedTypeException | UnknownFieldException
                | UnresolvedRequirementException | AmbiguousRequirementException | IOException
                | ComponentsSourceException ex) {

            throw new MojoExecutionException("Could not resolve components", ex);
        }
        
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
            initializerClass.getConstructor().newInstance().initializeMojo(mojo, mojoDescriptor, allProjects, module, classLoader, resolver);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ExpressionEvaluationException ex) {
            throw new IllegalStateException(ex);
        }
        
        mojo.execute();
    }
}
