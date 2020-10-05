package com.neaterbits.ide.common.build.tasks;

import com.neaterbits.ide.common.build.model.compile.FileCompilation;
import com.neaterbits.ide.common.build.model.compile.ModuleCompileList;
import com.neaterbits.ide.common.build.model.compile.SourceFolderCompileList;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;
import com.neaterbits.ide.util.dependencyresolution.spec.PrerequisitesBuilderSpec;
import com.neaterbits.ide.util.dependencyresolution.spec.builder.PrerequisitesBuilder;
import com.neaterbits.ide.util.scheduling.Constraint;

public class PrerequisitesBuilderModuleCompileList extends PrerequisitesBuilderSpec<ModulesBuildContext, ProjectModuleResourcePath> {

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
						"compilelist",
						sourceFolder -> sourceFolder.getModule().getName(),
						sourceFolder -> "Class files for source folder " + sourceFolder.getName())
				
					.withPrerequisites("Source folder compilations")
						.makingProduct(SourceFolderCompileList.class)
						.fromItemType(FileCompilation.class)
						.fromIterating(Constraint.IO, (ctx, sourceFolder) -> SourceFilesBuilderUtil.getSourceFiles(ctx, sourceFolder))

						.buildBy(sourceFileTarget -> sourceFileTarget
							.addFileSubTarget(FileCompilation.class, FileCompilation::getCompiledFile, classFile -> "Class file for source file " + classFile.getSourceFile().getName())
								.withPrerequisite("Source file")
								.from(FileCompilation::getSourcePath)
								.withFile(SourceFileResourcePath::getFile)
						)
						.collectSubTargetsToProduct((sourceFolder, fileCompilationList) -> new SourceFolderCompileList(sourceFolder, fileCompilationList))
			)
			.collectSubProductsToProduct((module, resultList) -> new ModuleCompileList(module, resultList));

	}
}
