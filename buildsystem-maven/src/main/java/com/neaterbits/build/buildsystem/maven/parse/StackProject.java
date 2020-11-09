package com.neaterbits.build.buildsystem.maven.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.neaterbits.build.buildsystem.maven.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.elements.MavenBuild;
import com.neaterbits.build.buildsystem.maven.elements.MavenDependency;
import com.neaterbits.build.buildsystem.maven.elements.MavenReporting;
import com.neaterbits.util.parse.context.Context;

final class StackProject extends StackEntity implements DependenciesSetter {

	private String name;

	private MavenModuleId parentModuleId;
	private Map<String, String> properties;
	private List<String> subModules;
	private List<MavenDependency> dependencies;
	private MavenReporting reporting;
	private MavenBuild build;

	StackProject(Context context) {
		super(context);

		this.subModules = new ArrayList<>();
		this.dependencies = new ArrayList<>();
	}

	String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
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

    List<String> getSubModules() {
		return subModules;
	}

	void setSubModules(List<String> subModules) {
		this.subModules = subModules;
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

	void setBuild(MavenBuild build) {
		this.build = build;
	}
}
