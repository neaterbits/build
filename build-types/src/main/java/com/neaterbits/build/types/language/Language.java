package com.neaterbits.build.types.language;

public enum Language {

	JAVA(true);
	
	private final boolean hasFolderNamespaces;

	private Language(boolean hasFolderNamespaces) {
		this.hasFolderNamespaces = hasFolderNamespaces;
	}

	public boolean hasFolderNamespaces() {
		return hasFolderNamespaces;
	}
}
