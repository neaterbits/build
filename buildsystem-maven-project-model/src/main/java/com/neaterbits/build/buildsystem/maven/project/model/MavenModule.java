package com.neaterbits.build.buildsystem.maven.project.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.common.model.MavenEntity;
import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;

public class MavenModule extends MavenEntity {

	private final File rootDirectory;
	private final MavenParent parent;

	private final String name;
	private final String description;
	
	private final Map<String, String> properties;

	private final MavenCommon common;

	private final MavenIssueManagement issueManagement;
	
	private final List<MavenProfile> profiles;

	public MavenModule(
			File rootDirectory,
			MavenModuleId moduleId,
			MavenParent parent,
			String packaging,
			String name,
			String description,
			Map<String, String> properties,
			MavenCommon common,
			MavenIssueManagement issueManagement,
			List<MavenProfile> profiles) {

		super(moduleId, packaging);

		Objects.requireNonNull(rootDirectory);

		this.rootDirectory = rootDirectory;
		this.parent = parent;

		this.name = name;
		this.description = description;

		this.issueManagement = issueManagement;
		
		this.properties = properties != null
		        ? Collections.unmodifiableMap(new HashMap<>(properties))
                : null;

        this.common = common;

        this.profiles = profiles != null
                ? Collections.unmodifiableList(profiles)
                : null;
	}

	private String getGroupId() {
	    
		return getModuleId().getGroupId() != null
				? getModuleId().getGroupId()
				: getParent() != null ? getParentModuleId().getGroupId() : null;
	}

	private String getVersion() {
		return getModuleId().getVersion() != null
				? getModuleId().getVersion()
				: getParent() != null ? getParentModuleId().getVersion() : null;
	}

	public final File getRootDirectory() {
		return rootDirectory;
	}

	public final MavenModuleId getParentModuleId() {
		return parent != null? parent.getModuleId() : null;
	}

	public final MavenParent getParent() {
        return parent;
    }

    public final String getName() {
        return name;
    }

    public final String getDescription() {
        return description;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

	public final MavenCommon getCommon() {
        return common;
    }

    public final List<MavenDependency> resolveDependencies() {
		
		final List<MavenDependency> resolvedDependencies;
		
		if (common.getDependencies() == null) {
			resolvedDependencies = null;
		}
		else {
			resolvedDependencies = new ArrayList<>(common.getDependencies().size());
			
			for (MavenDependency dependency : common.getDependencies()) {
				final MavenDependency resolved = resolveDependency(dependency, getGroupId(), getVersion());
				resolvedDependencies.add(resolved);
			}
		}

		return resolvedDependencies;
	}
	
	static MavenDependency resolveDependency(MavenDependency dependency, String groupId, String version) {

		final MavenModuleId moduleId = dependency.getModuleId();

		return new MavenDependency(
				new MavenModuleId(
						moduleId.getGroupId().replace("${project.groupId}", groupId),
						moduleId.getArtifactId(),
						moduleId.getVersion() != null ? moduleId.getVersion().replace("${project.version}", version) : null),
				
				dependency.getPackaging(),
				dependency.getScope(),
				dependency.getType(),
				dependency.getOptional(),
				dependency.getExclusions());
		
	}

    public final MavenIssueManagement getIssueManagement() {
        return issueManagement;
    }

    public final List<MavenProfile> getProfiles() {
        return profiles;
    }
}
