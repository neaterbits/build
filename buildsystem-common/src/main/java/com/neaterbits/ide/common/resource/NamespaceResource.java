package com.neaterbits.ide.common.resource;

import java.io.File;

import com.neaterbits.compiler.util.Strings;

public class NamespaceResource extends SourceFileHolderResource {

	private final String [] namespace;
	
	public NamespaceResource(File file, String [] namespace) {
		super(file, Strings.join(namespace, '.'));
	
		this.namespace = namespace;
	}

	public String [] getNamespace() {
		return namespace;
	}
}
