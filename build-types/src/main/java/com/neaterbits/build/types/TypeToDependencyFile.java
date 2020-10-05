package com.neaterbits.build.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

public final class TypeToDependencyFile implements ClassLibs {

	private final Map<TypeName, DependencyFile> typeToDependency;
	
	public TypeToDependencyFile() {
		this.typeToDependency = new HashMap<>();
	}
	
	public void mergeModuleDependencyTypes(DependencyFile module, Set<TypeName> types) {
		mergeDependencyTypes(module, types);
	}
		
	public void mergeLibraryDependencyTypesIfNotPresent(DependencyFile library, Set<TypeName> types) {
		mergeDependencyTypes(library, types);
	}

	private void mergeDependencyTypes(DependencyFile dependency, Set<TypeName> types) {
			
		Objects.requireNonNull(dependency);
		Objects.requireNonNull(types);

		for (TypeName type : types) {

			
			if (typeToDependency.put(type, dependency) != null) {
				
				// System.out.println("## already added " + type);
				
				// throw new IllegalStateException();
			}
		}
	}

	public void forEachKeyValue(BiConsumer<TypeName, DependencyFile> forEach) {
		
		for (Map.Entry<TypeName, DependencyFile> entry : typeToDependency.entrySet()) {
			forEach.accept(entry.getKey(), entry.getValue());
		}
	}
	
	public boolean hasType(TypeName typeName) {
		
		Objects.requireNonNull(typeName);
	
		return typeToDependency.containsKey(typeName);
	}
	
	@Override
	public DependencyFile getDependencyFileFor(TypeName typeName) {
		return typeToDependency.get(typeName);
	}

	@Override
	public List<DependencyFile> getFiles() {
		return Collections.unmodifiableList(new ArrayList<>(typeToDependency.values()));
	}
}
