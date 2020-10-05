package com.neaterbits.build.common.language;

import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledFileResourcePath;
import com.neaterbits.build.types.resource.compile.TargetDirectoryResourcePath;

public interface BuildableLanguage {

	CompiledFileResourcePath getCompiledFilePath(TargetDirectoryResourcePath targetDirectory, SourceFileResourcePath sourceFile);

}
