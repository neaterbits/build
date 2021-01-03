package com.neaterbits.build.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class TypeName {

	private final String [] namespace;
	private final String [] outerTypes;
	private final String name;

	public static TypeName fromNamespace(Collection<String> namespace, String name) {
		return new TypeName(namespace.toArray(new String[namespace.size()]), null, name);
	}

	public TypeName(String[] namespace, String[] outerTypes, String name) {

		Objects.requireNonNull(name);

		this.namespace = namespace;
		this.outerTypes = outerTypes;
		this.name = name;
	}

	public TypeName(String name) {

	    Objects.requireNonNull(name);

	    this.namespace = null;
	    this.outerTypes = null;
	    this.name = name;
	}


	public final String[] getNamespace() {
		return namespace;
	}

	public final String[] getOuterTypes() {
		return outerTypes;
	}

	public final String getName() {
		return name;
	}

	public final ScopedName toScopedName() {

		final List<String> scope;

		if (namespace == null && outerTypes == null) {
			scope = null;
		}
		else if (namespace != null && outerTypes != null) {
			scope = new ArrayList<>(namespace.length + outerTypes.length);

			for (int i = 0; i < namespace.length; ++ i) {
				scope.add(namespace[i]);
			}

			for (int i = 0; i < outerTypes.length; ++ i) {
				scope.add(outerTypes[i]);
			}
		}
		else if (namespace != null) {
			scope = Arrays.asList(namespace);
		}
		else {
			scope = Arrays.asList(outerTypes);
		}

		return new ScopedName(scope, name);
	}

	public String join(char separator) {
	    
        final StringBuilder sb = new StringBuilder();
        
        join(separator, sb::append);
        
        return sb.toString();
	}

    public void join(char separator, Consumer<String> append) {
        join(String.valueOf(separator), append);
    }
    
    private static void join(String [] strings, String separator, Consumer<String> append) {
        
        for (int i = 0; i < strings.length; ++ i) {
            if (i > 0) {
                append.accept(separator);
            }

            append.accept(strings[i]);
        }
    }

    public void join(String separator, Consumer<String> append) {

		if (namespace != null) {
		    join(namespace, separator, append);
		}

		if (outerTypes != null) {
		    if (namespace != null && namespace.length > 0) {
		        append.accept(separator);
		    }
		    
            join(outerTypes, separator, append);
		}

		if (
		           (namespace != null && namespace.length > 0)
		        || (outerTypes != null && outerTypes.length > 0)) {

		    append.accept(separator);
		}

		append.accept(name);
	}

	public String toDebugString() {
		return join('.');
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Arrays.hashCode(namespace);
		result = prime * result + Arrays.hashCode(outerTypes);
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
		TypeName other = (TypeName) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (!Arrays.equals(namespace, other.namespace))
			return false;
		if (!Arrays.equals(outerTypes, other.outerTypes))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TypeName [namespace=" + Arrays.toString(namespace) + ", outerTypes=" + Arrays.toString(outerTypes)
				+ ", name=" + name + "]";
	}
}
