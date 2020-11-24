package com.neaterbits.build.buildsystem.common.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

public abstract class StackStrings extends StackBase {

    private final List<String> strings;
    
    protected StackStrings(Context context) {
        super(context);
        
        this.strings = new ArrayList<>();
    }

    public final void add(String string) {

        Objects.requireNonNull(string);

        strings.add(string);
    }
    
    public final List<String> getStrings() {
        return strings;
    }
}
