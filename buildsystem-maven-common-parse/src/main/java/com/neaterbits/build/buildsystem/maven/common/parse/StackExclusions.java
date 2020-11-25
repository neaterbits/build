package com.neaterbits.build.buildsystem.maven.common.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.build.buildsystem.maven.common.model.MavenExclusion;
import com.neaterbits.util.parse.context.Context;

final class StackExclusions extends StackBase {

    private final List<MavenExclusion> exclusions;
    
    StackExclusions(Context context) {
        super(context);
        
        this.exclusions = new ArrayList<>();
    }
    
    void add(MavenExclusion exclusion) {

        Objects.requireNonNull(exclusion);

        exclusions.add(exclusion);
    }

    List<MavenExclusion> getExclusions() {
        return exclusions;
    }
}
