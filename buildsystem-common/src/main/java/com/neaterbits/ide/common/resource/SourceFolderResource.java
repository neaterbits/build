package com.neaterbits.ide.common.resource;

import java.io.File;

import com.neaterbits.ide.common.language.Language;

public final class SourceFolderResource extends SourceFileHolderResource {

	private final Language language;
	
	public SourceFolderResource(File file, String name, Language language) {
		super(file, name);
		
		this.language = language;
	}

	public Language getLanguage() {
		return language;
	}
}
