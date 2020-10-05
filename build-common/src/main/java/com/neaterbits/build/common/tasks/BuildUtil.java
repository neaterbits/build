package com.neaterbits.build.common.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neaterbits.build.common.language.CompileableLanguage;
import com.neaterbits.build.common.tasks.util.SourceFileScanner;
import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledFileResourcePath;
import com.neaterbits.build.types.resource.compile.TargetDirectoryResourcePath;

class BuildUtil {

	static FilesToCompile getFilesToCompile(
			ProjectModuleResourcePath module,
			Collection<SourceFolderResourcePath> sourceFolders,
			BuildRoot buildRoot,
			CompileableLanguage language) {
		
		
		final List<SourceFileResourcePath> allModuleSourceFiles = new ArrayList<>();
		
		for (SourceFolderResourcePath sourceFolder : sourceFolders) {
			
			SourceFileScanner.findSourceFiles(sourceFolder, allModuleSourceFiles);
		}
		
		final TargetDirectoryResourcePath targetDirectory = buildRoot.getTargetDirectory(module);
		
		final List<SourceFileResourcePath> toCompile = new ArrayList<>(allModuleSourceFiles.size());
		final List<SourceFileResourcePath> alreadyBuilt = new ArrayList<>(allModuleSourceFiles.size());
		
		for (SourceFileResourcePath sourceFile : allModuleSourceFiles) {
			final CompiledFileResourcePath compiledFilePath = language.getCompiledFilePath(targetDirectory, sourceFile);
		
			final File file = compiledFilePath.getFile();
			
			if (!file.exists() || file.lastModified() <= sourceFile.getFile().lastModified()) {
				toCompile.add(sourceFile);
			}
			else {
				alreadyBuilt.add(sourceFile);
			}
		}
		
		return new FilesToCompile(toCompile, alreadyBuilt);
	}
}
