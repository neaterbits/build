package com.neaterbits.build.buildsystem.maven.project.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.util.parse.context.Context;

final class StackOtherArchives extends StackBase {

    private final List<String> otherArchives;

    StackOtherArchives(Context context) {
        super(context);
        
        this.otherArchives = new ArrayList<>();
    }

    void add(String otherArchive) {
        
        Objects.requireNonNull(otherArchive);
    
        otherArchives.add(otherArchive);
    }

    List<String> getOtherArchives() {
        return otherArchives;
    }
}
