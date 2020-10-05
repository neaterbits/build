package com.neaterbits.ide.common.build.tasks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.ide.common.build.compile.BuildException;
import com.neaterbits.ide.common.build.compile.Compiler;
import com.neaterbits.ide.common.build.compile.CompilerStatus;
import com.neaterbits.ide.common.build.model.LibraryDependency;
import com.neaterbits.ide.common.build.model.ProjectDependency;
import com.neaterbits.ide.common.build.model.compile.ExternalModuleDependencyList;
import com.neaterbits.ide.common.build.model.compile.ModuleCompileList;
import com.neaterbits.ide.common.build.model.compile.ProjectModuleDependencyList;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.ide.util.dependencyresolution.spec.TargetBuilderSpec;
import com.neaterbits.ide.util.dependencyresolution.spec.builder.ActionLog;
import com.neaterbits.ide.util.dependencyresolution.spec.builder.TargetBuilder;
import com.neaterbits.ide.util.scheduling.Constraint;

public class TargetBuilderModules extends TargetBuilderSpec<ModulesBuildContext> {

	public static final String TARGET_COMPILEALL = "compileall";
	
	@Override
	protected void buildSpec(TargetBuilder<ModulesBuildContext> targetBuilder) {
		
		targetBuilder.addTarget(TARGET_COMPILEALL, "Compile all modules")
			.withPrerequisites("Modules")
			.fromIterating(context -> context.getModules())
			.buildBy(subTarget-> subTarget
					
				.addFileSubTarget(ProjectModuleResourcePath.class,
						CompiledModuleFileResourcePath.class,
						(context, module) -> context.getCompiledModuleFile(module),
						CompiledModuleFileResourcePath::getFile,
						module -> "Compile module  " + module.getName())

					// collect dependencies in list for later
				/*
					.withPrerequisites("Module dependencies list")
						.makingProduct(ModuleDependencyList.class)
						.fromItemType(Dependency.class)
						.fromIterating(null, ModuleBuilderUtil::transitiveProjectDependencies)
						.collectToProduct((module, dependencyList) -> new ModuleDependencyList(module, dependencyList))
				*/
					// add targets for local module dependencies
					.withPrerequisites(new PrerequisitesBuilderProjectDependencies())

					// for downloading external dependencies
					.withPrerequisites(new PrerequisitesBuilderExternalDependencies<>())
					
					// must collect info on classes to compile into a list
					// so can run compiler onto multiple files
					.withPrerequisites(new PrerequisitesBuilderModuleCompileList())
					
					.action(Constraint.CPU, (context, target, actionParameters) -> {
						
						final ModuleCompileList moduleCompileList = actionParameters.getCollectedProduct(
								target,
								ModuleCompileList.class);
						
						final ExternalModuleDependencyList externalDependencyList = actionParameters.getCollectedProduct(
								target,
								ExternalModuleDependencyList.class);

						final ProjectModuleDependencyList projectDependencyList = actionParameters.getCollectedProduct(
								target,
								ProjectModuleDependencyList.class);
						
						/*
						System.out.println("## module compile list " + moduleCompileList);
						
						System.out.println("## external dependency list " + externalDependencyList + " for " + target);

						System.out.println("## project dependency list " + projectDependencyList);
						*/

						if (moduleCompileList == null) {
							throw new IllegalStateException();
						}

						/* May be null for pom files without dependencies
						if (externalDependencyList == null) {
							throw new IllegalStateException();
						}
						*/

						final ActionLog actionLog;
						
						if (!moduleCompileList.getSourceFiles().isEmpty()) {
						
							final File targetDirectory = context.getBuildRoot().getTargetDirectory(target).getFile();
	
							actionLog = compileSourceFiles(context.compiler, moduleCompileList, targetDirectory, projectDependencyList, externalDependencyList);
						}
						else {
							actionLog = null;
						}
						
						return actionLog;
					})
			);
		
	}

	
	private static ActionLog compileSourceFiles(
			Compiler compiler,
			ModuleCompileList moduleCompileList,
			File targetDirectory,
			ProjectModuleDependencyList projectModuleDependencyList,
			ExternalModuleDependencyList externalDependencyList) throws IOException, BuildException {

		if (moduleCompileList == null) {
			throw new IllegalStateException();
		}

		final List<File> dependencies = new ArrayList<>(
				  projectModuleDependencyList.getDependencies().size()
				+ (externalDependencyList != null ? externalDependencyList.getDependencies().size() : 0));

		projectModuleDependencyList.getDependencies().stream()
			.map(ProjectDependency::getCompiledModuleFile)
			.forEach(dependencies::add);

		if (externalDependencyList != null) {
			externalDependencyList.getDependencies().stream()
				.map(LibraryDependency::getCompiledModuleFile)
				.forEach(dependencies::add);
		}
		
		final CompilerStatus status = compiler.compile(

				moduleCompileList.getSourceFiles().stream()
					.flatMap(sourceFiles -> sourceFiles.getFileCompilations().stream())
					.map(fileCompilation -> fileCompilation.getSourcePath())
					.collect(Collectors.toList()),
					
				targetDirectory,
				dependencies);

		if (!status.executedOk()) {
			throw new BuildException(status.getIssues());
		}
		else {
			return new ActionLog(status.getCommandLine(), status.getExitCode());
		}
	}
}
