package com.neaterbits.build.buildsystem.maven.project.parse;

import com.neaterbits.build.buildsystem.maven.common.parse.StackEntity;
import com.neaterbits.util.parse.context.Context;

final class StackDistributionManagementRelocation extends StackEntity {

    private String message;

    StackDistributionManagementRelocation(Context context) {
        super(context);
    }

    String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }
}
