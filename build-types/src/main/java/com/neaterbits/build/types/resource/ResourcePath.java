package com.neaterbits.build.types.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class ResourcePath {

	private final List<? extends Resource> path;

	public abstract ResourcePath getParentPath();
	
	ResourcePath(Resource ... resources) {
		this(Arrays.asList(resources));
	}

	ResourcePath(List<? extends Resource> path) {
		
		Objects.requireNonNull(path);
		
		this.path = Collections.unmodifiableList(path);
	}
	
	ResourcePath(ResourcePath resourcePath) {
		this.path = resourcePath.path;
	}
	
	ResourcePath(ResourcePath resourcePath, Resource resource) {
		
		Objects.requireNonNull(resourcePath);
		Objects.requireNonNull(resource);
		
		final List<Resource> path = new ArrayList<>(resourcePath.path.size() + 1);
		
		path.addAll(resourcePath.path);
		path.add(resource);
		
		this.path = Collections.unmodifiableList(path);
	}

	final List<? extends Resource> getPath() {
		return path;
	}

	public final int length() {
		return path.size();
	}
	
	public final Resource get(int index) {
		return path.get(index);
	}
	
	public final Resource getLast() {
		return path.get(path.size() - 1);
	}
	
	public final Resource getFromLast(int index) {
		return path.get(path.size() - 1 -index);
	}

	
	public final boolean isAtRoot() {
		return path.size() == 1;
	}
	

	@SuppressWarnings("unchecked")
	final <T extends Resource> List<T> getPaths(int subtractLength) {

		final List<T> list = new ArrayList<>(path.size() - subtractLength);
		
		for (int i = 0; i < path.size() - subtractLength; ++ i) {
			list.add((T)path.get(i));
		}

		return list;
	}

	
	final boolean isDirectSubPathOf(ResourcePath other) {
		
		final boolean isDirectSubPathOf;
		
		if (path.size() != other.path.size() + 1) {
			isDirectSubPathOf = false;
		}
		else {
			
			boolean matches = true;
			
			for (int i = 0; i < other.path.size(); ++ i) {
				if (!path.get(i).equals(other.path.get(i))) {
					matches = false;
					break;
				}
			}
			
			isDirectSubPathOf = matches;
		}
	
		return isDirectSubPathOf;
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourcePath other = (ResourcePath) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return path.toString();
	}
}
