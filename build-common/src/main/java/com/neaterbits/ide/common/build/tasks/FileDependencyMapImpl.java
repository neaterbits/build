package com.neaterbits.ide.common.build.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.neaterbits.build.types.compile.FileDependencyMap;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.compiler.util.TypeName;

final class FileDependencyMapImpl implements FileDependencyMap {

	private final Map<TypeName, Set<SourceFileResourcePath>> filesDependingOn;
	
	FileDependencyMapImpl() {

		this.filesDependingOn = new HashMap<>();

	}
	
	@Override
	public Set<SourceFileResourcePath> getFilesDependingOn(TypeName sourceFile) {
		
		Objects.requireNonNull(sourceFile);
		
		return filesDependingOn.get(sourceFile);
	}

}
