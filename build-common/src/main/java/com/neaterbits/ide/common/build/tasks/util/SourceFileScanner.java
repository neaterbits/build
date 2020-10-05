package com.neaterbits.ide.common.build.tasks.util;

import java.io.File;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import com.neaterbits.compiler.util.Files;
import com.neaterbits.compiler.util.Strings;
import com.neaterbits.ide.common.resource.NamespaceResource;
import com.neaterbits.ide.common.resource.ResourcePath;
import com.neaterbits.ide.common.resource.SourceFileHolderResourcePath;
import com.neaterbits.ide.common.resource.SourceFileResource;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;
import com.neaterbits.ide.util.PathUtil;

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
		
		String dirPath = PathUtil.removeDirectoryFromPath(sourceFolderFile, directory);
		
		final String [] namespaceParts = Strings.split(dirPath, '/');
		
		final NamespaceResource namespaceResource = new NamespaceResource(directory, namespaceParts);

		return new Namespace(dirPath, namespaceResource);
	}
}
