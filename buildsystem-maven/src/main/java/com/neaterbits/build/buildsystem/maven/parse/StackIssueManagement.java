package com.neaterbits.build.buildsystem.maven.parse;

import com.neaterbits.util.parse.context.Context;

final class StackIssueManagement
        extends StackBase
        implements SystemSetter, UrlSetter {

    private String system;
    private String url;

    StackIssueManagement(Context context) {
        super(context);
    }

    String getSystem() {
        return system;
    }

    @Override
    public void setSystem(String system) {
        this.system = system;
    }

    String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }
}
