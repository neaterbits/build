package com.neaterbits.build.strategies.common;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.build.buildsystem.common.Scope;
import com.neaterbits.build.common.tasks.ModuleBuilderUtil;
import com.neaterbits.build.types.dependencies.LibraryDependency;
import com.neaterbits.build.types.dependencies.ProjectDependency;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;

public class StrategiesBuilderUtil {

    public static List<ProjectDependency> transitiveProjectDependencies(TaskBuilderContext context, ProjectModuleResourcePath module) {

        return ModuleBuilderUtil.transitiveProjectDependencies(context.getBuildRoot(), module);
    }

    public static List<LibraryDependency> transitiveProjectExternalDependencies(TaskBuilderContext context, ProjectModuleResourcePath module) {

        final List<LibraryDependency> downloadedDependencies = new ArrayList<>();

        // In same pom file
        downloadedDependencies.addAll(context.getBuildRoot().getLibraryDependenciesForProjectModule(module));

        // Transitive from module dependencies
        final List<ProjectDependency> moduleDependencies = transitiveProjectDependencies(context, module);
        final List<LibraryDependency> moduleExternalDependencies = moduleDependencies.stream()
                .flatMap(projectDependency -> context.getBuildRoot().getLibraryDependenciesForProjectModule(projectDependency.getModulePath()).stream())
                .collect(Collectors.toList());
        
        downloadedDependencies.addAll(moduleExternalDependencies);
        
        final List<LibraryDependency> allDependencies = new ArrayList<>(downloadedDependencies);
        
        // Transitive from all downloaded
        for (LibraryDependency externalDependency : downloadedDependencies) {
            transitiveDependencies(context, externalDependency, allDependencies);
        }

        return allDependencies;
    }

    static List<LibraryDependency> transitiveLibraryDependencies(TaskBuilderContext context, LibraryDependency module) {
        
        final List<LibraryDependency> dependencies = new ArrayList<>();
        
        transitiveDependencies(context, module, dependencies);

        return dependencies;
    }

    private static void transitiveDependencies(TaskBuilderContext context, LibraryDependency dependency, List<LibraryDependency> dependencies) {

        final List<LibraryDependency> moduleDependencies;

        moduleDependencies = context.getBuildRoot().getDependenciesForExternalLibrary(dependency, Scope.COMPILE, false);
         
        dependencies.addAll(moduleDependencies);

        for (LibraryDependency foundDep : moduleDependencies) {
            try {
                transitiveDependencies(context, foundDep, dependencies);
            }
            catch (Exception ex) {
                throw new IllegalStateException("Exception while getting dependencies for " + dependency, ex);
            }
        }
    }
}
