package com.neaterbits.build.buildsystem.maven.project.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.util.parse.context.Context;

final class StackFilters extends StackBase {

    private final List<String> filters;

    StackFilters(Context context) {
        super(context);
        
        this.filters = new ArrayList<>();
    }

    void add(String filter) {
        
        Objects.requireNonNull(filter);
        
        filters.add(filter);
    }
    
    List<String> getFilters() {
        return filters;
    }
}
