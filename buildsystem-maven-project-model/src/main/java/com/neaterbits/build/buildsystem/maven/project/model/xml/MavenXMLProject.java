package com.neaterbits.build.buildsystem.maven.project.model.xml;

import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;

public final class MavenXMLProject<DOCUMENT> {

	private final DOCUMENT document;
	private final MavenProject project;

	public MavenXMLProject(DOCUMENT document, MavenProject project) {
	    
	    Objects.requireNonNull(project);
	    
		this.document = document;
		this.project = project;
	}

	public DOCUMENT getDocument() {
		return document;
	}

	public MavenProject getProject() {
		return project;
	}
}
