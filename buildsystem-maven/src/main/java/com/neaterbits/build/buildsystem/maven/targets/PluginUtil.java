package com.neaterbits.build.buildsystem.maven.targets;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;

import com.neaterbits.build.buildsystem.maven.MavenBuildRoot;
import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.elements.MavenPluginRepository;
import com.neaterbits.build.buildsystem.maven.elements.MavenProject;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInfo;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInstantiator;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginsAccess;
import com.neaterbits.build.buildsystem.maven.plugins.MojoFinder;
import com.neaterbits.build.buildsystem.maven.plugins.PluginFinder;
import com.neaterbits.build.buildsystem.maven.plugins.initialize.MojoInitializer;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.ActionLog;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.FunctionActionLog;

class PluginUtil {

    static void executePluginGoal(
            MavenBuildRoot mavenBuildRoot,
            String plugin,
            String goal,
            MavenProject module) throws IOException, MojoExecutionException, MojoFailureException {
        
        final MavenPluginInstantiator pluginInstantiator = mavenBuildRoot.getPluginInstantiator();
        final MavenPluginsAccess pluginsAccess = mavenBuildRoot.getPluginsAccess();

        final MavenPlugin mavenPlugin = PluginFinder.getPlugin(module, plugin);
        
        if (mavenPlugin == null) {
            throw new IllegalStateException("No plugin '" + plugin + "'");
        }
        
        final MavenPluginInfo pluginInfo = pluginsAccess.getPluginInfo(mavenPlugin);
        
        if (pluginInfo == null) {
            throw new IllegalStateException("No plugin info for " + mavenPlugin);
        }

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
            initializerClass.getConstructor().newInstance().initializeMojo(mojo, mavenBuildRoot.getProjects(), module);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ExpressionEvaluationException ex) {
            throw new IllegalStateException(ex);
        }
        
        mojo.execute();
    }

    static ActionLog downloadPlugin(
            MavenBuildRoot mavenBuildRoot,
            MavenPlugin mavenPlugin,
            List<MavenPluginRepository> pluginRepositories) {
        
        ActionLog actionLog;
        
        try {
            mavenBuildRoot.downloadPluginIfNotPresent(mavenPlugin, pluginRepositories);
            
            actionLog = FunctionActionLog.OK;
            
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
        
        return actionLog;
    }
}
