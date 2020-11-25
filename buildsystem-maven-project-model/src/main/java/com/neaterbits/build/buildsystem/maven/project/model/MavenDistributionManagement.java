package com.neaterbits.build.buildsystem.maven.project.model;

public final class MavenDistributionManagement {

    private final String downloadUrl;
    private final String status;
    private final MavenDistributionManagementRepository repository;
    private final MavenDistributionManagementRepository snapshotRepository;
    private final MavenDistributionManagementSite site;
    private final MavenDistributionManagementRelocation relocation;
    
    public MavenDistributionManagement(
            String downloadUrl,
            String status,
            MavenDistributionManagementRepository repository,
            MavenDistributionManagementRepository snapshotRepository,
            MavenDistributionManagementSite site,
            MavenDistributionManagementRelocation relocation) {

        this.downloadUrl = downloadUrl;
        this.status = status;
        this.repository = repository;
        this.snapshotRepository = snapshotRepository;
        this.site = site;
        this.relocation = relocation;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getStatus() {
        return status;
    }

    public MavenDistributionManagementRepository getRepository() {
        return repository;
    }

    public MavenDistributionManagementRepository getSnapshotRepository() {
        return snapshotRepository;
    }

    public MavenDistributionManagementSite getSite() {
        return site;
    }

    public MavenDistributionManagementRelocation getRelocation() {
        return relocation;
    }
}
