package com.neaterbits.build.buildsystem.maven.parse;

import com.neaterbits.build.buildsystem.maven.elements.MavenDistributionManagementRelocation;
import com.neaterbits.build.buildsystem.maven.elements.MavenDistributionManagementRepository;
import com.neaterbits.build.buildsystem.maven.elements.MavenDistributionManagementSite;
import com.neaterbits.util.parse.context.Context;

final class StackDistributionManagement extends StackBase {

    private String downloadUrl;
    private String status;
    private MavenDistributionManagementRepository repository;
    private MavenDistributionManagementRepository snapshotRepository;
    private MavenDistributionManagementSite site;
    private MavenDistributionManagementRelocation relocation;
    
    StackDistributionManagement(Context context) {
        super(context);
    }

    String getDownloadUrl() {
        return downloadUrl;
    }

    void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    String getStatus() {
        return status;
    }

    void setStatus(String status) {
        this.status = status;
    }

    MavenDistributionManagementRepository getRepository() {
        return repository;
    }

    void setRepository(MavenDistributionManagementRepository repository) {
        this.repository = repository;
    }

    MavenDistributionManagementRepository getSnapshotRepository() {
        return snapshotRepository;
    }

    void setSnapshotRepository(MavenDistributionManagementRepository snapshotRepository) {
        this.snapshotRepository = snapshotRepository;
    }

    MavenDistributionManagementSite getSite() {
        return site;
    }

    void setSite(MavenDistributionManagementSite site) {
        this.site = site;
    }

    MavenDistributionManagementRelocation getRelocation() {
        return relocation;
    }

    void setRelocation(MavenDistributionManagementRelocation relocation) {
        this.relocation = relocation;
    }
}