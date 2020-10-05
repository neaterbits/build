package com.neaterbits.build.common.tasks;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import com.neaterbits.build.buildsystem.common.ScanException;
import com.neaterbits.build.buildsystem.maven.MavenBuildSystem;
import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.build.model.BuildRootImpl;

public abstract class BaseBuildTest {

	BuildRoot getBuildRoot() {
		
		final File rootDir = new File("..");
		
		final BuildRoot buildRoot;
		try {
			buildRoot = new BuildRootImpl<>(rootDir, new MavenBuildSystem().scan(rootDir));
		} catch (ScanException ex) {
			throw new IllegalStateException(ex);
		}
		
		assertThat(buildRoot).isNotNull();
		assertThat(buildRoot.getModules().size()).isGreaterThan(0);

		return buildRoot;
	}
	
}
