package com.neaterbits.build.buildsystem.maven.project.parse;

import com.neaterbits.build.buildsystem.common.parse.StackBoolean;
import com.neaterbits.util.parse.context.Context;

final class StackSendOnWarning extends StackBoolean {

    StackSendOnWarning(Context context) {
        super(context);
    }
}
