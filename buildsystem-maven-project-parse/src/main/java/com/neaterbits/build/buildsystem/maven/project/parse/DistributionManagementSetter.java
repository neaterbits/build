package com.neaterbits.build.buildsystem.maven.project.parse;

import com.neaterbits.build.buildsystem.maven.project.model.MavenDistributionManagement;

interface DistributionManagementSetter {

    void setDistributionManagement(MavenDistributionManagement distributionManagement);
}
