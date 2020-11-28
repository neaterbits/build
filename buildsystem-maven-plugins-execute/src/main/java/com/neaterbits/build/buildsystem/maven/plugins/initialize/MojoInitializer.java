package com.neaterbits.build.buildsystem.maven.plugins.initialize;

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

import com.neaterbits.build.buildsystem.maven.common.model.configuration.MavenConfiguration;
import com.neaterbits.build.buildsystem.maven.container.PlexusContainerImpl;
import com.neaterbits.build.buildsystem.maven.plugins.convertmodel.ConvertModel;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoConfiguration;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoParameter;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoRequirement;
import com.neaterbits.util.di.Resolver;

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
            Collection<com.neaterbits.build.buildsystem.maven.project.model.MavenProject> allModules,
            com.neaterbits.build.buildsystem.maven.project.model.MavenProject module,
            ClassLoader classLoader,
            Resolver resolver) throws ExpressionEvaluationException, MojoExecutionException {
        
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

        final MavenConfiguration pluginConfiguration = null;

        final MojoExecutionContext mojoExecutionContext = new MojoExecutionContext(mojo);

        try {
            initParameters(mojoExecutionContext, mojoDescriptor, pluginConfiguration, mavenSession, mojoExecution);
        } catch (MojoInitializeException ex) {
            throw new MojoExecutionException("Exception while initializing plugin value", ex);
        }
        
        try {
            initComponents(mojoExecutionContext, mojoDescriptor, classLoader, resolver);
        } catch (ClassNotFoundException ex) {
            throw new MojoExecutionException("Could not resolve", ex);
        }

        final PlexusContainer plexusContainer = new PlexusContainerImpl(resolver.getComponents());

        if (mojo instanceof Contextualizable) {
            
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
            MojoExecutionContext context,
            MojoDescriptor mojoDescriptor,
            MavenConfiguration pluginConfiguration,
            MavenSession mavenSession,
            MojoExecution mojoExecution) throws MojoInitializeException {

        final PluginParameterExpressionEvaluator expressionEvaluator = new PluginParameterExpressionEvaluator(mavenSession, mojoExecution);

        if (mojoDescriptor != null && mojoDescriptor.getParameters() != null) {
         
            for (MojoParameter mojoParameter : mojoDescriptor.getParameters()) {

                final boolean applied = Configurer.applyConfiguration(context, mojoParameter, pluginConfiguration);

                if (!applied) {
                    final MojoConfiguration configuration = mojoDescriptor.getConfigurations() == null
                            ? null
                            : mojoDescriptor.getConfigurations().stream()
                                .filter(c -> c.getParamName().equals(mojoParameter.getName()))
                                .findFirst()
                                .orElse(null);
                    
                    
                    if (configuration != null && configuration.getDefaultValue() != null) {
    
                        MojoFieldUtil.setFieldValue(
                                context,
                                context.getMojo(),
                                configuration.getParamName(),
                                mojoParameter.getType(),
                                field -> {
                        
                            Object value;
                            
                            try {
                                value = expressionEvaluator.evaluate(configuration.getDefaultValue(), field.getType());
                            } catch (ExpressionEvaluationException ex) {
                                throw new ValueFormatException(context, "Exception while evaluating value" + mojoParameter.getName(), ex);
                            }
                            
                            if (field.getType().equals(boolean.class) && value instanceof String) {
                                value = Boolean.getBoolean((String)value);
                            }
                            
                            return value;
                        });
                    }
                }
            }
        }
    }

    private void initComponents(
            MojoExecutionContext context,
            MojoDescriptor mojoDescriptor,
            ClassLoader classLoader,
            Resolver resolver) throws MojoExecutionException, ClassNotFoundException {

        if (mojoDescriptor != null && mojoDescriptor.getRequirements() != null) {
            
            for (MojoRequirement requirement : mojoDescriptor.getRequirements()) {

                final Class<?> roleClass = classLoader.loadClass(requirement.getRole());
                
                try {
                    final Object component = resolver.instantiate(roleClass, requirement.getRoleHint());
                    
                    if (component != null) {
                        MojoFieldUtil.setFieldValue(
                                context,
                                context.getMojo(),
                                requirement.getFieldName(),
                                null,
                                field -> component);
                    }
                } catch (MojoInitializeException ex) {
                    
                    throw new MojoExecutionException(
                            "Exception while initializing plugin component field '" 
                                        + requirement.getFieldName() + "' of " + context.getDebugName(), ex);
                }
            }
        }
    }
}
