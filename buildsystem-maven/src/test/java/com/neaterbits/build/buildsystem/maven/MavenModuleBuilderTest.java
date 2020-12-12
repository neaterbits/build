package com.neaterbits.build.buildsystem.maven;

import com.neaterbits.build.buildsystem.common.BuildSystem;
import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.test.BaseModuleBuilderTest;
import com.neaterbits.build.types.ModuleId;

public class MavenModuleBuilderTest extends BaseModuleBuilderTest {

	@Override
	protected String getJarFileName(String moduleName, ModuleId rootModuleId) {
		
		final MavenModuleId moduleId = (MavenModuleId)rootModuleId;
	
		return "buildsystem-common-" + moduleId.getVersion() + ".jar";
	}

	@Override
	protected BuildSystem makeBuildSystem() {
		return new MavenBuildSystem();
	}
}
