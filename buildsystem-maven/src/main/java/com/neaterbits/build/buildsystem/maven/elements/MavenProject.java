package com.neaterbits.build.buildsystem.maven.elements;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.MavenModuleId;

public final class MavenProject extends MavenModule {

    private final MavenOrganization organization;
    private final MavenCiManagement ciManagement;
    private final List<MavenMailingList> mailingLists;
    
	public MavenProject(
			File rootDirectory,
			MavenModuleId moduleId,
			MavenModuleId parentModuleId,
			String packaging,
			Map<String, String> properties,
			MavenCommon common,
			MavenOrganization organization,
			MavenIssueManagement issueManagement,
			MavenCiManagement ciManagement,
			List<MavenMailingList> mailingLists,
			List<MavenProfile> profiles) {
		
		super(
		        rootDirectory,
		        moduleId,
		        parentModuleId,
		        packaging,
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
}
