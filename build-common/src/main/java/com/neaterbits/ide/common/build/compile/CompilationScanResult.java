package com.neaterbits.ide.common.build.compile;

import java.util.Objects;

import com.neaterbits.ide.common.build.model.compile.CompileList;
import com.neaterbits.ide.common.build.model.compile.FileDependencyMap;

public final class CompilationScanResult {

	private final CompileList compileList;
	private final FileDependencyMap fileDependencyMap;
	
	public CompilationScanResult(CompileList compileList, FileDependencyMap fileDependencyMap) {

		Objects.requireNonNull(compileList);
		
		this.compileList = compileList;
		this.fileDependencyMap = fileDependencyMap;
	}

	public CompileList getCompileList() {
		return compileList;
	}

	public FileDependencyMap getFileDependencyMap() {
		return fileDependencyMap;
	}
}
