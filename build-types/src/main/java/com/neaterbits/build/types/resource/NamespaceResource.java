package com.neaterbits.build.types.resource;

import java.io.File;

import com.neaterbits.util.StringUtils;

public final class NamespaceResource extends SourceFileHolderResource {

	private final String [] namespace;

	public NamespaceResource(File file, String [] namespace) {
		super(file, StringUtils.join(namespace, '.'));

		this.namespace = namespace;
	}

	public String [] getNamespace() {
		return namespace;
	}
}
