package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenActivation;
import com.neaterbits.build.buildsystem.maven.elements.MavenBuild;
import com.neaterbits.build.buildsystem.maven.elements.MavenDependency;
import com.neaterbits.build.buildsystem.maven.elements.MavenPluginRepository;
import com.neaterbits.build.buildsystem.maven.elements.MavenReporting;
import com.neaterbits.build.buildsystem.maven.elements.MavenRepository;
import com.neaterbits.util.parse.context.Context;

final class StackProfile
        extends StackBase
        implements IdSetter, CommonSetter {

    private String id;
    private MavenActivation activation;
    
    private final StackCommon common;

    StackProfile(Context context) {
        super(context);
        
        this.common = new StackCommon();
    }

    String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    MavenActivation getActivation() {
        return activation;
    }

    void setActivation(MavenActivation activation) {
        this.activation = activation;
    }

    StackCommon getCommon() {
        return common;
    }

    @Override
    public void setBuild(MavenBuild build) {

        common.setBuild(build);
    }

    @Override
    public void setReporting(MavenReporting reporting) {

        common.setReporting(reporting);
    }

    @Override
    public void setModules(List<String> modules) {

        common.setModules(modules);
    }

    @Override
    public void setRepositories(List<MavenRepository> repositories) {

        common.setRepositories(repositories);
    }

    @Override
    public void setPluginRepositories(List<MavenPluginRepository> pluginRepositories) {

        common.setPluginRepositories(pluginRepositories);
    }

    @Override
    public void setDependencies(List<MavenDependency> dependencies) {

        common.setDependencies(dependencies);
    }
}
