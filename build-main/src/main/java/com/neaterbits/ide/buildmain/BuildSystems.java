package com.neaterbits.ide.buildmain;

import java.io.File;

import com.neaterbits.ide.buildsystem.maven.MavenBuildSystem;
import com.neaterbits.ide.common.buildsystem.BuildSystem;

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
