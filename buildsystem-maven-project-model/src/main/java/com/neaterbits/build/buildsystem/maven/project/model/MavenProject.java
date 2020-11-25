package com.neaterbits.build.buildsystem.maven.project.model;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;

public final class MavenProject extends MavenModule {

    private final MavenOrganization organization;
    private final MavenCiManagement ciManagement;
    private final List<MavenMailingList> mailingLists;
    private final MavenScm scm;
    private final MavenDistributionManagement distributionManagement;
    
	public MavenProject(
			File rootDirectory,
			MavenModuleId moduleId,
			MavenParent parent,
			String packaging,
			String name,
			String description,
			Map<String, String> properties,
			MavenCommon common,
			MavenOrganization organization,
			MavenIssueManagement issueManagement,
			MavenCiManagement ciManagement,
			List<MavenMailingList> mailingLists,
			MavenScm scm,
			MavenDistributionManagement distributionManagement,
			List<MavenProfile> profiles) {
		
		super(
		        rootDirectory,
		        moduleId,
		        parent,
		        packaging,
		        name,
		        description,
		        properties,
		        common,
		        issueManagement,
		        profiles);
		
		Objects.requireNonNull(moduleId);
		
		this.organization = organization;
		this.ciManagement = ciManagement;

		this.mailingLists = mailingLists != null
		            ? Collections.unmodifiableList(mailingLists)
                    : null;

        this.scm = scm;
        this.distributionManagement = distributionManagement;
	}

    public MavenOrganization getOrganization() {
        return organization;
    }

    public MavenCiManagement getCiManagement() {
        return ciManagement;
    }

    public List<MavenMailingList> getMailingLists() {
        return mailingLists;
    }

    public MavenScm getScm() {
        return scm;
    }

    public MavenDistributionManagement getDistributionManagement() {
        return distributionManagement;
    }
}
