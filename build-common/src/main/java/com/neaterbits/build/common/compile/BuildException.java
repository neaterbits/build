package com.neaterbits.build.common.compile;

import java.util.ArrayList;
import java.util.List;

import com.neaterbits.build.types.compile.BuildIssue;

public class BuildException extends Exception {

	private static final long serialVersionUID = 1L;

	private final List<BuildIssue> issues;
	
	public BuildException(List<BuildIssue> issues) {
		this.issues = new ArrayList<>(issues);
	}

	public List<BuildIssue> getIssues() {
		return issues;
	}
}
