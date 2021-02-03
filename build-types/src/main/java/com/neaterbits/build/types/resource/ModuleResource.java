package com.neaterbits.build.types.resource;

import java.io.File;
import java.util.Objects;

import com.neaterbits.build.types.ModuleId;

// Representing a module and its root directory
public final class ModuleResource extends FileSystemResource {

	private final ModuleId moduleId;

	public ModuleResource(ModuleId moduleId, File file) {
		super(file, moduleId.getId());

		Objects.requireNonNull(moduleId);

		this.moduleId = moduleId;
	}

	public ModuleId getModuleId() {
		return moduleId;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((moduleId == null) ? 0 : moduleId.hashCode());
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
        ModuleResource other = (ModuleResource) obj;
        if (moduleId == null) {
            if (other.moduleId != null)
                return false;
        } else if (!moduleId.equals(other.moduleId))
            return false;
        return true;
    }
}
