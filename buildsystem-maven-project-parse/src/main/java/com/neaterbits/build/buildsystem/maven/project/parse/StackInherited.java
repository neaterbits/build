package com.neaterbits.build.buildsystem.maven.project.parse;

import com.neaterbits.build.buildsystem.common.parse.StackBoolean;
import com.neaterbits.util.parse.context.Context;

final class StackInherited extends StackBoolean {

    StackInherited(Context context) {
        super(context);
    }
}
