package com.neaterbits.build.types;

import java.util.Objects;

public class ModuleId {

	private final String id;

	public ModuleId(String id) {
		
		Objects.requireNonNull(id);
		
		this.id = id;
	}
	
	public final String getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ModuleId other = (ModuleId) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
