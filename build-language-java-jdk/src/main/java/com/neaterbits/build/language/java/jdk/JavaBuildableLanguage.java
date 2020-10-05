package com.neaterbits.build.language.java.jdk;

import java.io.File;

import com.neaterbits.build.common.language.BuildableLanguage;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledFileResource;
import com.neaterbits.build.types.resource.compile.CompiledFileResourcePath;
import com.neaterbits.build.types.resource.compile.TargetDirectoryResourcePath;
import com.neaterbits.util.PathUtil;

public class JavaBuildableLanguage implements BuildableLanguage {

	@Override
	public CompiledFileResourcePath getCompiledFilePath(TargetDirectoryResourcePath targetDirectory, SourceFileResourcePath sourceFile) {

		final File sourceFolder = sourceFile.getSourceFolder();

		final String path = PathUtil.removeDirectoryFromPath(sourceFolder, sourceFile.getFile());

		final String classFilePath;

		if (path.endsWith(".java")) {
			classFilePath = path.substring(0, path.length() - ".java".length()) + ".class";
		}
		else {
			classFilePath = path;
		}

		final File classesDirectory = new File(targetDirectory.getFile(), "classes");

		final File classFile = new File(classesDirectory, classFilePath);

		return new CompiledFileResourcePath(targetDirectory, new CompiledFileResource(classFile));
	}

}
