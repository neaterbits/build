package com.neaterbits.build.buildsystem.maven.project.parse;

import com.neaterbits.build.buildsystem.maven.common.parse.DependenciesSetter;

interface CommonSetter 
    extends ModulesSetter,
            BuildSetter,
            ReportingSetter,
            RepositoriesSetter,
            PluginRepositoriesSetter,
            DependencyManagementSetter,
            DependenciesSetter {
    
}
