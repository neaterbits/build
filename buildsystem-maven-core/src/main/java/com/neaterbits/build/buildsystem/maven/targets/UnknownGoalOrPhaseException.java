package com.neaterbits.build.buildsystem.maven.targets;

public final class UnknownGoalOrPhaseException extends Exception {

    private static final long serialVersionUID = 1L;

    private final String goalOrPhase;
    
    UnknownGoalOrPhaseException(String message, String goalOrPhase) {
        super(message);
        
        this.goalOrPhase = goalOrPhase;
    }

    public String getGoalOrPhase() {
        return goalOrPhase;
    }
}
