package com.neaterbits.build.buildsystem.maven.project.model;

import java.util.Collections;
import java.util.List;

public final class MavenCiManagement {

    private final String system;
    private final String url;
    private final List<MavenNotifier> notifiers;

    public MavenCiManagement(String system, String url, List<MavenNotifier> notifiers) {

        this.system = system;
        this.url = url;
        this.notifiers = notifiers != null
                ? Collections.unmodifiableList(notifiers)
                : null;
    }

    public String getSystem() {
        return system;
    }

    public String getUrl() {
        return url;
    }

    public List<MavenNotifier> getNotifiers() {
        return notifiers;
    }
}
