package com.neaterbits.ide.component.java.language;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.build.types.resource.SourceLineResourcePath;
import com.neaterbits.compiler.util.Strings;
import com.neaterbits.ide.common.build.compile.Compiler;
import com.neaterbits.ide.common.build.compile.CompilerStatus;
import com.neaterbits.ide.common.build.model.compile.BuildIssue;
import com.neaterbits.ide.common.build.model.compile.BuildIssue.Type;

public final class JavaCompiler implements Compiler {

	@Override
	public boolean supportsCompilingMultipleFiles() {
		return true;
	}

	@Override
	public CompilerStatus compile(List<SourceFileResourcePath> sourceFiles, File targetDirectory, List<File> compiledDependencies) throws IOException {
		
		/*
		System.out.println("## compile to " + targetDirectory.getPath() + ": " + sourceFiles.stream()
					.map(file -> file.getName())
					.collect(Collectors.toList()));

		System.out.println("## dependencies: " + compiledDependencies);

		*/

		
		final List<String> arguments = new ArrayList<String>();
		
		arguments.add("javac");
		
		arguments.add("-d");
		arguments.add(targetDirectory.getPath());

		if (compiledDependencies != null) {

			/*
			for (File compiledDependency : compiledDependencies) {

				arguments.add("-cp");
				
				arguments.add(compiledDependency.getPath());
			}
			*/

			arguments.add("-cp");

			arguments.add(Strings.join(compiledDependencies, ':', File::getPath));
		}
		
		arguments.addAll(sourceFiles.stream()
				.map(sourceFile -> sourceFile.getFile().getPath())
				.collect(Collectors.toList()));

		// System.out.println("### arguments: " + arguments);

		final ProcessBuilder processBuilder = new ProcessBuilder(arguments.toArray(new String[arguments.size()]));
		
		final Process process = processBuilder.start();

		final List<BuildIssue> issues = new ArrayList<>();
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
			
			String line;
			
			while (null != (line = reader.readLine())) {
				
				// System.out.println("## compiler output: " + line);
				
				final BuildIssue buildIssue;
				
				if (line.contains("error:")) {
					buildIssue = makeBuildIssue(line, sourceFiles);
				}
				else {
					buildIssue = null;
				}

				if (buildIssue != null) {
					issues.add(buildIssue);
				}
			}
		}

		final int exitCode;
		
		try {
			exitCode = process.waitFor();
		} catch (InterruptedException ex) {
			throw new IOException(ex);
		}
		
		System.out.println("## compile to " + targetDirectory.getPath() + " completed with exit code " + exitCode + " and issues " + issues.size());
		
		return new CompilerStatus(Strings.join(arguments, ' '), exitCode, exitCode == 0, issues);
	}
	
	private static BuildIssue makeBuildIssue(String line, List<SourceFileResourcePath> sourceFiles) {
		
		System.out.println("error: " + line);

		final String [] parts = Strings.split(line, ':');
		
		final BuildIssue buildIssue;
		
		if (parts.length >= 4) {
			
			final int sourceLine = Integer.parseInt(parts[1]);
			
			SourceFileResourcePath sourceFilePath = null;
			
			String errorSourceFile = parts[0];
			
			if (errorSourceFile.startsWith("./")) {
				errorSourceFile = errorSourceFile.substring("./".length());
			}
			
			for (SourceFileResourcePath sourceFile : sourceFiles) {
				
				if (sourceFile.getFile().getPath().endsWith(parts[0])) {
					sourceFilePath = sourceFile;
					break;
				}
			}
			
			final SourceLineResourcePath sourceLineResourcePath = new SourceLineResourcePath(
					sourceFilePath,
					sourceLine);
			
			buildIssue = new BuildIssue(
					Type.ERROR,
					parts[3],
					sourceLineResourcePath);
			
		}
		else {
			buildIssue = null;
		}

		return buildIssue;
	}
}
