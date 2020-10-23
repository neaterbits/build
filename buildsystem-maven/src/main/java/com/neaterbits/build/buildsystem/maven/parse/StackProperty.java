package com.neaterbits.build.buildsystem.maven.parse;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

final class StackProperty extends StackText {

    private final String name;

    StackProperty(Context context, String name) {
        super(context);

        Objects.requireNonNull(name);
        
        this.name = name;
    }

    String getName() {
        return name;
    }
}
