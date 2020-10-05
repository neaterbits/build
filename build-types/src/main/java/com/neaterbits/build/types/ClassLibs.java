package com.neaterbits.build.types;

import java.util.List;

public interface ClassLibs {

	DependencyFile getDependencyFileFor(TypeName typeName);

	List<DependencyFile> getFiles();
}
