package com.neaterbits.build.buildsystem.maven.project.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.util.parse.context.Context;

final class StackMailingList
    extends StackBase
    implements NameSetter {

    private String name;
    private String subscribe;
    private String unsubscribe;
    private String post;
    private String archive;

    private List<String> otherArchives;

    StackMailingList(Context context) {
        super(context);
    }

    String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    String getSubscribe() {
        return subscribe;
    }

    void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    String getUnsubscribe() {
        return unsubscribe;
    }

    void setUnsubscribe(String unsubscribe) {
        this.unsubscribe = unsubscribe;
    }

    String getPost() {
        return post;
    }

    void setPost(String post) {
        this.post = post;
    }

    String getArchive() {
        return archive;
    }

    void setArchive(String archive) {
        this.archive = archive;
    }

    List<String> getOtherArchives() {
        return otherArchives;
    }

    void setOtherArchives(List<String> otherArchives) {
        this.otherArchives = otherArchives;
    }
}
