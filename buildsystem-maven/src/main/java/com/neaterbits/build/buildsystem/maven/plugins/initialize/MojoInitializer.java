package com.neaterbits.build.buildsystem.maven.plugins.initialize;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;

import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.DefaultMavenExecutionResult;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.Mojo;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;
import org.eclipse.aether.RepositorySystemSession;

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
            Collection<com.neaterbits.build.buildsystem.maven.elements.MavenProject> allModules,
            com.neaterbits.build.buildsystem.maven.elements.MavenProject module) throws ExpressionEvaluationException {
        
        final PlexusContainer container = createProxy(PlexusContainer.class);
        
        final RepositorySystemSession repositorySystemSession = createProxy(RepositorySystemSession.class);
        
        @SuppressWarnings({ "deprecation", "unused" })
        final MavenSession mavenSession = new MavenSession(
                container,
                repositorySystemSession,
                new DefaultMavenExecutionRequest(),
                new DefaultMavenExecutionResult());
    }
}
