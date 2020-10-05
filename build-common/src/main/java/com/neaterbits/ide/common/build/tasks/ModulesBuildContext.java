package com.neaterbits.ide.common.build.tasks;

import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.build.types.compile.Compiler;
import com.neaterbits.build.types.compile.FileDependencyMap;
import com.neaterbits.ide.common.language.BuildableLanguage;

public final class ModulesBuildContext extends TaskBuilderContext {
	final Compiler compiler;
	private final FileDependencyMap fileDependencyMap;
	
	public ModulesBuildContext(
			BuildRoot buildRoot,
			
			BuildableLanguage language,

			Compiler compiler,
			
			FileDependencyMap fileDependencyMap) {
		
		super(buildRoot, language);
		
		this.compiler = compiler;
		this.fileDependencyMap = fileDependencyMap;
	}

	public ModulesBuildContext(ModulesBuildContext context) {
		super(context);
		
		this.compiler = context.compiler;
		this.fileDependencyMap = context.fileDependencyMap;
	}
}
