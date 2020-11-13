package com.neaterbits.build.buildsystem.maven.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

abstract class StackStrings extends StackBase {

    private final List<String> strings;
    
    StackStrings(Context context) {
        super(context);
        
        this.strings = new ArrayList<>();
    }

    final void add(String string) {

        Objects.requireNonNull(string);

        strings.add(string);
    }
    
    final List<String> getStrings() {
        return strings;
    }
}
