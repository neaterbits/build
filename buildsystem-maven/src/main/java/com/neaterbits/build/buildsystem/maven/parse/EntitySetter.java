package com.neaterbits.build.buildsystem.maven.parse;

public interface EntitySetter {

	void setGroupId(String groupId);
	
	void setArtifactId(String artifactId);

	void setVersion(String version);
	
	void setPackaging(String packaging);
}

