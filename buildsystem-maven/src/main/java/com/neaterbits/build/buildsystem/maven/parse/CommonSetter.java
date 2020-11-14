package com.neaterbits.build.buildsystem.maven.parse;

interface CommonSetter 
    extends ModulesSetter,
            BuildSetter,
            ReportingSetter,
            RepositoriesSetter,
            PluginRepositoriesSetter,
            DependencyManagementSetter,
            DependenciesSetter {
    
}
