package com.neaterbits.build.buildsystem.maven.parse;

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
