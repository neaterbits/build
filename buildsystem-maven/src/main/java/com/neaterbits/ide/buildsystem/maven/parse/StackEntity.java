package com.neaterbits.ide.buildsystem.maven.parse;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.ide.buildsystem.maven.MavenModuleId;

abstract class StackEntity extends StackBase implements EntitySetter {
	
	private String groupId;
	private String artifactId;
	private String version;
	private String packaging;

	StackEntity(Context context) {
		super(context);
	}

	final String getGroupId() {
		return groupId;
	}
	
	@Override
	public final void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	public final String getArtifactId() {
		return artifactId;
	}
	
	@Override
	public final void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	
	public final String getVersion() {
		return version;
	}
	
	@Override
	public final void setVersion(String version) {
		this.version = version;
	}
	
	public final String getPackaging() {
		return packaging;
	}
	
	@Override
	public final void setPackaging(String packaging) {
		this.packaging = packaging;
	}
	
	final MavenModuleId makeModuleId() {
		return new MavenModuleId(groupId, artifactId, version);
	}
}
