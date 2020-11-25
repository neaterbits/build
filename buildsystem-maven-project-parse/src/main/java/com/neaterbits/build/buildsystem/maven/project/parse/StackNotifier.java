package com.neaterbits.build.buildsystem.maven.project.parse;

import java.util.Properties;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.build.buildsystem.maven.common.parse.TypeSetter;
import com.neaterbits.util.parse.context.Context;

final class StackNotifier extends StackBase implements TypeSetter {

    private String type;
    
    private Boolean sendOnError;
    private Boolean sendOnFailure;
    private Boolean sendOnSuccess;
    private Boolean sendOnWarning;
    
    private Properties configuration;

    StackNotifier(Context context) {
        super(context);
        
        this.configuration = new Properties();
    }

    String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    Boolean getSendOnError() {
        return sendOnError;
    }

    void setSendOnError(Boolean sendOnError) {
        this.sendOnError = sendOnError;
    }

    Boolean getSendOnFailure() {
        return sendOnFailure;
    }

    void setSendOnFailure(Boolean sendOnFailure) {
        this.sendOnFailure = sendOnFailure;
    }

    Boolean getSendOnSuccess() {
        return sendOnSuccess;
    }

    void setSendOnSuccess(Boolean sendOnSuccess) {
        this.sendOnSuccess = sendOnSuccess;
    }

    Boolean getSendOnWarning() {
        return sendOnWarning;
    }

    void setSendOnWarning(Boolean sendOnWarning) {
        this.sendOnWarning = sendOnWarning;
    }

    void setConfiguration(Properties configuration) {
        this.configuration = configuration;
    }

    Properties getConfiguration() {
        return configuration;
    }
}
