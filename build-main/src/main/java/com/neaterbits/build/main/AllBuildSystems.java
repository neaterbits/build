package com.neaterbits.build.main;

import java.io.File;

import com.neaterbits.build.buildsystem.common.BuildSystem;
import com.neaterbits.build.buildsystem.common.BuildSystems;
import com.neaterbits.build.buildsystem.maven.MavenBuildSystem;

public class AllBuildSystems implements BuildSystems {

	private final BuildSystem [] buildSystems;
	
	AllBuildSystems() {
		this.buildSystems = new BuildSystem [] {
				
				new MavenBuildSystem()
				
		};
	}
	
	@Override
	public BuildSystem findBuildSystem(File projectDir) {
		
		for (BuildSystem buildSystem : buildSystems) {
			
			if (buildSystem.isBuildSystemFor(projectDir)) {
				return buildSystem;
			}
			
		}

		return null;
	}
}
