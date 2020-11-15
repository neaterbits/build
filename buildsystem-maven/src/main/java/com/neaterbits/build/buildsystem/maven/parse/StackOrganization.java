package com.neaterbits.build.buildsystem.maven.parse;

import com.neaterbits.util.parse.context.Context;

final class StackOrganization
        extends StackBase
        implements NameSetter, UrlSetter {

    private String name;

    private String url;

    StackOrganization(Context context) {
        super(context);
    }

    String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }
}
