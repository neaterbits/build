package com.neaterbits.build.buildsystem.maven.targets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.build.buildsystem.maven.CachedDependencies;
import com.neaterbits.build.buildsystem.maven.EffectiveProject;
import com.neaterbits.build.buildsystem.maven.MavenBuildRoot;
import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.effective.EffectivePOMReader;
import com.neaterbits.build.buildsystem.maven.project.model.BaseMavenRepository;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.util.Indent;

class DepsHelper {

    static List<ProjectDependency> externalDependencies(
            MavenBuildRoot buildRoot,
            CachedDependencies cached,
            MavenProject sourceProject,
            List<BaseMavenRepository> repositories) {
        
        if (!buildRoot.isProjectModule(sourceProject)) {
            throw new IllegalArgumentException();
        }
        
        final List<ProjectDependency> result = sourceProject.getCommon().getDependencies() != null
                
                ? sourceProject.getCommon().getDependencies().stream()
                        .map(dep -> new InitialTransitiveDependency(
                                                sourceProject,
                                                sourceProject.getCommon().getRepositories()
                                                    .stream()
                                                    .collect(Collectors.toList()),
                                                dep.getModuleId()))
                        .collect(Collectors.toList())
                        
                : Collections.emptyList();

        if (result.stream().anyMatch(dep -> !buildRoot.isProjectModule(dep.getOriginalDependency()))) {
            throw new IllegalStateException();
        }

        return result;
    }

    static List<ProjectDependency> listOfFurtherDependencies(
            int indent,
            boolean debug,
            MavenBuildRoot buildRoot,
            CachedDependencies cached,
            ProjectDependency projectDependency) {

        if (debug) {
            System.out.println(Indent.indent(indent)
                    + "## ENTER listOfFurtherDependencies(" + projectDependency.getTargetedDependency().getId() + ")");
            
            for (MavenProject proj : projectDependency.getDependencyPath()) {
                System.out.println(Indent.indent(indent + 1) + "## path " + proj.getModuleId().getId());     
            }
        }

        // List of further dependencies from last external pom file, prioritise parent pom files
        // before other dependencies because the parent dependencies may
        // specify repositories etc that are needed to fetch the pom's further dependencies

        final MavenModuleId moduleId = projectDependency.getTargetedDependency();
        
        final MavenProject project = cached.getExternalProject(moduleId);

        final List<ProjectDependency> result;

        if (project == null) {

            if (debug) {
                System.out.println(Indent.indent(indent + 1) + "## no project for " + moduleId);
            }
            
            // Parent dependency not downloaded yet so return empty list
            result = Collections.emptyList();
        }
        else if (projectDependency instanceof ParentDependency) {

            final ParentDependency parentDependency = (ParentDependency)projectDependency;

            if (project.getParent() != null) {

                if (debug) {
                    System.out.println(Indent.indent(indent + 1) + "## ParentDependency with parent " + moduleId);
                }

                final ParentDependency nextDependency = new ParentDependency(
                        parentDependency,
                        project,
                        parentDependency.getReferencedFromRepositories(),
                        project.getParentModuleId());
                
                result = Arrays.asList(nextDependency);
            }
            else {
                
                if (debug) {
                    System.out.println(Indent.indent(indent + 1) + "## ParentDependency without parent " + moduleId);
                }

                // Reached entry with no parent dependency, find all transitive
                
                result = onDependencyRootReached(indent + 1, debug, buildRoot, project, parentDependency, cached);
            }
        }
        else if (projectDependency instanceof TransitiveDependency) {
            
            final TransitiveDependency transitiveDependency = (TransitiveDependency)projectDependency;

            if (project.getParent() != null) {

                if (debug) {
                    System.out.println(Indent.indent(indent + 1) + "## TransitiveDependency with parent " + moduleId);
                }
                
                final ParentDependency nextDependency = new ParentDependency(
                        transitiveDependency,
                        project,
                        transitiveDependency.getReferencedFromRepositories(),
                        project.getParentModuleId());
                
                result = Arrays.asList(nextDependency);
            }
            else {

                if (debug) {
                    System.out.println(Indent.indent(indent + 1) + "## TransitiveDependency without parent " + moduleId);
                }
                
                if (project.getCommon().getDependencies() != null) {
                    
                    result = new ArrayList<>(project.getCommon().getDependencies().size());
                    
                    final EffectiveProject effective = getEffectiveExternalProject(cached, project);
                    
                    addAnyTransitiveDependencies(
                            indent,
                            debug,
                            buildRoot,
                            result,
                            effective,
                            projectDependency,
                            project);
                }
                else {
                    // No further dependencies
                    result = Collections.emptyList();
                }
            }
        }
        else {
            throw new IllegalStateException();
        }
 
        if (result.stream().anyMatch(dep -> !buildRoot.isProjectModule(dep.getOriginalDependency()))) {
            throw new IllegalStateException();
        }

        if (debug) {
            System.out.println(Indent.indent(indent)
                    + "## EXIT listOfFurtherDependencies(" + projectDependency.getTargetedDependency().getId() + ")");
        }
        
        return result;
    }
    
