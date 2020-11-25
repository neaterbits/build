package com.neaterbits.build.buildsystem.maven.project.parse;

import com.neaterbits.build.buildsystem.common.parse.StackBoolean;
import com.neaterbits.util.parse.context.Context;

final class StackSendOnError extends StackBoolean {

    StackSendOnError(Context context) {
        super(context);
    }
}
