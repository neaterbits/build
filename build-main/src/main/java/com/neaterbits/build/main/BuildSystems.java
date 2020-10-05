package com.neaterbits.build.main;

import java.io.File;

import com.neaterbits.build.buildsystem.common.BuildSystem;
import com.neaterbits.build.buildsystem.maven.MavenBuildSystem;

public class BuildSystems {

	private final BuildSystem [] buildSystems;
	
	BuildSystems() {
		this.buildSystems = new BuildSystem [] {
				
				new MavenBuildSystem()
				
		};
	}
	
	BuildSystem findBuildSystem(File projectDir) {
		
		for (BuildSystem buildSystem : buildSystems) {
			
			if (buildSystem.isBuildSystemFor(projectDir)) {
				return buildSystem;
			}
			
		}

		return null;
	}
}
