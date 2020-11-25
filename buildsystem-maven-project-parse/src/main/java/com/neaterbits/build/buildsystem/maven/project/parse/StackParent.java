package com.neaterbits.build.buildsystem.maven.project.parse;

import com.neaterbits.build.buildsystem.maven.common.parse.StackEntity;
import com.neaterbits.util.parse.context.Context;

final class StackParent extends StackEntity {

    private String relativePath;
	
    StackParent(Context context) {
		super(context);
	}

    String getRelativePath() {
        return relativePath;
    }

    void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }
}
