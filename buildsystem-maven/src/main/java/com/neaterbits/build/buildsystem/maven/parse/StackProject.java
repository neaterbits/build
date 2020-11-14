package com.neaterbits.build.buildsystem.maven.parse;

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
        implements NameSetter, UrlSetter, CommonSetter {

	private String name;

	private String url;
	
	private MavenModuleId parentModuleId;
	private Map<String, String> properties;
	
	private final StackCommon common;
	
	private List<MavenProfile> profiles;
	
	StackProject(Context context) {
		super(context);
		
		this.common = new StackCommon();
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

    StackCommon getCommon() {
        return common;
    }

    @Override
    public void setBuild(MavenBuild build) {
        
        common.setBuild(build);
    }

    @Override
    public void setReporting(MavenReporting reporting) {

        common.setReporting(reporting);
    }

    @Override
    public void setModules(List<String> modules) {

        common.setModules(modules);
    }

    @Override
    public void setRepositories(List<MavenRepository> repositories) {

        common.setRepositories(repositories);
    }

    @Override
    public void setPluginRepositories(List<MavenPluginRepository> pluginRepositories) {

        common.setPluginRepositories(pluginRepositories);
    }

    @Override
    public void setDependencies(List<MavenDependency> dependencies) {

        common.setDependencies(dependencies);
    }

    List<MavenProfile> getProfiles() {
        return profiles;
    }

    void setProfiles(List<MavenProfile> profiles) {
        this.profiles = profiles;
    }
}
