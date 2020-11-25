package com.neaterbits.build.buildsystem.maven.project.model;

import com.neaterbits.build.buildsystem.maven.common.model.MavenEntity;
import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;

public final class MavenDistributionManagementRelocation extends MavenEntity {

    private final String message;

    public MavenDistributionManagementRelocation(MavenModuleId moduleId, String message) {
        super(moduleId, null);

        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
