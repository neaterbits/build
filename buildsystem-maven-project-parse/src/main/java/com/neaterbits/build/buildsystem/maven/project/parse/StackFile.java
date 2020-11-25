package com.neaterbits.build.buildsystem.maven.project.parse;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.util.parse.context.Context;

final class StackFile extends StackBase {

    private String exists;
    
    private String missing;

    StackFile(Context context) {
        super(context);
    }

    String getExists() {
        return exists;
    }

    void setExists(String exists) {
        this.exists = exists;
    }

    String getMissing() {
        return missing;
    }

    void setMissing(String missing) {
        this.missing = missing;
    }
}
