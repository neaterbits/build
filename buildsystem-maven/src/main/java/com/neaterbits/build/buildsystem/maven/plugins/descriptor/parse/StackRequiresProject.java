package com.neaterbits.build.buildsystem.maven.plugins.descriptor.parse;

import com.neaterbits.build.buildsystem.maven.parse.StackBoolean;
import com.neaterbits.util.parse.context.Context;

final class StackRequiresProject extends StackBoolean {

    StackRequiresProject(Context context) {
        super(context);
    }
}
