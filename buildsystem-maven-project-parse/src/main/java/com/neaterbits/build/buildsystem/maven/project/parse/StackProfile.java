package com.neaterbits.build.buildsystem.maven.project.parse;

import java.util.List;
import java.util.Map;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.project.model.MavenActivation;
import com.neaterbits.build.buildsystem.maven.project.model.MavenBuild;
import com.neaterbits.build.buildsystem.maven.project.model.MavenDependencyManagement;
import com.neaterbits.build.buildsystem.maven.project.model.MavenDistributionManagement;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPluginRepository;
import com.neaterbits.build.buildsystem.maven.project.model.MavenReporting;
import com.neaterbits.build.buildsystem.maven.project.model.MavenRepository;
import com.neaterbits.util.parse.context.Context;

final class StackProfile
        extends StackBase
        implements IdSetter, CommonSetter, PropertiesSetter, DistributionManagementSetter {

    private String id;
    private MavenActivation activation;
    
    private final StackCommon common;

    private Map<String, String> properties;
    
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
    public void setDependencyManagement(MavenDependencyManagement dependencyManagement) {
        common.setDependencyManagement(dependencyManagement);
    }

    @Override
    public void setDistributionManagement(MavenDistributionManagement distributionManagement) {
        common.setDistributionManagement(distributionManagement);
    }

    @Override
    public void setDependencies(List<MavenDependency> dependencies) {

        common.setDependencies(dependencies);
    }

    Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
