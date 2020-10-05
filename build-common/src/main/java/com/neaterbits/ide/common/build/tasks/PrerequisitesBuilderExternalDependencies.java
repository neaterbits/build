package com.neaterbits.ide.common.build.tasks;


import java.util.List;

import com.neaterbits.ide.common.build.model.LibraryDependency;
import com.neaterbits.ide.common.build.model.compile.ExternalModuleDependencyList;
import com.neaterbits.ide.common.resource.LibraryResourcePath;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.util.dependencyresolution.spec.PrerequisitesBuilderSpec;
import com.neaterbits.ide.util.dependencyresolution.spec.builder.PrerequisitesBuilder;
import com.neaterbits.ide.util.scheduling.Constraint;

public final class PrerequisitesBuilderExternalDependencies<CONTEXT extends TaskBuilderContext>
			extends PrerequisitesBuilderSpec<CONTEXT, ProjectModuleResourcePath> {

	@Override
	public void buildSpec(PrerequisitesBuilder<CONTEXT, ProjectModuleResourcePath> builder) {

		builder
			.withPrerequisites("External dependencies")
			.makingProduct(ExternalModuleDependencyList.class)
			.fromItemType(LibraryDependency.class)
			
			.fromIteratingAndBuildingRecursively(
					Constraint.NETWORK,
					LibraryDependency.class,
					
					// from project all module target
					(context, module) -> {

						final List<LibraryDependency> list = ModuleBuilderUtil.transitiveProjectExternalDependencies(context, module);

						System.out.println("## get project transitive library dependencies for " + module + " " + list);
						
						try {
							throw new Exception();
						}
						catch (Exception ex) {
							ex.printStackTrace();
						}
						
						return list;
					},
						
					// from external dependencies found above
					(context, dep) -> {
						
						
						final List<LibraryDependency> list = ModuleBuilderUtil.transitiveLibraryDependencies(context, dep);
						
						System.out.println("## get library transitive dependencies for " + dep + " " + list);
						
						
						return list;
					},
					dependency -> dependency) // already of ItemType
			
			.buildBy(st -> {
				
				st.addFileSubTarget(
						LibraryDependency.class,
						LibraryResourcePath.class,
						(context, dependency) -> dependency.getModulePath(),
						LibraryResourcePath::getFile,
						dependency -> "External dependency " + dependency.getModulePath().getLast().getName())
	
				.action(Constraint.NETWORK, (context, target, actionParams) -> {
					context.getBuildRoot().downloadExternalDependencyAndAddToBuildModel(target);
					
					return null;
				});
			})
			.collectSubTargetsToProduct((module, dependencies) -> {
				
				System.out.println("## collect external dependencies " + module + " " + dependencies);
				
				return new ExternalModuleDependencyList(module, dependencies);
			});

	}
	
}
