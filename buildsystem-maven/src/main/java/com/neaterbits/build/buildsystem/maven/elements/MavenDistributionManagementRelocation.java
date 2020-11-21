package com.neaterbits.build.buildsystem.maven.elements;

import com.neaterbits.build.buildsystem.maven.MavenModuleId;

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
