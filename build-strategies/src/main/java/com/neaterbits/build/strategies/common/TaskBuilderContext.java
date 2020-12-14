package com.neaterbits.build.strategies.common;

import com.neaterbits.build.common.language.BuildableLanguage;
import com.neaterbits.build.model.BuildRoot;

public abstract class TaskBuilderContext extends BuildRootContext {

    private final BuildableLanguage language;

	protected TaskBuilderContext(BuildRoot buildRoot, BuildableLanguage language) {
	    super(buildRoot);
	    
		this.language = language;
	}
	
	protected TaskBuilderContext(TaskBuilderContext context) {
		super(context);
		
		this.language = context.language;
	}

	public final BuildableLanguage getLanguage() {
		return language;
	}
}
