package com.neaterbits.build.buildsystem.maven.project.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.util.parse.context.Context;

final class StackGoals extends StackBase {

    private final List<String> goals;
    
    StackGoals(Context context) {
        super(context);
        
        this.goals = new ArrayList<>();
    }

    void add(String goal) {

        Objects.requireNonNull(goal);

        goals.add(goal);
    }
    
    List<String> getGoals() {
        return goals;
    }
}
