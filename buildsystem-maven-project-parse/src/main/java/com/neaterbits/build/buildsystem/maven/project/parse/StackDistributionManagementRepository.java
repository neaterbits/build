package com.neaterbits.build.buildsystem.maven.project.parse;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.util.parse.context.Context;

final class StackDistributionManagementRepository
        extends StackBase
        implements IdSetter, NameSetter, UrlSetter, LayoutSetter {

    private Boolean uniqueVersion;
    private String id;
    private String name;
    private String url;
    private String layout;
    
    StackDistributionManagementRepository(Context context) {
        super(context);
    }

    Boolean getUniqueVersion() {
        return uniqueVersion;
    }

    void setUniqueVersion(Boolean uniqueVersion) {
        this.uniqueVersion = uniqueVersion;
    }

    String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
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

    String getLayout() {
        return layout;
    }

    @Override
    public void setLayout(String layout) {
        this.layout = layout;
    }
}
