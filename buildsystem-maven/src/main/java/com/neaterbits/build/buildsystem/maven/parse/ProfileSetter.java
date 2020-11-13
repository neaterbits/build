package com.neaterbits.build.buildsystem.maven.parse;

interface ProfileSetter 
    extends ModulesSetter,
            BuildSetter,
            RepositoriesSetter,
            PluginRepositoriesSetter,
            DependenciesSetter {
    
}
