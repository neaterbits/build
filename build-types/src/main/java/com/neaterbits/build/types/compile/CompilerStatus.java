package com.neaterbits.build.types.compile;

import java.util.List;

public final class CompilerStatus {

	private final String commandLine;
	private final int exitCode;
	private final boolean executedOk;
	private final List<BuildIssue> issues;

	public CompilerStatus(String commandLine, int exitCode, boolean executedOk, List<BuildIssue> issues) {

		this.commandLine = commandLine;
		this.exitCode = exitCode;
		this.executedOk = executedOk;
		this.issues = issues;
	}
	
	public String getCommandLine() {
		return commandLine;
	}

	public int getExitCode() {
		return exitCode;
	}

	public boolean executedOk() {
		return executedOk;
	}

	public List<BuildIssue> getIssues() {
		return issues;
	}
}
