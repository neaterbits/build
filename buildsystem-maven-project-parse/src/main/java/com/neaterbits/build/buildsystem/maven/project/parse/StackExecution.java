package com.neaterbits.build.buildsystem.maven.project.parse;

import java.util.List;

import com.neaterbits.util.parse.context.Context;

final class StackExecution extends StackConfigurable implements IdSetter {

    private String id;

    private String phase;

    private List<String> goals;

    StackExecution(Context context) {
        super(context);
    }

    String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    String getPhase() {
        return phase;
    }

    void setPhase(String phase) {
        this.phase = phase;
    }

    List<String> getGoals() {
        return goals;
    }

    void setGoals(List<String> goals) {
        this.goals = goals;
    }
}
