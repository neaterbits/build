package com.neaterbits.ide.common.build.model.compile;

import java.util.Set;

import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;

public interface FileDependencyMap {

	Set<SourceFileResourcePath> getFilesDependingOn(TypeName typeName);
	
}
