package com.neaterbits.build.buildsystem.maven.plugins.descriptor.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoConfiguration;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoParameter;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoRequirement;
import com.neaterbits.util.parse.context.Context;

final class StackMojo
    extends StackBase
    implements 
        DescriptionSetter,
        ImplementationSetter,
        SinceSetter,
        DeprecatedSetter,
        InheritedByDefaultSetter {

    private String goal;
    
    private String description;
    
    private String implementation;
    
    private String language;

    private String phase;

    private String executePhase;

    private String executeGoal;
    
    private String executeLifecycle;
    
    private String requiresDependencyResolution;
    
    private String requiresDependencyCollection;
    
    private Boolean requiresDirectInvocation;
    
    private Boolean requiresProject;
    
    private Boolean requiresReports;
    
    private Boolean requiresOnline;
    
    private Boolean aggregator;
    
    private Boolean inheritedByDefault;
    
    private Boolean threadSafe;
    
    private String instantiationStrategy;
    
    private String executionStrategy;
    
    private String since;
    
    private String deprecated;
    
    private String configurator;
    
    private String composer;
    
    private List<MojoParameter> parameters;

    private List<MojoConfiguration> configurations;

    private List<MojoRequirement> requirements;
    
    StackMojo(Context context) {
        super(context);
    }

    void setGoal(String goal) {
        this.goal = goal;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }

    void setLanguage(String language) {
        this.language = language;
    }

    void setPhase(String phase) {
        this.phase = phase;
    }

    void setExecutePhase(String executePhase) {
        this.executePhase = executePhase;
    }

    void setExecuteGoal(String executeGoal) {
        this.executeGoal = executeGoal;
    }

    void setExecuteLifecycle(String executeLifecycle) {
        this.executeLifecycle = executeLifecycle;
    }

    void setRequiresDependencyResolution(String requiresDependencyResolution) {
        this.requiresDependencyResolution = requiresDependencyResolution;
    }

    void setRequiresDependencyCollection(String requiresDependencyCollection) {
        this.requiresDependencyCollection = requiresDependencyCollection;
    }

    void setRequiresDirectInvocation(Boolean requiresDirectInvocation) {
        this.requiresDirectInvocation = requiresDirectInvocation;
    }

    void setRequiresProject(Boolean requiresProject) {
        this.requiresProject = requiresProject;
    }

    void setRequiresReports(Boolean requiresReports) {
        this.requiresReports = requiresReports;
    }

    void setRequiresOnline(Boolean requiresOnline) {
        this.requiresOnline = requiresOnline;
    }

    void setAggregator(Boolean aggregator) {
        this.aggregator = aggregator;
    }

    @Override
    public void setInheritedByDefault(Boolean inheritedByDefault) {
        this.inheritedByDefault = inheritedByDefault;
    }

    void setThreadSafe(Boolean threadSafe) {
        this.threadSafe = threadSafe;
    }

    void setInstantiationStrategy(String instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

    void setExecutionStrategy(String executionStrategy) {
        this.executionStrategy = executionStrategy;
    }

    @Override
    public void setSince(String since) {
        this.since = since;
    }

    @Override
    public void setDeprecated(String deprecated) {
        this.deprecated = deprecated;
    }

    void setConfigurator(String configurator) {
        this.configurator = configurator;
    }

    void setComposer(String composer) {
        this.composer = composer;
    }

    void addParameter(MojoParameter parameter) {
        
        Objects.requireNonNull(parameter);
        
        if (parameters == null) {
            this.parameters = new ArrayList<>();
        }

        parameters.add(parameter);
    }

    void setConfigurations(List<MojoConfiguration> configurations) {
        
        Objects.requireNonNull(configurations);
        
        this.configurations = configurations;
    }

    void addRequirement(MojoRequirement requirement) {
        
        Objects.requireNonNull(requirement);
        
        if (requirements == null) {
            this.requirements = new ArrayList<>();
        }

        requirements.add(requirement);
    }

    MojoDescriptor build() {

        return new MojoDescriptor(
                goal,
                description,
                implementation,
                language,
                phase,
                executePhase,
                executeGoal,
                executeLifecycle,
                requiresDependencyResolution,
                requiresDependencyCollection,
                requiresDirectInvocation,
                requiresProject,
                requiresReports,
                requiresOnline,
                aggregator,
                inheritedByDefault,
                threadSafe,
                instantiationStrategy,
                executionStrategy,
                since,
                deprecated,
                configurator,
                composer,
                parameters,
                configurations,
                requirements);
    }
}
