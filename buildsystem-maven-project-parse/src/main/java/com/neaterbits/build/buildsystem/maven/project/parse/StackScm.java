package com.neaterbits.build.buildsystem.maven.project.parse;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.util.parse.context.Context;

final class StackScm extends StackBase implements UrlSetter {

    private String connection;
    private String developerConnection;
    private String tag;
    private String url;

    StackScm(Context context) {
        super(context);
    }

    String getConnection() {
        return connection;
    }

    void setConnection(String connection) {
        this.connection = connection;
    }

    String getDeveloperConnection() {
        return developerConnection;
    }

    void setDeveloperConnection(String developerConnection) {
        this.developerConnection = developerConnection;
    }

    String getTag() {
        return tag;
    }

    void setTag(String tag) {
        this.tag = tag;
    }

    String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }
}