    private static EffectiveProject getEffectiveExternalProject(
                                    CachedDependencies cached,
                                    MavenProject project) {
        
        final MavenModuleId moduleId = EffectivePOMReader.getProjectModuleId(project);
        
        return cached.getEffectiveExternalProject(moduleId);
    }
    
    private static List<ProjectDependency> onDependencyRootReached(
                                                int indent,
                                                boolean debug,
                                                MavenBuildRoot buildRoot,
                                                MavenProject project,
                                                ParentDependency parentDependency,
                                                CachedDependencies cached) {
        
        if (debug) {
            System.out.println(Indent.indent(indent) + "## ENTER onDependencyRootReached(" + project.getModuleId().getId() + ")");
        }
        
        final List<ProjectDependency> result = new ArrayList<>();
        
        if (project.getParent() != null) {
            throw new IllegalArgumentException();
        }
        
        if (!parentDependency.getThisParentDependency().equals(project.getModuleId())) {
            throw new IllegalArgumentException();
        }

        if (project.getCommon().getDependencies() != null) {

            System.out.println(Indent.indent(indent)
                        + "## onDependencyRootReached transitive in project " + project.getModuleId());
            
            final EffectiveProject effective = getEffectiveExternalProject(cached, project);
            
            addAnyTransitiveDependencies(
                    indent + 1,
                    debug,
                    buildRoot,
                    result,
                    effective,
                    parentDependency,
                    project);
        }
        
        // Up to first transitive
        for (ProjectDependency dep = parentDependency.getReferencedFrom();
                dep != null;
                dep = dep.getReferencedFrom()) {
            
            final MavenProject depProject = cached.getExternalProject(dep.getTargetedDependency());

            if (dep instanceof ParentDependency) {
                
                final ParentDependency parentDep = (ParentDependency)dep;

                System.out.println(Indent.indent(indent) + "## onDependencyRootReached parent " + dep.getTargetedDependency());

                final EffectiveProject effective = getEffectiveExternalProject(cached, depProject);
        
                addAnyTransitiveDependencies(
                        indent + 1,
                        debug,
                        buildRoot,
                        result,
                        effective,
                        parentDep,
                        depProject);
            }
            else if (dep instanceof TransitiveDependency) {

                final TransitiveDependency transitiveDependency = (TransitiveDependency)dep;
                
                System.out.println(Indent.indent(indent) + "## onDependencyRootReached transitive " + dep.getTargetedDependency());
                
                final EffectiveProject effective = getEffectiveExternalProject(cached, depProject);
                
                if (effective.getDependencies() != null && !effective.getDependencies().isEmpty()) {
                    System.out.println(Indent.indent(indent)
                            + "## onDependencyRootReached dependencies in transitive for "
                            + effective.getModuleId());
                    
                    addAnyTransitiveDependencies(
                            indent + 1,
                            debug,
                            buildRoot,
                            result,
                            effective,
                            transitiveDependency,
                            depProject);
                }
                
                // Only scan to transitive, where effective pom resolved from parent poms
                break;
            }
        }
        
        if (debug) {
            System.out.println(
                      Indent.indent(indent)
                    + "## EXIT onDependencyRootReached(" + project.getModuleId().getId() + ")");
        }
        
        return result;
    }
    
    private static void addAnyTransitiveDependencies(
            int indent,
            boolean debug,
            MavenBuildRoot root,
            List<ProjectDependency> result,
            EffectiveProject effectiveProject,
            ProjectDependency referencedFrom,
            MavenProject projectReferencedFrom) {
        
        if (debug) {
            System.out.println(Indent.indent(indent)
                    + "## ENTER addAnyTransitiveDependencies("
                    + referencedFrom.getTargetedDependency().getId() + ")");
        }

        if (!root.isProjectModule(referencedFrom.getOriginalDependency())) {
            throw new IllegalStateException();
        }

        if (effectiveProject.getDependencies() != null) {
            
            for (MavenDependency dependency : effectiveProject.getDependencies()) {
                
                if (debug) {
                    System.out.println(Indent.indent(indent)
                            + "## addAnyTransitiveDependencies "
                            + dependency.getModuleId().getId());
                }
                
                if (isOptional(indent + 1, debug, dependency)) {
                    continue;
                }
                
                final TransitiveDependency transitiveDependency
                    = new TransitiveDependency(
                            referencedFrom,
                            projectReferencedFrom,
                            referencedFrom.getReferencedFromRepositories(),
                            dependency.getModuleId());
                
                result.add(transitiveDependency);
            }
        }

        if (debug) {
            System.out.println(Indent.indent(indent)
                    + "## EXIT addAnyTransitiveDependencies("
                    + referencedFrom.getTargetedDependency().getId() + ")");
        }
    }
    
    private static boolean isOptional(int indent, boolean debug, MavenDependency dependency) {

        final boolean optional;
        
        if ("true".equals(dependency.getOptional())) {

            System.out.println(Indent.indent(indent)
                    + "## isOptional skip optional "
                    + dependency.getModuleId().getId());
        
            optional = true;
        }
        else {
            optional = false;
        }

        return optional;
    }
}
