package com.neaterbits.build.buildsystem.maven.phases;

public final class UnknownPhaseException extends Exception {

    private static final long serialVersionUID = 1L;

    private final String phase;
    
    UnknownPhaseException(String message, String phase) {
        super(message);
    
        this.phase = phase;
    }

    public String getPhase() {
        return phase;
    }
}
