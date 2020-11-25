package com.neaterbits.build.buildsystem.maven.project.model;

import java.util.Properties;

public final class MavenNotifier {
    
    private final String type;
    
    private final Boolean sendOnError;
    private final Boolean sendOnFailure;
    private final Boolean sendOnSuccess;
    private final Boolean sendOnWarning;
    
    private final Properties configuration;

    public MavenNotifier(
            String type,
            Boolean sendOnError,
            Boolean sendOnFailure,
            Boolean sendOnSuccess,
            Boolean sendOnWarning,
            Properties configuration) {

        this.type = type;
        this.sendOnError = sendOnError;
        this.sendOnFailure = sendOnFailure;
        this.sendOnSuccess = sendOnSuccess;
        this.sendOnWarning = sendOnWarning;
        this.configuration = configuration;
    }

    public String getType() {
        return type;
    }

    public Boolean getSendOnError() {
        return sendOnError;
    }

    public Boolean getSendOnFailure() {
        return sendOnFailure;
    }

    public Boolean getSendOnSuccess() {
        return sendOnSuccess;
    }

    public Boolean getSendOnWarning() {
        return sendOnWarning;
    }

    public Properties getConfiguration() {
        return configuration;
    }
}
