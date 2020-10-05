package com.neaterbits.ide.common.build.tasks;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import com.neaterbits.ide.buildsystem.maven.MavenBuildSystem;
import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.build.model.BuildRootImpl;
import com.neaterbits.ide.common.buildsystem.ScanException;

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
