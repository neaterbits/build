package com.neaterbits.build.strategies.common;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.build.common.tasks.util.SourceFileScanner;
import com.neaterbits.build.types.compile.FileCompilation;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;
import com.neaterbits.build.types.resource.compile.TargetDirectoryResourcePath;

public class SourceFilesBuilderUtil {

	public static List<FileCompilation> getSourceFiles(TaskBuilderContext context, SourceFolderResourcePath sourceFolder) {
		
		final List<SourceFileResourcePath> sourceFiles = new ArrayList<>();

		SourceFileScanner.findSourceFiles(sourceFolder, sourceFiles);

		final TargetDirectoryResourcePath targetDirectory = context.getBuildRoot().getTargetDirectory(sourceFolder.getModule());

		return sourceFiles.stream()
				.map(sourceFile -> new FileCompilation(sourceFile, context.getLanguage().getCompiledFilePath(targetDirectory, sourceFile)))
				.collect(Collectors.toList());
	}
}
