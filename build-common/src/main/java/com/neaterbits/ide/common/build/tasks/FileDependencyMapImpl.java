package com.neaterbits.ide.common.build.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.ide.common.build.model.compile.FileDependencyMap;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;

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
