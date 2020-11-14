package com.neaterbits.build.buildsystem.maven.parse;

import com.neaterbits.build.buildsystem.maven.elements.MavenActivationFile;
import com.neaterbits.build.buildsystem.maven.elements.MavenActivationOS;
import com.neaterbits.build.buildsystem.maven.elements.MavenActivationProperty;
import com.neaterbits.util.parse.context.Context;

final class StackActivation extends StackBase {

    private Boolean activeByDefault;
    private String jdk;
    
    private MavenActivationOS os;
    private MavenActivationProperty property;
    private MavenActivationFile file;

    StackActivation(Context context) {
        super(context);
    }

    Boolean getActiveByDefault() {
        return activeByDefault;
    }

    void setActiveByDefault(Boolean activeByDefault) {
        this.activeByDefault = activeByDefault;
    }

    String getJdk() {
        return jdk;
    }

    void setJdk(String jdk) {
        this.jdk = jdk;
    }

    MavenActivationOS getOs() {
        return os;
    }

    void setOs(MavenActivationOS os) {
        this.os = os;
    }

    MavenActivationProperty getProperty() {
        return property;
    }

    void setProperty(MavenActivationProperty property) {
        this.property = property;
    }

    MavenActivationFile getFile() {
        return file;
    }

    void setFile(MavenActivationFile file) {
        this.file = file;
    }
}