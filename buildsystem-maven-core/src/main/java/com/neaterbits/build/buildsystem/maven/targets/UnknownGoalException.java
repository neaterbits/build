package com.neaterbits.build.buildsystem.maven.targets;

public final class UnknownGoalException extends Exception {

    private static final long serialVersionUID = 1L;

    private final String plugin;
    private final String goal;
    
    UnknownGoalException(String message, String plugin, String goal) {
        super(message);

        this.plugin = plugin;
        this.goal = goal;
    }

    public String getPlugin() {
        return plugin;
    }

    public String getGoal() {
        return goal;
    }
}
