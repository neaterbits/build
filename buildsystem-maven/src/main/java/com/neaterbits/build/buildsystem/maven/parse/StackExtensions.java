package com.neaterbits.build.buildsystem.maven.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.elements.MavenExtension;
import com.neaterbits.compiler.util.Context;

final class StackExtensions extends StackBase {

	private final List<MavenExtension> extensions;
	
	StackExtensions(Context context) {
		super(context);
		
		this.extensions = new ArrayList<>();
	}
	
	public List<MavenExtension> getExtensions() {
		return extensions;
	}
	
	public void addExtension(MavenExtension extension) {
		
		Objects.requireNonNull(extension);
		
		extensions.add(extension);
	}
}
