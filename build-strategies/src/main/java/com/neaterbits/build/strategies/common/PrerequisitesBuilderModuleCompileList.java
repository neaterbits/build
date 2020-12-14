package com.neaterbits.build.strategies.common;

import com.neaterbits.build.types.compile.FileCompilation;
import com.neaterbits.build.types.compile.ModuleCompileList;
import com.neaterbits.build.types.compile.SourceFolderCompileList;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;
import com.neaterbits.util.concurrency.dependencyresolution.spec.PrerequisitesBuilderSpec;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.PrerequisitesBuilder;
import com.neaterbits.util.concurrency.scheduling.Constraint;

public final class PrerequisitesBuilderModuleCompileList extends PrerequisitesBuilderSpec<ModulesBuildContext, ProjectModuleResourcePath> {

	@Override
	public void buildSpec(PrerequisitesBuilder<ModulesBuildContext, ProjectModuleResourcePath> builder) {

		builder
			.withPrerequisites("Module class compile list")
			.makingProduct(ModuleCompileList.class)
			.fromItemType(SourceFolderCompileList.class)
			
			.fromIterating(Constraint.IO, (context, module) -> context.getBuildRoot().getBuildSystemRootScan().findSourceFolders(module))
	
			.buildBy(st -> st
					
				.addInfoSubTarget(
						SourceFolderResourcePath.class,
                        "folder",
                        "collect",
						sourceFolder -> sourceFolder.getModule().getName(),
						sourceFolder -> "Class files for source folder " + sourceFolder.getName())
				
					.withPrerequisites("Source folder compilations")
						.makingProduct(SourceFolderCompileList.class)
						.fromItemType(FileCompilation.class)
						.fromIterating(Constraint.IO, (ctx, sourceFolder) -> SourceFilesBuilderUtil.getSourceFiles(ctx, sourceFolder))

						.buildBy(sourceFileTarget -> sourceFileTarget
							.addFileSubTarget(
							        FileCompilation.class,
							        "classfile",
							        "collect",
							        FileCompilation::getCompiledFile,
							        classFile -> "Class file for source file " + classFile.getSourceFile().getName())
								.withPrerequisite("Source file")
								.from(FileCompilation::getSourcePath)
								.withFile(SourceFileResourcePath::getFile)
						)
						.collectSubTargetsToProduct((sourceFolder, fileCompilationList) -> new SourceFolderCompileList(sourceFolder, fileCompilationList))
			)
			.collectSubProductsToProduct((module, resultList) -> new ModuleCompileList(module, resultList));

	}
}
