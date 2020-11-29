package com.neaterbits.build.buildsystem.maven.plugins;

import java.net.URLClassLoader;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.repositoryaccess.MavenRepositoryAccess;

@FunctionalInterface
public interface PluginsEnvironmentProvider {
    
    public static final class Result {
        
        private final MavenPluginsEnvironment environment;
        private final URLClassLoader classLoader;
        
        public Result(MavenPluginsEnvironment environment, URLClassLoader classLoader) {
            
            Objects.requireNonNull(environment);
            Objects.requireNonNull(classLoader);
            
            this.environment = environment;
            this.classLoader = classLoader;
        }

        public MavenPluginsEnvironment getEnvironment() {
            return environment;
        }

        public URLClassLoader getClassLoader() {
            return classLoader;
        }
    }

    Result provide(
            MavenPluginInfo pluginInfo,
            MavenDependency executePluginDependency,
            MavenRepositoryAccess repositoryAccess);
}
