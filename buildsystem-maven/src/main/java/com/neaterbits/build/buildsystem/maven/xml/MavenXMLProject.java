package com.neaterbits.build.buildsystem.maven.xml;

import com.neaterbits.build.buildsystem.maven.elements.MavenProject;

public final class MavenXMLProject<DOCUMENT> {

	private final DOCUMENT document;
	private final MavenProject project;

	public MavenXMLProject(DOCUMENT document, MavenProject project) {
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
