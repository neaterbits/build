package com.neaterbits.build.buildsystem.maven.common.model;

import java.util.Objects;

public abstract class MavenEntity {

	private final MavenModuleId moduleId;
	
	private final String packaging;

	protected MavenEntity(MavenModuleId moduleId, String packaging) {
		
		Objects.requireNonNull(moduleId);
		
		this.moduleId = moduleId;
		
		this.packaging = packaging;
	}

	public final MavenModuleId getModuleId() {
		return moduleId;
	}

	public String getPackaging() {
		return packaging;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [moduleId=" + moduleId + ", packaging=" + packaging + "]";
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
        MavenEntity other = (MavenEntity) obj;
        if (moduleId == null) {
            if (other.moduleId != null)
                return false;
        } else if (!moduleId.equals(other.moduleId))
            return false;
        return true;
    }
}
