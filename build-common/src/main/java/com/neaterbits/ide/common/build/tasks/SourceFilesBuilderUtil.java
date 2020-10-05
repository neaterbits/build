package com.neaterbits.ide.common.build.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.build.types.compile.FileCompilation;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;
import com.neaterbits.build.types.resource.compile.TargetDirectoryResourcePath;
import com.neaterbits.ide.common.build.tasks.util.SourceFileScanner;

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
