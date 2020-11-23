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
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;
import org.eclipse.aether.RepositorySystemSession;

import com.neaterbits.build.buildsystem.maven.container.PlexusContainerImpl;
import com.neaterbits.build.buildsystem.maven.plugins.convertmodel.ConvertModel;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoConfiguration;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoParameter;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoRequirement;

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

        try {
            initParameters(mojo, mojoDescriptor, mavenSession, mojoExecution);
        } catch (GetValueException ex) {
            throw new MojoExecutionException("Exception while initializing plugin value", ex);
        }
        
        final PlexusContainer plexusContainer = new PlexusContainerImpl();

        if (mojo instanceof Contextualizable) {
            
            final Context context = plexusContainer.getContext();
            
            context.put(PlexusConstants.PLEXUS_KEY, plexusContainer);
            
            try {
                ((Contextualizable)mojo).contextualize(context);
            } catch (ContextException ex) {
                throw new MojoExecutionException("Failed to contextualize mojo " + mojo.getClass(), ex);
            }
        }
        
        initComponents(mojo, mojoDescriptor, plexusContainer);
    }

    private void initParameters(
            Mojo mojo,
            MojoDescriptor mojoDescriptor,
            MavenSession mavenSession,
            MojoExecution mojoExecution) throws GetValueException {

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

                    setFieldValue(mojo, configuration.getParamName(), field -> {
                    
                        Object value;
                        
                        try {
                            value = expressionEvaluator.evaluate(configuration.getDefaultValue(), field.getType());
                        } catch (ExpressionEvaluationException ex) {
                            throw new GetValueException("Exception while evaluating value", ex);
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
    
    private static class GetValueException extends Exception {

        private static final long serialVersionUID = 1L;

        GetValueException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    @FunctionalInterface
    interface GetFieldValue {
        
        Object getFieldValue(Field field) throws GetValueException;
    }

    private void initComponents(
            Mojo mojo,
            MojoDescriptor mojoDescriptor,
            PlexusContainer container) throws MojoExecutionException {

        if (mojoDescriptor != null && mojoDescriptor.getRequirements() != null) {
            
            for (MojoRequirement requirement : mojoDescriptor.getRequirements()) {

                try {
                    final Object component = container.lookup(requirement.getRole(), requirement.getRoleHint());
                    
                    if (component != null) {
                        setFieldValue(mojo, requirement.getFieldName(), field -> component);
                    }
                } catch (GetValueException | ComponentLookupException ex) {
                    
                    throw new MojoExecutionException(
                            "Exception while initializing plugin component field '" 
                                        + requirement.getFieldName() + "' of " + mojo.getClass().getName(), ex);
                }
            }
        }
    }

    private static void setFieldValue(Mojo mojo, String fieldName, GetFieldValue getValue) throws GetValueException {
        
        final Field field;

        try {
            field = mojo.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException | SecurityException ex) {
            throw new IllegalStateException(ex);
        }
        
        if (field != null) {
            setFieldValue(mojo, field, getValue.getFieldValue(field));
        }
    }
    
    private static void setFieldValue(Mojo mojo, Field field, Object value) {
        
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
