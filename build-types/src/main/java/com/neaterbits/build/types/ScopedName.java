package com.neaterbits.build.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.util.StringUtils;

public final class ScopedName {

	private final List<String> scope;
	private final String name;

	public static ScopedName makeScopedName(String [] parts) {

		Objects.requireNonNull(parts);

		if (parts.length == 0) {
			throw new IllegalArgumentException();
		}

		return new ScopedName(
				parts.length > 1 ? Arrays.asList(Arrays.copyOf(parts, parts.length - 1)) : null,
				parts[parts.length - 1]);
	}

	public static ScopedName makeScopedName(List<String> parts) {

        Objects.requireNonNull(parts);

        if (parts.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return new ScopedName(
                parts.size() > 1 ? parts.subList(0, parts.size() - 1) : null,
                parts.get(parts.size() - 1));
    }

	public static ScopedName makeScopedName(String [] namespaceParts, String name) {

		Objects.requireNonNull(namespaceParts);
		Objects.requireNonNull(name);

		return makeScopedName(namespaceParts, namespaceParts.length, name);
	}

	public static ScopedName makeScopedName(String [] parts, int num, String name) {

		Objects.requireNonNull(parts);

		final List<String> scope = new ArrayList<>(num + 1);

		for (int i = 0; i < num; ++ i) {
			scope.add(parts[i]);
		}

		return new ScopedName(scope, name);
	}

	public static ScopedName makeScopedName(String name) {

	    return new ScopedName(null, name);
	}

	public ScopedName(List<String> scope, String name) {

		Objects.requireNonNull(name);

		this.scope = scope != null ? Collections.unmodifiableList(scope) : null;
		this.name = name;
	}

	public List<String> getScope() {
		return scope;
	}

	public boolean hasScope() {
		return scope != null;
	}
	
	public String getDebugString() {
	    
	    return scope != null
	            ? StringUtils.join(scope, '.') + '.' + name
                : name;
	}

	public String [] getParts() {

		final String [] parts;

		if (scope != null) {
			parts = scope.toArray(new String[scope.size() + 1]);

			parts[parts.length - 1] = name;
		}
		else {
			parts = new String [] { name };
		}

		return parts;
	}

	public String getName() {
		return name;
	}

	public boolean scopeStartsWith(String [] parts) {
		return scope != null ? StringUtils.startsWith(scope, parts) : false;
	}

	public ScopedName removeFromScope(String [] parts) {
		if (!scopeStartsWith(parts)) {
			throw new IllegalArgumentException("Does not start with scope " + Arrays.toString(parts));
		}

		return new ScopedName(scope.subList(parts.length, scope.size()), name);
	}

	@Override
	public String toString() {
		return (scope != null ? StringUtils.join(scope, '.') + '/' : "") + name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScopedName other = (ScopedName) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (scope == null) {
			if (other.scope != null)
				return false;
		} else if (!scope.equals(other.scope))
			return false;
		return true;
	}
}
