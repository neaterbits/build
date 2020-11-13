package com.neaterbits.build.buildsystem.maven.parse;

interface EntitySetter extends VersionSetter {

	void setGroupId(String groupId);
	
	void setArtifactId(String artifactId);

	void setPackaging(String packaging);
}

