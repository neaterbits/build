package com.neaterbits.build.common.tasks.util;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import com.neaterbits.build.types.resource.NamespaceResource;
import com.neaterbits.build.types.resource.ResourcePath;
import com.neaterbits.build.types.resource.SourceFileHolderResourcePath;
import com.neaterbits.build.types.resource.SourceFileResource;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;
import com.neaterbits.util.Files;
import com.neaterbits.util.PathUtil;
import com.neaterbits.util.StringUtils;

public class SourceFileScanner {

	public static void findSourceFiles(SourceFolderResourcePath sourceFolderPath, List<SourceFileResourcePath> sourceFiles) {

		findSourceFiles(
				sourceFolderPath,
				sourceFolderPath.getFile(),
				(sourceHolder, file) -> new SourceFileResourcePath(sourceHolder, new SourceFileResource(file)),
				sourceFile -> sourceFiles.add(sourceFile));
	}

	public static void findSourceFiles(
			SourceFolderResourcePath sourceFolderPath,
			BiFunction<SourceFileHolderResourcePath, File, SourceFileResourcePath> getSourceFile,
			List<SourceFileResourcePath> sourceFiles) {

		findSourceFiles(sourceFolderPath, sourceFolderPath.getFile(), getSourceFile, sourceFile -> sourceFiles.add(sourceFile));
	}

	/*

	public static void findSourceFiles(SourceFolderResourcePath sourceFolderPath, Consumer<SourceFileResourcePath> resources) {

		findSourceFiles(
				sourceFolderPath,
				sourceFolderPath.getFile(),
				(sourceHolder, file) -> new SourceFileResourcePath(sourceHolder, new SourceFileResource(file)),
				resources);
	}
	*/


	public static void findSourceFiles(SourceFileHolderResourcePath sourceHolderPath, File sourceFolderFile, List<ResourcePath> resources) {

		findSourceFiles(
				sourceHolderPath,
				sourceFolderFile,
				(sourceHolder, file) -> new SourceFileResourcePath(sourceHolder, new SourceFileResource(file)),
				sourceFile -> resources.add(sourceFile));
	}

	public static void findSourceFiles(
			SourceFileHolderResourcePath sourceHolderPath,
			File sourceFolderFile,
			BiFunction<SourceFileHolderResourcePath, File, SourceFileResourcePath> getSourceFile,
			Consumer<SourceFileResourcePath> resources) {

		Files.recurseDirectories(sourceFolderFile, file -> {

			final SourceFileResourcePath filePath = getSourceFile.apply(sourceHolderPath, file);

			resources.accept(filePath);
		});
	}

	public static class Namespace {
		private final String dirPath;
		private final NamespaceResource namespace;

		public Namespace(String dirPath, NamespaceResource namespace) {
			this.dirPath = dirPath;
			this.namespace = namespace;
		}

		public String getDirPath() {
			return dirPath;
		}

		public NamespaceResource getNamespace() {
			return namespace;
		}
	}

	public static Namespace getNamespaceResource(File sourceFolderFile, File sourceFile) {

		final File directory = sourceFile.getParentFile();

		final String dirPath = PathUtil.removeDirectoryFromPath(sourceFolderFile, directory);

		final String [] namespaceParts = StringUtils.split(dirPath, File.separatorChar);
		
		final NamespaceResource namespaceResource = new NamespaceResource(directory, namespaceParts);

		return new Namespace(dirPath, namespaceResource);
	}
}
