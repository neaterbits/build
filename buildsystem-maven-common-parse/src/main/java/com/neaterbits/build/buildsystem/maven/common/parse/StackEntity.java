package com.neaterbits.build.buildsystem.maven.common.parse;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.util.parse.context.Context;

public abstract class StackEntity extends StackBase implements EntitySetter {

	private String groupId;
	private String artifactId;
	private String version;
	private String packaging;

	protected StackEntity(Context context) {
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

	public final MavenModuleId makeModuleId() {
		return new MavenModuleId(groupId, artifactId, version);
	}
}
