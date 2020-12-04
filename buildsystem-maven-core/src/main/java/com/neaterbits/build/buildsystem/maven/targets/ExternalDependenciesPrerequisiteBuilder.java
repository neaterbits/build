package com.neaterbits.build.buildsystem.maven.targets;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.neaterbits.build.buildsystem.common.ScanException;
import com.neaterbits.build.buildsystem.maven.MavenBuildRoot;
import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.project.model.BaseMavenRepository;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.common.tasks.ModuleBuilderUtil;
import com.neaterbits.util.concurrency.dependencyresolution.spec.PrerequisitesBuilderSpec;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.FunctionActionLog;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.PrerequisitesBuilder;
import com.neaterbits.util.concurrency.scheduling.Constraint;

final class ExternalDependenciesPrerequisiteBuilder
    extends PrerequisitesBuilderSpec<MavenBuilderContext, PhaseMavenProject> {
    
    static class ProjectDependency {
        private final MavenProject referencedFrom;
        private final List<BaseMavenRepository> referencedFromRepositories;
        
        // for equals()/hashCode()
        private final MavenDependency targetedDependency;
        
        private final MavenDependency dependency;
        private final MavenDependency parentDependency;

        ProjectDependency(
                MavenProject referencedFrom,
                List<BaseMavenRepository> referencedFromRepositories,
                MavenDependency targetedDependency,
                MavenDependency dependency,
                MavenDependency parentDependency) {

            Objects.requireNonNull(referencedFrom);
            Objects.requireNonNull(referencedFromRepositories);
            Objects.requireNonNull(targetedDependency);
            
            this.referencedFrom = referencedFrom;
            this.referencedFromRepositories = referencedFromRepositories;
            this.targetedDependency = targetedDependency;
            this.dependency = dependency;
            this.parentDependency = parentDependency;
        }

        MavenDependency getTargetedDependency() {
            return targetedDependency;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((targetedDependency == null) ? 0 : targetedDependency.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ProjectDependency other = (ProjectDependency) obj;
            if (targetedDependency == null) {
                if (other.targetedDependency != null)
                    return false;
            } else if (!targetedDependency.equals(other.targetedDependency))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "ProjectDependency [referencedFrom=" + referencedFrom + ", targetedDependency=" + targetedDependency
                    + ", dependency=" + dependency + ", parentDependency=" + parentDependency + "]";
        }
    }

    @Override
    public void buildSpec(PrerequisitesBuilder<MavenBuilderContext, PhaseMavenProject> builder) {

        builder.withPrerequisites("Project external dependencies")
            .fromIteratingAndBuildingRecursively(
                    Constraint.IO,
                    ProjectDependency.class,
    
                    (context, module) -> externalDependencies(
                            context.getBuildSystemRoot(),
                            module.getProject(),
                            module.getProject().getCommon().getRepositories()
                                        .stream() // downcast
                                        .collect(Collectors.toList())),
                    
                    (context, externalProjectDependency)
                                -> listOfFurtherDependencies(context.getBuildSystemRoot(), externalProjectDependency),
                    
                    Function.identity())
            .buildBy(
                    externalDependencyBuild -> externalDependencyBuild
                        .addFilesSubTarget(
                                ProjectDependency.class,
                                (context, target, prerequisites) -> false,
                                projectDep -> projectDep.targetedDependency.getModuleId().getId(),
                                projectDep -> "External dependency " + projectDep.targetedDependency.getModuleId().getId())
                        
    
                        // Action for transitive dependencies
                        .action(Constraint.NETWORK, (context, target, actionParams) -> {
                            
                            downloadExternalDependencies(context.getBuildSystemRoot(), target);
                            
                            return FunctionActionLog.OK;
                        }));
    }
    
    static void downloadExternalDependencies(MavenBuildRoot buildRoot, ProjectDependency target) throws IOException, ScanException {
        
        buildRoot.downloadExternalDependencyIfNotPresentAndAddToModel(
                                            target.referencedFromRepositories,
                                            target.targetedDependency);
    }

    static List<ProjectDependency> listOfFurtherDependencies(MavenBuildRoot buildRoot, ProjectDependency projectDependency) {
        
        // List of further dependencies from last external pom file, prioritise parent pom files
        // before other dependencies because the parent dependencies may
        // specify repositories etc that are needed to fetch the pom's further dependencies
        
        final List<ProjectDependency> result;
        
        if (projectDependency.parentDependency == null) {
            // Initial dependency, not scanned parent dependencies
            result = getParentOrTransitiveDependencies(buildRoot, projectDependency.dependency, projectDependency.referencedFromRepositories);
        }
        else {
            // Last was parent dependency, see if that has parent dependency, or return transitive dependencies
            result = getParentOrTransitiveDependencies(buildRoot, projectDependency.parentDependency, projectDependency.referencedFromRepositories);
        }
        
        return result;
    }
    
    private static List<ProjectDependency> getParentOrTransitiveDependencies(
            MavenBuildRoot buildRoot,
            MavenDependency dependency,
            List<BaseMavenRepository> repositories) {

        return getParentOrTransitiveDependencies(buildRoot, dependency.getModuleId(), repositories);
    }

    static List<ProjectDependency> getParentOrTransitiveDependencies(
            MavenBuildRoot buildRoot,
            MavenModuleId moduleId,
            List<BaseMavenRepository> repositories) {
        
        final MavenProject project = buildRoot.getExternalProject(moduleId);
        
        final List<ProjectDependency> result;

        if (project == null) {
            // Parent dependency not downloaded yet so return empty list
            result = Collections.emptyList();
        }
        else {
            if (project.getParent() != null) {
                
                final MavenDependency parentDependency = createDependency(project.getParentModuleId());
            
                final ProjectDependency projectDependency = new ProjectDependency(
                                                                    project,
                                                                    repositories,
                                                                    parentDependency,
                                                                    null,
                                                                    parentDependency);
                
                result = Arrays.asList(projectDependency);
            }
            else {
                // Continue with dependencies
                result = externalDependenciesFromEffectiveProject(buildRoot, project);
            }
        }
        
        return result;
    }
    
    private static MavenDependency createDependency(MavenModuleId moduleId) {
        
        return new MavenDependency(
                moduleId,
                null,
                null,
                null,
                null,
                null);
    }

    private static List<ProjectDependency> externalDependenciesFromEffectiveProject(
            MavenBuildRoot root,
            MavenProject project) {
        
        final MavenProject effectiveProject = root.getEffectiveExternalProject(project.getModuleId());
        
        return externalDependencies(
                root,
                effectiveProject,
                effectiveProject.getCommon().getRepositories()
                                .stream() // downcast
                                .collect(Collectors.toList()));
    }
    
    private static List<ProjectDependency> externalDependencies(
            MavenBuildRoot root,
            MavenProject sourceProject,
            List<BaseMavenRepository> repositories) {
        
        return ModuleBuilderUtil.transitiveProjectDependencies(
                sourceProject,
                (MavenProject project) -> root.getDependencies(project).stream()
                    .filter(p -> !root.isProjectModule(p.getModuleId()))
                    .map(dep -> new ProjectDependency(
                            sourceProject,
                            repositories,
                            dep,
                            dep,
                            null))
                    .collect(Collectors.toList()),
                projectDependency -> root.getProject(projectDependency.targetedDependency));
    }
}
