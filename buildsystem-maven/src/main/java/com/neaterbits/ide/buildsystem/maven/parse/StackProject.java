package com.neaterbits.ide.buildsystem.maven.parse;

import java.util.ArrayList;
import java.util.List;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.ide.buildsystem.maven.MavenModuleId;
import com.neaterbits.ide.buildsystem.maven.elements.MavenBuild;
import com.neaterbits.ide.buildsystem.maven.elements.MavenDependency;
import com.neaterbits.ide.buildsystem.maven.elements.MavenReporting;

final class StackProject extends StackEntity {

	private String name;
	
	private MavenModuleId parentModuleId;
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

	List<String> getSubModules() {
		return subModules;
	}

	void setSubModules(List<String> subModules) {
		this.subModules = subModules;
	}

	List<MavenDependency> getDependencies() {
		return dependencies;
	}

	void setDependencies(List<MavenDependency> dependencies) {
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
