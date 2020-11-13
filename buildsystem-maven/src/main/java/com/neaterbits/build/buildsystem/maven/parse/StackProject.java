package com.neaterbits.build.buildsystem.maven.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.neaterbits.build.buildsystem.maven.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.elements.MavenBuild;
import com.neaterbits.build.buildsystem.maven.elements.MavenDependency;
import com.neaterbits.build.buildsystem.maven.elements.MavenPluginRepository;
import com.neaterbits.build.buildsystem.maven.elements.MavenProfile;
import com.neaterbits.build.buildsystem.maven.elements.MavenReporting;
import com.neaterbits.build.buildsystem.maven.elements.MavenRepository;
import com.neaterbits.util.parse.context.Context;

final class StackProject
        extends StackEntity
        implements DependenciesSetter, NameSetter, UrlSetter, ProfileSetter {

	private String name;

	private String url;
	
	private MavenModuleId parentModuleId;
	private Map<String, String> properties;
	private List<String> modules;
	private List<MavenDependency> dependencies;
	private MavenReporting reporting;
	private MavenBuild build;
    private List<MavenRepository> repositories;
	private List<MavenPluginRepository> pluginRepositories;
	private List<MavenProfile> profiles;
	
	StackProject(Context context) {
		super(context);

		this.modules = new ArrayList<>();
		this.dependencies = new ArrayList<>();
	}

	String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	String getUrl() {
        return url;
    }

	@Override
    public void setUrl(String url) {
        this.url = url;
    }

    public MavenModuleId getParentModuleId() {
		return parentModuleId;
	}

	public void setParentModuleId(MavenModuleId parentModuleId) {
		this.parentModuleId = parentModuleId;
	}

	Map<String, String> getProperties() {
        return properties;
    }

    void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    List<String> getModules() {
		return modules;
	}

    @Override
	public void setModules(List<String> modules) {
		this.modules = modules;
	}

	List<MavenDependency> getDependencies() {
		return dependencies;
	}

	@Override
	public void setDependencies(List<MavenDependency> dependencies) {
		this.dependencies = dependencies;
	}

	MavenReporting getReporting() {
		return reporting;
	}

	void setReporting(MavenReporting reporting) {
		this.reporting = reporting;
	}

	MavenBuild getBuild() {
		return build;
	}

	@Override
	public void setBuild(MavenBuild build) {
		this.build = build;
	}

    List<MavenRepository> getRepositories() {
        return repositories;
    }

    @Override
    public void setRepositories(List<MavenRepository> repositories) {
        this.repositories = repositories;
    }

    List<MavenPluginRepository> getPluginRepositories() {
        return pluginRepositories;
    }

    @Override
    public void setPluginRepositories(List<MavenPluginRepository> pluginRepositories) {
        this.pluginRepositories = pluginRepositories;
    }

    List<MavenProfile> getProfiles() {
        return profiles;
    }

    void setProfiles(List<MavenProfile> profiles) {
        this.profiles = profiles;
    }
}
