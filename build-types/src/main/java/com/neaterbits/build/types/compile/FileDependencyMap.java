package com.neaterbits.build.types.compile;

import java.util.Set;

import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.compiler.util.TypeName;

public interface FileDependencyMap {

	Set<SourceFileResourcePath> getFilesDependingOn(TypeName typeName);
	
}
