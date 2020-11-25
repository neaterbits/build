package com.neaterbits.build.buildsystem.maven.project.model;

import java.util.Collections;
import java.util.List;

public final class MavenMailingList {

    private final String name;
    private final String subscribe;
    private final String unsubscribe;
    private final String post;
    private final String archive;
    
    private final List<String> otherArchives;

    public MavenMailingList(
            String name,
            String subscribe,
            String unsubscribe,
            String post,
            String archive,
            List<String> otherArchives) {
        
        this.name = name;
        this.subscribe = subscribe;
        this.unsubscribe = unsubscribe;
        this.post = post;
        this.archive = archive;
        this.otherArchives = otherArchives != null
                    ? Collections.unmodifiableList(otherArchives)
                    : null;
    }

    public String getName() {
        return name;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public String getUnsubscribe() {
        return unsubscribe;
    }

    public String getPost() {
        return post;
    }

    public String getArchive() {
        return archive;
    }

    public List<String> getOtherArchives() {
        return otherArchives;
    }
}
