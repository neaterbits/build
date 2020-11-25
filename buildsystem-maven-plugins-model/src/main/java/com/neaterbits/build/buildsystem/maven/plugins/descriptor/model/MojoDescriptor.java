package com.neaterbits.build.buildsystem.maven.plugins.descriptor.model;

import java.util.List;

public final class MojoDescriptor {

    private final String goal;
    
    private final String description;
    
    private final String implementation;
    
    private final String language;

    private final String phase;

    private final String executePhase;

    private final String executeGoal;
    
    private final String executeLifecycle;
    
    private final String requiresDependencyResolution;
    
    private final String requiresDependencyCollection;
    
    private final Boolean requiresDirectInvocation;
    
    private final Boolean requiresProject;
    
    private final Boolean requiresReports;
    
    private final Boolean requiresOnline;
    
    private final Boolean aggregator;
    
    private final Boolean inheritedByDefault;
    
    private final Boolean threadSafe;
    
    private final String instantiationStrategy;
    
    private final String executionStrategy;
    
    private final String since;
    
    private final String deprecated;
    
    private final String configurator;
    
    private final String composer;
    
    private final List<MojoParameter> parameters;

    private final List<MojoConfiguration> configurations;

    private final List<MojoRequirement> requirements;

    
    public MojoDescriptor(String goal, String description, String implementation, String language, String phase,
            String executePhase, String executeGoal, String executeLifecycle, String requiresDependencyResolution,
            String requiresDependencyCollection, Boolean requiresDirectInvocation, Boolean requiresProject,
            Boolean requiresReports, Boolean requiresOnline, Boolean aggregator, Boolean inheritedByDefault,
            Boolean threadSafe, String instantiationStrategy, String executionStrategy, String since, String deprecated,
            String configurator, String composer, List<MojoParameter> parameters,
            List<MojoConfiguration> configurations, List<MojoRequirement> requirements) {

        this.goal = goal;
        this.description = description;
        this.implementation = implementation;
        this.language = language;
        this.phase = phase;
        this.executePhase = executePhase;
        this.executeGoal = executeGoal;
        this.executeLifecycle = executeLifecycle;
        this.requiresDependencyResolution = requiresDependencyResolution;
        this.requiresDependencyCollection = requiresDependencyCollection;
        this.requiresDirectInvocation = requiresDirectInvocation;
        this.requiresProject = requiresProject;
        this.requiresReports = requiresReports;
        this.requiresOnline = requiresOnline;
        this.aggregator = aggregator;
        this.inheritedByDefault = inheritedByDefault;
        this.threadSafe = threadSafe;
        this.instantiationStrategy = instantiationStrategy;
        this.executionStrategy = executionStrategy;
        this.since = since;
        this.deprecated = deprecated;
        this.configurator = configurator;
        this.composer = composer;
        this.parameters = parameters;
        this.configurations = configurations;
        this.requirements = requirements;
    }

    public String getGoal() {
        return goal;
    }

    public String getDescription() {
        return description;
    }

    public String getImplementation() {
        return implementation;
    }

    public String getLanguage() {
        return language;
    }

    public String getPhase() {
        return phase;
    }

    public String getExecutePhase() {
        return executePhase;
    }

    public String getExecuteGoal() {
        return executeGoal;
    }

    public String getExecuteLifecycle() {
        return executeLifecycle;
    }

    public String getRequiresDependencyResolution() {
        return requiresDependencyResolution;
    }

    public String getRequiresDependencyCollection() {
        return requiresDependencyCollection;
    }

    public Boolean getRequiresDirectInvocation() {
        return requiresDirectInvocation;
    }

    public Boolean getRequiresProject() {
        return requiresProject;
    }

    public Boolean getRequiresReports() {
        return requiresReports;
    }

    public Boolean getRequiresOnline() {
        return requiresOnline;
    }

    public Boolean getAggregator() {
        return aggregator;
    }

    public Boolean getInheritedByDefault() {
        return inheritedByDefault;
    }

    public Boolean getThreadSafe() {
        return threadSafe;
    }

    public String getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public String getExecutionStrategy() {
        return executionStrategy;
    }

    public String getSince() {
        return since;
    }

    public String getDeprecated() {
        return deprecated;
    }

    public String getConfigurator() {
        return configurator;
    }

    public String getComposer() {
        return composer;
    }

    public List<MojoParameter> getParameters() {
        return parameters;
    }

    public List<MojoConfiguration> getConfigurations() {
        return configurations;
    }

    public List<MojoRequirement> getRequirements() {
        return requirements;
    }
}
