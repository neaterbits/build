package com.neaterbits.build.strategies.common;


import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.build.types.compile.ExternalModuleDependencyList;
import com.neaterbits.build.types.dependencies.LibraryDependency;
import com.neaterbits.build.types.resource.LibraryResourcePath;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.util.concurrency.dependencyresolution.executor.SubPrerequisites;
import com.neaterbits.util.concurrency.dependencyresolution.spec.PrerequisitesBuilderSpec;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.PrerequisitesBuilder;
import com.neaterbits.util.concurrency.scheduling.Constraint;

public final class PrerequisitesBuilderExternalDependencies<CONTEXT extends TaskBuilderContext>
			extends PrerequisitesBuilderSpec<CONTEXT, ProjectModuleResourcePath> {

    private static class ProjectLibraryDependency {
        
        private final ProjectModuleResourcePath project;
        private final LibraryDependency dependency;

        ProjectLibraryDependency(ProjectModuleResourcePath project, LibraryDependency dependency) {
            
            Objects.requireNonNull(project);
            Objects.requireNonNull(dependency);
            
            this.project = project;
            this.dependency = dependency;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((dependency == null) ? 0 : dependency.hashCode());
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
            ProjectLibraryDependency other = (ProjectLibraryDependency) obj;
            if (dependency == null) {
                if (other.dependency != null)
                    return false;
            } else if (!dependency.equals(other.dependency))
                return false;
            return true;
        }
    }

	@Override
	public void buildSpec(PrerequisitesBuilder<CONTEXT, ProjectModuleResourcePath> builder) {

		builder
			.withPrerequisites("External dependencies")
			.makingProduct(ExternalModuleDependencyList.class)
			.fromItemType(LibraryDependency.class)
			
			.fromIteratingAndBuildingRecursively(
					Constraint.NETWORK,
					ProjectLibraryDependency.class,
					
					// from project all module target
					(context, module) -> {

						final List<LibraryDependency> list = StrategiesBuilderUtil.transitiveProjectExternalDependencies(context, module);

						try {
							throw new Exception();
						}
						catch (Exception ex) {
							ex.printStackTrace();
						}
						
						return list.stream()
						        .map(libraryDependency -> new ProjectLibraryDependency(module, libraryDependency))
						        .collect(Collectors.toList());
					},
						
					// from external dependencies found above
					(context, dep) -> {
						
						final List<LibraryDependency> list = StrategiesBuilderUtil.transitiveLibraryDependencies(context, dep.dependency);
						
						final List<ProjectLibraryDependency> libraries = list.stream()
						        .map(libraryDependency -> new ProjectLibraryDependency(dep.project, libraryDependency))
						        .collect(Collectors.toList());
						
						return new SubPrerequisites<>(ProjectLibraryDependency.class, libraries);
					},
					dependency -> dependency) // already of ItemType
			
			.buildBy(st -> {
				
				st.addFileSubTarget(
						ProjectLibraryDependency.class,
						"library",
						"download",
						LibraryResourcePath.class,
						(context, projectDependency) -> projectDependency.dependency.getModulePath(),
						LibraryResourcePath::getFile,
						projectDependency -> "External dependency " + projectDependency.dependency.getModulePath().getLast().getName())
	
				.action(Constraint.NETWORK, (context, target, actionParams) -> {
					context.getBuildRoot().downloadExternalDependencyAndAddToBuildModel(target.project, target.dependency);
					
					return null;
				});
			})
			.collectSubTargetsToProduct((module, dependencies) -> {
				return new ExternalModuleDependencyList(module, dependencies);
			});

	}
	
}
