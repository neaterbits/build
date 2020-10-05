package com.neaterbits.ide.common.build.compile;

import java.util.ArrayList;
import java.util.List;

import com.neaterbits.ide.common.build.model.compile.BuildIssue;

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
