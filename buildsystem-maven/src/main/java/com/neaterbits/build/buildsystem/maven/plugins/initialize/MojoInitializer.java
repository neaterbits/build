package com.neaterbits.build.buildsystem.maven.plugins.initialize;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;

import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.DefaultMavenExecutionResult;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.PluginParameterExpressionEvaluator;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.PlexusConstants;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;
import org.eclipse.aether.RepositorySystemSession;

import com.neaterbits.build.buildsystem.maven.container.PlexusContainerImpl;
import com.neaterbits.build.buildsystem.maven.plugins.convertmodel.ConvertModel;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoConfiguration;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoParameter;

public class MojoInitializer {

    private static <T> T createProxy(Class<T> type) {
        
        final Object instance = Proxy.newProxyInstance(
                MojoInitializer.class.getClassLoader(),
                new Class [] { type },
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        throw new UnsupportedOperationException("method " + method.getName());
                    }
                });
                
        @SuppressWarnings("unchecked")
        final T result = (T)instance;

        return result;
    }
    
    public void initializeMojo(
            Mojo mojo,
            MojoDescriptor mojoDescriptor,
            Collection<com.neaterbits.build.buildsystem.maven.elements.MavenProject> allModules,
            com.neaterbits.build.buildsystem.maven.elements.MavenProject module) throws ExpressionEvaluationException, MojoExecutionException {
        
        final PlexusContainer container = createProxy(PlexusContainer.class);
        
        final RepositorySystemSession repositorySystemSession = createProxy(RepositorySystemSession.class);
        
        @SuppressWarnings({ "deprecation" })
        final MavenSession mavenSession = new MavenSession(
                container,
                repositorySystemSession,
                new DefaultMavenExecutionRequest(),
                new DefaultMavenExecutionResult());
        
        final MavenProject currentProject = ConvertModel.convertProject(module);
        
        mavenSession.setCurrentProject(currentProject);
        
        final MojoExecution mojoExecution = new MojoExecution(null);

        initParameters(mojo, mojoDescriptor, mavenSession, mojoExecution);
        
        if (mojo instanceof Contextualizable) {
            
            final PlexusContainer plexusContainer = new PlexusContainerImpl();
            
            final Context context = plexusContainer.getContext();
            
            context.put(PlexusConstants.PLEXUS_KEY, plexusContainer);
            
            try {
                ((Contextualizable)mojo).contextualize(context);
            } catch (ContextException ex) {
                throw new MojoExecutionException("Failed to contextualize mojo " + mojo.getClass(), ex);
            }
        }
    }

    private void initParameters(
            Mojo mojo,
            MojoDescriptor mojoDescriptor,
            MavenSession mavenSession,
            MojoExecution mojoExecution) throws ExpressionEvaluationException {

        final PluginParameterExpressionEvaluator expressionEvaluator = new PluginParameterExpressionEvaluator(mavenSession, mojoExecution);

        if (mojoDescriptor != null && mojoDescriptor.getParameters() != null) {
         
            for (MojoParameter mojoParameter : mojoDescriptor.getParameters()) {
                
                final MojoConfiguration configuration = mojoDescriptor.getConfigurations() == null
                        ? null
                        : mojoDescriptor.getConfigurations().stream()
                            .filter(c -> c.getParamName().equals(mojoParameter.getName()))
                            .findFirst()
                            .orElse(null);
                
                if (configuration != null && configuration.getDefaultValue() != null) {
                    
                    final Field field;

                    try {
                        field = mojo.getClass().getDeclaredField(configuration.getParamName());
                    } catch (NoSuchFieldException | SecurityException ex) {
                        throw new IllegalStateException(ex);
                    }
                    
                    if (field != null) {
                        Object value = expressionEvaluator.evaluate(configuration.getDefaultValue(), field.getType());
                        
                        if (field.getType().equals(boolean.class) && value instanceof String) {
                            value = Boolean.getBoolean((String)value);
                        }

                        final boolean fieldAccessible = field.canAccess(mojo);

                        if (!fieldAccessible) {
                            field.setAccessible(true);
                        }

                        try {
                            field.set(mojo, value);
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                            throw new IllegalStateException(ex);
                        }
                        finally {
                            if (!fieldAccessible) {
                                field.setAccessible(false);
                            }
                        }
                    }
                }
            }
        }
    }
}
