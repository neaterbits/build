package com.neaterbits.build.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import com.neaterbits.build.buildsystem.common.BuildSystem;
import com.neaterbits.build.buildsystem.common.ScanException;
import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.build.model.BuildRootImpl;

public abstract class BaseBuildTest {

	protected abstract BuildSystem makeBuildSystem();
	
	BuildRoot getBuildRoot() {
		
		final File rootDir = new File("..");
		
		final BuildRoot buildRoot;
		try {
			buildRoot = new BuildRootImpl<>(rootDir, makeBuildSystem().scan(rootDir), null);
		} catch (ScanException ex) {
			throw new IllegalStateException(ex);
		}
		
		assertThat(buildRoot).isNotNull();
		assertThat(buildRoot.getModules().size()).isGreaterThan(0);

		return buildRoot;
	}
	
}
