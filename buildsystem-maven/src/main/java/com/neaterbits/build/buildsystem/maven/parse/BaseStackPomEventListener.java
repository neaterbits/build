package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenActivation;
import com.neaterbits.build.buildsystem.maven.elements.MavenActivationFile;
import com.neaterbits.build.buildsystem.maven.elements.MavenActivationOS;
import com.neaterbits.build.buildsystem.maven.elements.MavenActivationProperty;
import com.neaterbits.build.buildsystem.maven.elements.MavenBuild;
import com.neaterbits.build.buildsystem.maven.elements.MavenBuildPlugin;
import com.neaterbits.build.buildsystem.maven.elements.MavenExecution;
import com.neaterbits.build.buildsystem.maven.elements.MavenCiManagement;
import com.neaterbits.build.buildsystem.maven.elements.MavenExtension;
import com.neaterbits.build.buildsystem.maven.elements.MavenIssueManagement;
import com.neaterbits.build.buildsystem.maven.elements.MavenMailingList;
import com.neaterbits.build.buildsystem.maven.elements.MavenNotifier;
import com.neaterbits.build.buildsystem.maven.elements.MavenOrganization;
import com.neaterbits.build.buildsystem.maven.elements.MavenPluginConfiguration;
import com.neaterbits.build.buildsystem.maven.elements.MavenPluginRepository;
import com.neaterbits.build.buildsystem.maven.elements.MavenProfile;
import com.neaterbits.build.buildsystem.maven.elements.MavenReleases;
import com.neaterbits.build.buildsystem.maven.elements.MavenReportSet;
import com.neaterbits.build.buildsystem.maven.elements.MavenReporting;
import com.neaterbits.build.buildsystem.maven.elements.MavenReportPlugin;
import com.neaterbits.build.buildsystem.maven.elements.MavenRepository;
import com.neaterbits.build.buildsystem.maven.elements.MavenResource;
import com.neaterbits.build.buildsystem.maven.elements.MavenScm;
import com.neaterbits.build.buildsystem.maven.elements.MavenSnapshots;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenDependencyManagement;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginManagement;
import com.neaterbits.build.buildsystem.maven.xml.XMLAttribute;
import com.neaterbits.util.parse.context.Context;

abstract class BaseStackPomEventListener extends BaseEntityStackEventListener implements PomEventListener {

    @Override
    public final void onProjectStart(Context context) {
        push(new StackProject(context));
    }

    @Override
    public final void onParentStart(Context context) {

        push(new StackParent(context));
    }

    @Override
    public final void onParentEnd(Context context) {

        final StackParent stackParent = pop();

        final StackProject stackProject = get();

        stackProject.setParentModuleId(stackParent.makeModuleId());
    }

    @Override
    public final void onPropertiesStart(Context context) {

        push(new StackProperties(context));
    }

    @Override
    public void onUnknownTagStart(Context context, String name, List<XMLAttribute> attributes) {

        final Object cur = get();
        
        if (cur instanceof StackProperties || cur instanceof StackCiConfiguration) {
            push(new StackProperty(context, name));
        }
        else if (cur instanceof StackPluginConfigurationMap || cur instanceof StackConfigurationLevel) {
            push(new StackConfigurationLevel(context, name));
        }
    }

    @Override
    public void onUnknownTagEnd(Context context, String name) {
        
        final Object cur = get();
        
        if (cur instanceof StackProperty) {
            final StackProperty stackProperty = pop();

            final Object last = get();
            
            if (last instanceof StackProperties) {

                final StackProperties stackProperties = get();
                
                stackProperties.add(stackProperty.getName(), stackProperty.getText());
                
            }
            else if (last instanceof StackCiConfiguration) {
                
                final StackCiConfiguration stackCiConfiguration = get();
                
                stackCiConfiguration.addKeyValue(stackProperty.getName(), stackProperty.getText());
            }
            else {
                throw new IllegalStateException();
            }
        }
        else if (cur instanceof StackConfigurationLevel) {
         
            final StackConfigurationLevel stackConfigurationLevel = pop();
            
            final StackBase last = get();
            
            if (last instanceof StackConfigurationLevel) {
                
                final StackConfigurationLevel lastConfigurationLevel = (StackConfigurationLevel)last;
                
                if (stackConfigurationLevel.getText() != null) {
                    lastConfigurationLevel.add(stackConfigurationLevel.getTagName(), stackConfigurationLevel.getText());
                }
                else {
                    lastConfigurationLevel.add(stackConfigurationLevel.getTagName(), stackConfigurationLevel.getObject());
                }
            }
            else {
                throw new IllegalStateException();
            }
        }
    }

    @Override
    public void onPropertiesEnd(Context context) {

        final StackProperties stackProperties = pop();
        
        if (!stackProperties.getProperties().isEmpty()) {
            
            final StackProject stackProject = get();
            
            stackProject.setProperties(stackProperties.getProperties());
        }
    }

    @Override
    public final void onModulesStart(Context context) {

        push(new StackModules(context));
    }

    @Override
    public final void onModuleStart(Context context) {

        push(new StackModule(context));
    }

    @Override
    public final void onModuleEnd(Context context) {
        final StackModule stackModule = pop();

        final StackModules stackModules = get();

        stackModules.addModule(stackModule.getText());
    }

    @Override
    public final void onModulesEnd(Context context) {

        final StackModules stackModules = pop();

        final ModulesSetter modulesSetter = get();

        modulesSetter.setModules(stackModules.getModules());
    }

    @Override
    public final void onReportingStart(Context context) {

        push(new StackReporting(context));
    }

    @Override
    public final void onReportSetsStart(Context context) {
        push(new StackReportSets(context));
    }

    @Override
    public void onReportSetStart(Context context) {
        push(new StackReportSet(context));
    }

    @Override
    public void onReportsStart(Context context) {
        push(new StackReports(context));
    }

    @Override
    public void onReportStart(Context context) {
        push(new StackReport(context));
    }

    @Override
    public void onReportsEnd(Context context) {

        final StackReports stackReports = pop();
        
        final StackReportSet stackReportSet = get();
        
        stackReportSet.setReports(stackReports.getReports());
    }

    @Override
    public void onReportEnd(Context context) {

        final StackReport stackReport = pop();
        
        final StackReports stackReports = get();
        
        stackReports.add(stackReport.getText());
    }

    @Override
    public void onReportSetEnd(Context context) {

        final StackReportSet stackReportSet = pop();
        
        final StackReportSets stackReportSets = get();
        
        final MavenReportSet reportSet = new MavenReportSet(
                                                stackReportSet.getId(),
                                                stackReportSet.getReports());
        stackReportSets.add(reportSet);
    }

    @Override
    public void onReportSetsEnd(Context context) {

        final StackReportSets stackReportSets = pop();
        
        final StackReportingPlugin stackReportingPlugin = get();
        
        stackReportingPlugin.setReportSets(stackReportSets.getReportSets());
    }

    @Override
    public final void onReportingEnd(Context context) {

        final StackReporting stackReporting = pop();

        final MavenReporting reporting = new MavenReporting(
                                            stackReporting.getExcludeDefaults(),
                                            stackReporting.getOutputDirectory(),
                                            stackReporting.getPlugins());

        final StackProject stackProject = get();

        stackProject.setReporting(reporting);
    }

    @Override
    public final void onBuildStart(Context context) {
        push(new StackBuild(context));
    }

    @Override
    public void onDependencyManagementStart(Context context) {
        push(new StackDependencyManagement(context));
    }

    @Override
    public void onDependencyManagementEnd(Context context) {

        final StackDependencyManagement stackDependencyManagement = pop();
        
        final DependencyManagementSetter dependencyManagementSetter = get();
        
        final MavenDependencyManagement dependencyManagement
                = new MavenDependencyManagement(stackDependencyManagement.getDependencies());
        
        dependencyManagementSetter.setDependencyManagement(dependencyManagement);
    }

    @Override
    public void onPluginManagementStart(Context context) {
        push(new StackPluginManagement(context));
    }

    @Override
    public void onPluginManagementEnd(Context context) {

        final StackPluginManagement stackPluginManagement = pop();
        
        final StackBaseBuild stackBuildLike = get();
        
        final MavenPluginManagement pluginManagement
                = new MavenPluginManagement(stackPluginManagement.getPlugins());
        
        stackBuildLike.setPluginManagement(pluginManagement);
    }

    @Override
    public final void onPluginsStart(Context context) {
        
        final StackBase cur = get();
        
        if (cur instanceof StackBuild || cur instanceof StackPluginManagement) {
            push(new StackPlugins(context));
        }
        else if (cur instanceof StackReporting) {
            push(new StackReportingPlugins(context));
        }
        else {
            throw new IllegalStateException();
        }
    }

    @Override
    public void onDefaultGoalStart(Context context) {
        push(new StackDefaultGoal(context));
    }

    @Override
    public void onDefaultGoalEnd(Context context) {
        
        final StackDefaultGoal stackDefaultGoal = pop();
        
        final StackBaseBuild stackBaseBuild = get();
        
        stackBaseBuild.setDefaultGoal(stackDefaultGoal.getText());
    }

    @Override
    public final void onDirectoryStart(Context context) {
        push(new StackDirectory(context));
    }

    @Override
    public final void onDirectoryEnd(Context context) {

        final StackDirectory stackDirectory = pop();
        
        final DirectorySetter directorySetter = get();
        
        directorySetter.setDirectory(stackDirectory.getText());
    }

    @Override
    public final void onOutputDirectoryStart(Context context) {
        push(new StackOutputDirectory(context));
    }

    @Override
    public final void onOutputDirectoryEnd(Context context) {

        final StackOutputDirectory stackOutputDirectory = pop();
        
        final StackBuild stackBuild = get();
        
        stackBuild.setOutputDirectory(stackOutputDirectory.getText());
    }

    @Override
    public final void onFinalNameStart(Context context) {
        push(new StackFinalName(context));
    }

    @Override
    public final void onFinalNameEnd(Context context) {

        final StackFinalName stackFinalName = pop();
        
        final StackBaseBuild stackBaseBuild = get();
        
        stackBaseBuild.setFinalName(stackFinalName.getText());
    }

    @Override
    public void onFiltersStart(Context context) {
        push(new StackFilters(context));
    }

    @Override
    public void onFilterStart(Context context) {
        push(new StackFilter(context));
    }

    @Override
    public void onFilterEnd(Context context) {

        final StackFilter stackFilter = pop();
        
        final StackFilters stackFilters = get();
        
        stackFilters.add(stackFilter.getText());
    }

    @Override
    public void onFiltersEnd(Context context) {

        final StackFilters stackFilters = pop();

        final StackBaseBuild stackBaseBuild = get();
        
        stackBaseBuild.setFilters(stackFilters.getFilters());
    }

    @Override
    public final void onSourceDirectoryStart(Context context) {
        push(new StackSourceDirectory(context));
    }

    @Override
    public final void onSourceDirectoryEnd(Context context) {

        final StackSourceDirectory stackSourceDirectory = pop();
        
        final StackBuild stackBuild = get();
        
        stackBuild.setSourceDirectory(stackSourceDirectory.getText());
    }

    @Override
    public final void onScriptSourceDirectoryStart(Context context) {
        push(new StackScriptSourceDirectory(context));
    }

    @Override
    public final void onScriptSourceDirectoryEnd(Context context) {

        final StackScriptSourceDirectory stackScriptSourceDirectory = pop();
        
        final StackBuild stackBuild = get();
        
        stackBuild.setScriptSourceDirectory(stackScriptSourceDirectory.getText());
    }

    @Override
    public final void onTestSourceDirectoryStart(Context context) {
        push(new StackTestSourceDirectory(context));
    }

    @Override
    public final void onTestSourceDirectoryEnd(Context context) {

        final StackTestSourceDirectory stackTestSourceDirectory = pop();
        
        final StackBuild stackBuild = get();
        
        stackBuild.setTestSourceDirectory(stackTestSourceDirectory.getText());
    }

    @Override
    public void onResourcesStart(Context context) {
        push(new StackResources(context));
    }

    @Override
    public void onResourceStart(Context context) {
        push(new StackResource(context));
    }

    @Override
    public void onTargetPathStart(Context context) {
        push(new StackTargetPath(context));
    }

    @Override
    public void onTargetPathEnd(Context context) {

        final StackTargetPath stackTargetPath = pop();
        
        final StackResource stackResource = get();
        
        stackResource.setTargetPath(stackTargetPath.getText());
    }

    @Override
    public void onFilteringStart(Context context) {
        push(new StackFiltering(context));
    }

    @Override
    public void onFilteringEnd(Context context) {

        final StackFiltering stackFiltering = pop();
        
        final StackResource stackResource = get();
        
        stackResource.setFiltering(stackFiltering.getValue());
    }

    @Override
    public void onIncludesStart(Context context) {
        push(new StackIncludes(context));
    }

    @Override
    public void onIncludeStart(Context context) {
        push(new StackInclude(context));
    }

    @Override
    public void onIncludeEnd(Context context) {

        final StackInclude stackInclude = pop();
        
        final StackIncludes stackIncludes = get();
        
        stackIncludes.add(stackInclude.getText());
    }

    @Override
    public void onIncludesEnd(Context context) {

        final StackIncludes stackIncludes = pop();
        
        final StackResource stackResource = get();
        
        stackResource.setIncludes(stackIncludes.getStrings());
    }

    @Override
    public void onExcludesStart(Context context) {
        push(new StackExcludes(context));
    }

    @Override
    public void onExcludeStart(Context context) {
        push(new StackExclude(context));
    }

    @Override
    public void onExcludeEnd(Context context) {

        final StackExclude stackExclude = pop();
        
        final StackExcludes stackExcludes = get();
        
        stackExcludes.add(stackExclude.getText());
    }

    @Override
    public void onExcludesEnd(Context context) {

        final StackExcludes stackExcludes = pop();
        
        final StackResource stackResource = get();
        
        stackResource.setExcludes(stackExcludes.getStrings());
    }

    @Override
    public void onResourceEnd(Context context) {
    
        addResource();
    }
    

    private void addResource() {
        
        final StackResource stackResource = pop();
        
        final StackResources stackResources = get();
        
        final MavenResource resource = new MavenResource(
                stackResource.getTargetPath(),
                stackResource.getFiltering(),
                stackResource.getDirectory(),
                stackResource.getIncludes(),
                stackResource.getExcludes());
        
        stackResources.add(resource);
    }

    @Override
    public void onResourcesEnd(Context context) {
        final StackResources stackResources = pop();
        
        final StackBuild stackBuild = get();
        
        stackBuild.setResources(stackResources.getResources());
    }

    @Override
    public void onTestResourcesStart(Context context) {
        push(new StackResources(context));
    }

    @Override
    public void onTestResourceStart(Context context) {
        push(new StackResource(context));
    }

    @Override
    public void onTestResourceEnd(Context context) {

        addResource();
    }

    @Override
    public void onTestResourcesEnd(Context context) {
        
        final StackResources stackResources = pop();
        
        final StackBuild stackBuild = get();
        
        stackBuild.setTestResources(stackResources.getResources());
    }

    @Override
    public final void onPluginStart(Context context) {
        
        final StackBase cur = get();
        
        if (cur instanceof StackPlugins) {
            push(new StackPlugin(context));
        }
        else if (cur instanceof StackReportingPlugins) {
            push(new StackReportingPlugin(context));
        }
        else {
            throw new IllegalStateException();
        }
    }

    @Override
    public void onInheritedStart(Context context) {
        push(new StackInherited(context));
    }

    @Override
    public void onInheritedEnd(Context context) {

        final StackInherited stackInherited = pop();
        
        final InheritedSetter inheritedSetter = get();
        
        inheritedSetter.setInherited(stackInherited.getValue());
    }

    @Override
    public void onConfigurationStart(Context context) {
        
        final StackBase cur = get();
        
        if (cur instanceof StackPlugin || cur instanceof StackReportingPlugin || cur instanceof StackExecution) {
            push(new StackPluginConfigurationMap(context));
        }
        else if (cur instanceof StackNotifier) {
            push(new StackCiConfiguration(context));
        }
        else {
            throw new IllegalStateException("Unknown type " + cur.getClass());
        }
    }

    @Override
    public void onConfigurationEnd(Context context) {

        final StackBase cur = pop();
        
        if (cur instanceof StackPluginConfigurationMap) {
            
            final StackPluginConfigurationMap stackPluginConfiguration = (StackPluginConfigurationMap)cur;
            
            final ConfigurationSetter configurationSetter = get();
            
            configurationSetter.setConfiguration(stackPluginConfiguration.getConfiguration());
        }
        else if (cur instanceof StackCiConfiguration) {
     
            final StackCiConfiguration stackCiConfiguration = (StackCiConfiguration)cur;
            
            final StackNotifier stackNotifier = get();
            
            stackNotifier.setConfiguration(stackCiConfiguration.getProperties());
        }
        else {
            throw new IllegalStateException();
        }
    }

    @Override
    public void onExecutionsStart(Context context) {

        push(new StackExecutions(context));
        
    }

    @Override
    public void onExecutionStart(Context context) {
        push(new StackExecution(context));
    }

    @Override
    public void onPhaseStart(Context context) {
        push(new StackPhase(context));
    }

    @Override
    public void onPhaseEnd(Context context) {

        final StackPhase stackPhase = pop();
        
        final StackExecution stackExecution = get();
        
        stackExecution.setPhase(stackPhase.getText());
    }

    @Override
    public void onGoalsStart(Context context) {
        push(new StackGoals(context));
    }

    @Override
    public void onGoalStart(Context context) {
        push(new StackGoal(context));
    }

    @Override
    public void onGoalEnd(Context context) {

        final StackGoal stackGoal = pop();
        
        final StackGoals stackGoals = get();
        
        stackGoals.add(stackGoal.getText());
    }

    @Override
    public void onGoalsEnd(Context context) {

        final StackGoals stackGoals = pop();
        
        final StackExecution stackExecution = get();
        
        stackExecution.setGoals(stackGoals.getGoals());
    }

    @Override
    public void onExecutionEnd(Context context) {

        final StackExecution stackExecution = pop();
        
        final StackExecutions stackExecutions = get();
        
        final MavenExecution execution = new MavenExecution(
                                                stackExecution.getId(),
                                                stackExecution.getPhase(),
                                                stackExecution.getGoals(),
                                                stackExecution.makeConfiguration());
        
        stackExecutions.add(execution);
    }

    @Override
    public void onExecutionsEnd(Context context) {

        final StackExecutions stackExecutions = pop();
        
        final StackPlugin stackPlugin = get();
        
        stackPlugin.setExecutions(stackExecutions.getExecutions());
    }

    @Override
    public final void onPluginEnd(Context context) {

        final StackBase cur = pop();
        
        if (cur instanceof StackPlugin) {
         
            final StackPlugin stackPlugin = (StackPlugin)cur;
    
            final MavenBuildPlugin plugin = new MavenBuildPlugin(
                                                            stackPlugin.makeModuleId(),
                                                            new MavenPluginConfiguration(
                                                                    stackPlugin.getInherited(),
                                                                    stackPlugin.getConfiguration()),
                                                            stackPlugin.getExtensions(),
                                                            stackPlugin.getDependencies(),
                                                            stackPlugin.getExecutions());
    
            final StackPlugins stackPlugins = get();
    
            stackPlugins.addPlugin(plugin);
        }
        else if (cur instanceof StackReportingPlugin) {
            
            final StackReportingPlugin stackReportingPlugin = (StackReportingPlugin)cur;
            
            final MavenReportPlugin reportingPlugin = new MavenReportPlugin(
                                                            stackReportingPlugin.makeModuleId(),
                                                            new MavenPluginConfiguration(
                                                                    stackReportingPlugin.getInherited(),
                                                                    stackReportingPlugin.getConfiguration()),
                                                            stackReportingPlugin.getReportSets());
            
            final StackReportingPlugins stackReportingPlugins = get();
            
            stackReportingPlugins.add(reportingPlugin);
        }
        else {
            throw new IllegalStateException();
        }
    }

    @Override
    public final void onPluginsEnd(Context context) {

        final StackBase cur = pop();
        
        if (cur instanceof StackPlugins) {
            final StackPlugins stackPlugins = (StackPlugins)cur;
    
            final PluginsSetter pluginsSetter = get();
    
            pluginsSetter.setPlugins(stackPlugins.getPlugins());
        }
        else if (cur instanceof StackReportingPlugins) {
            
            final StackReportingPlugins stackReportingPlugins = (StackReportingPlugins)cur;
            
            final StackReporting stackReporting = get();
            
            stackReporting.setPlugins(stackReportingPlugins.getPlugins());
        }
        else {
            throw new IllegalStateException();
        }
    }

    @Override
    public final void onExtensionsStart(Context context) {

        final StackBase cur = get();
        
        if (cur instanceof StackBuild) {
            push(new StackExtensionsList(context));
        }
        else if (cur instanceof StackPlugin) {
            push(new StackPluginExtensions(context));
        }
    }


    @Override
    public final void onExtensionStart(Context context) {
        push(new StackExtension(context));
    }

    @Override
    public final void onExtensionEnd(Context context) {

        final StackExtension stackExtension = pop();

        final StackExtensionsList stackExtensions = get();

        stackExtensions.addExtension(new MavenExtension(stackExtension.makeModuleId()));
    }

    @Override
    public final void onExtensionsEnd(Context context) {

        final StackBase cur = get();
        
        if (cur instanceof StackExtensionsList) {

            final StackExtensionsList stackExtensions = pop();

            final StackBuild stackBuild = get();

            stackBuild.setExtensions(stackExtensions.getExtensions());
        }
        else if (cur instanceof StackPluginExtensions) {
            
            final StackPluginExtensions stackExtensions = pop();
            
            final StackPlugin stackPlugin = get();
            
            stackPlugin.setExtensions(stackExtensions.getValue());
        }
    }

    @Override
    public final void onBuildEnd(Context context) {

        final StackBuild stackBuild = pop();

        final MavenBuild build = new MavenBuild(
                stackBuild.getDefaultGoal(),
                stackBuild.getDirectory(),
                stackBuild.getFinalName(),
                stackBuild.getFilters(),
                stackBuild.getOutputDirectory(),
                stackBuild.getSourceDirectory(),
                stackBuild.getScriptSourceDirectory(),
                stackBuild.getTestSourceDirectory(),
                stackBuild.getResources(),
                stackBuild.getTestResources(),
                stackBuild.getPluginManagement(),
                stackBuild.getPlugins());

        final BuildSetter buildSetter = get();

        buildSetter.setBuild(build);
    }
    
    @Override
    public void onOrganizationStart(Context context) {
        push(new StackOrganization(context));
    }

    @Override
    public void onOrganizationEnd(Context context) {

        final StackOrganization stackOrganization = pop();
        
        final StackProject stackProject = get();
        
        final MavenOrganization organization
            = new MavenOrganization(stackOrganization.getName(), stackOrganization.getUrl());
        
        stackProject.setOrganization(organization);
    }

    @Override
    public void onIssueManagementStart(Context context) {
        push(new StackIssueManagement(context));
    }

    @Override
    public void onSystemStart(Context context) {
        push(new StackSystem(context));
    }

    @Override
    public void onSystemEnd(Context context) {

        final StackSystem stackSystem = pop();
        
        final SystemSetter systemSetter = get();
        
        systemSetter.setSystem(stackSystem.getText());
    }

    @Override
    public void onIssueManagementEnd(Context context) {

        final StackIssueManagement stackIssueManagement = pop();
        
        final StackProject stackProject = get();
        
        final MavenIssueManagement issueManagement
                = new MavenIssueManagement(stackIssueManagement.getSystem(), stackIssueManagement.getUrl());
        
        stackProject.setIssueManagement(issueManagement);
    }

    @Override
    public final void onCiManagementStart(Context context) {
        push(new StackCiManagement(context));
    }

    @Override
    public final void onNotifiersStart(Context context) {
        push(new StackNotifiers(context));
    }

    @Override
    public final void onNotifierStart(Context context) {
        push(new StackNotifier(context));
    }

    @Override
    public final void onTypeStart(Context context) {
        push(new StackType(context));
    }

    @Override
    public final void onTypeEnd(Context context) {

        final StackType stackType = pop();
        
        final StackNotifier stackNotifier = get();
        
        stackNotifier.setType(stackType.getText());
    }

    @Override
    public final void onSendOnErrorStart(Context context) {
        push(new StackSendOnError(context));
    }

    @Override
    public final void onSendOnErrorEnd(Context context) {

        final StackSendOnError stackSendOnError = pop();
        
        final StackNotifier stackNotifier = get();
        
        stackNotifier.setSendOnError(stackSendOnError.getValue());
    }

    @Override
    public final void onSendOnFailureStart(Context context) {
        push(new StackSendOnFailure(context));
    }

    @Override
    public final void onSendOnFailureEnd(Context context) {

        final StackSendOnFailure stackSendOnFailure = pop();
        
        final StackNotifier stackNotifier = get();
        
        stackNotifier.setSendOnFailure(stackSendOnFailure.getValue());
    }

    @Override
    public final void onSendOnSuccessStart(Context context) {
        push(new StackSendOnSuccess(context));
    }

    @Override
    public final void onSendOnSuccessEnd(Context context) {

        final StackSendOnSuccess stackSendOnSuccess = pop();
        
        final StackNotifier stackNotifier = get();
        
        stackNotifier.setSendOnSuccess(stackSendOnSuccess.getValue());
    }

    @Override
    public final void onSendOnWarningStart(Context context) {
        push(new StackSendOnWarning(context));
    }

    @Override
    public final void onSendOnWarningEnd(Context context) {

        final StackSendOnWarning stackSendOnWarning = pop();
        
        final StackNotifier stackNotifier = get();
    
        stackNotifier.setSendOnWarning(stackSendOnWarning.getValue());
    }

    @Override
    public final void onNotifierEnd(Context context) {

        final StackNotifier stackNotifier = pop();
        
        final StackNotifiers stackNotifiers = get();
        
        final MavenNotifier notifier = new MavenNotifier(
                                                stackNotifier.getType(),
                                                stackNotifier.getSendOnError(),
                                                stackNotifier.getSendOnFailure(),
                                                stackNotifier.getSendOnSuccess(),
                                                stackNotifier.getSendOnWarning(),
                                                stackNotifier.getConfiguration());
        
        stackNotifiers.add(notifier);
    }

    @Override
    public final void onNotifiersEnd(Context context) {

        final StackNotifiers stackNotifiers = pop();
        
        final StackCiManagement stackCiManagement = get();
        
        stackCiManagement.setNotifiers(stackNotifiers.getNotifiers());
    }

    @Override
    public final void onCiManagementEnd(Context context) {

        final StackCiManagement stackCiManagement = pop();
        
        final StackProject stackProject = get();
        
        final MavenCiManagement ciManagement = new MavenCiManagement(
                                                        stackCiManagement.getSystem(),
                                                        stackCiManagement.getUrl(),
                                                        stackCiManagement.getNotifiers());
        stackProject.setCiManagement(ciManagement);
    }

    @Override
    public final void onMailingListsStart(Context context) {
        push(new StackMailingLists(context));
    }

    @Override
    public final void onMailingListStart(Context context) {
        push(new StackMailingList(context));
    }

    @Override
    public final void onSubscribeStart(Context context) {
        push(new StackSubscribe(context));
    }

    @Override
    public final void onSubscribeEnd(Context context) {

        final StackSubscribe stackSubscribe = pop();
        
        final StackMailingList stackMailingList = get();
    
        stackMailingList.setSubscribe(stackSubscribe.getText());
    }

    @Override
    public final void onUnsubscribeStart(Context context) {
        push(new StackUnsubscribe(context));
    }

    @Override
    public final void onUnsubscribeEnd(Context context) {

        final StackUnsubscribe stackUnsubscribe = pop();
        
        final StackMailingList stackMailingList = get();
        
        stackMailingList.setUnsubscribe(stackUnsubscribe.getText());
    }

    @Override
    public final void onPostStart(Context context) {
        push(new StackPost(context));
    }

    @Override
    public final void onPostEnd(Context context) {

        final StackPost stackPost = pop();
        
        final StackMailingList stackMailingList = get();
        
        stackMailingList.setPost(stackPost.getText());
    }

    @Override
    public final void onArchiveStart(Context context) {
        push(new StackArchive(context));
    }

    @Override
    public final void onArchiveEnd(Context context) {

        final StackArchive stackArchive = pop();
        
        final StackMailingList stackMailingList = get();
        
        stackMailingList.setArchive(stackArchive.getText());
    }

    @Override
    public final void onOtherArchivesStart(Context context) {
        push(new StackOtherArchives(context));
    }

    @Override
    public final void onOtherArchiveStart(Context context) {
        push(new StackOtherArchive(context));
    }

    @Override
    public final void onOtherArchiveEnd(Context context) {

        final StackOtherArchive stackOtherArchive = pop();
        
        final StackOtherArchives stackOtherArchives = get();
        
        stackOtherArchives.add(stackOtherArchive.getText());
    }

    @Override
    public final void onOtherArchivesEnd(Context context) {

        final StackOtherArchives stackOtherArchives = pop();
        
        final StackMailingList stackMailingList = get();
    
        stackMailingList.setOtherArchives(stackOtherArchives.getOtherArchives());
    }

    @Override
    public final void onMailingListEnd(Context context) {

        final StackMailingList stackMailingList = pop();
        
        final StackMailingLists stackMailingLists = get();
        
        final MavenMailingList mailingList = new MavenMailingList(
                                                stackMailingList.getName(),
                                                stackMailingList.getSubscribe(),
                                                stackMailingList.getUnsubscribe(),
                                                stackMailingList.getPost(),
                                                stackMailingList.getArchive(),
                                                stackMailingList.getOtherArchives());
        
        stackMailingLists.add(mailingList);
    }

    @Override
    public final void onMailingListsEnd(Context context) {

        final StackMailingLists stackMailingLists = pop();
        
        final StackProject stackProject = get();
        
        stackProject.setMailingLists(stackMailingLists.getMailingLists());
    }

    @Override
    public final void onScmStart(Context context) {
        push(new StackScm(context));
    }

    @Override
    public final void onConnectionStart(Context context) {
        push(new StackConnection(context));
    }

    @Override
    public final void onConnectionEnd(Context context) {

        final StackConnection stackConnection = pop();
        
        final StackScm stackScm = get();
        
        stackScm.setConnection(stackConnection.getText());
    }

    @Override
    public final void onDeveloperConnectionStart(Context context) {
        push(new StackDeveloperConnection(context));
    }

    @Override
    public final void onDeveloperConnectionEnd(Context context) {

        final StackDeveloperConnection stackDeveloperConnection = pop();
        
        final StackScm stackScm = get();
        
        stackScm.setDeveloperConnection(stackDeveloperConnection.getText());
    }

    @Override
    public final void onTagStart(Context context) {
        push(new StackTag(context));
    }

    @Override
    public final void onTagEnd(Context context) {

        final StackTag stackTag = pop();
        
        final StackScm stackScm = get();
        
        stackScm.setTag(stackTag.getText());
    }

    @Override
    public final void onScmEnd(Context context) {

        final StackScm stackScm = pop();
        
        final StackProject stackProject = get();
        
        final MavenScm mavenScm = new MavenScm(
                                    stackScm.getConnection(),
                                    stackScm.getDeveloperConnection(),
                                    stackScm.getTag(),
                                    stackScm.getUrl());
        
        stackProject.setScm(mavenScm);
    }

    @Override
    public void onRepositoriesStart(Context context) {
        
        push(new StackRepositories<>(context));
    }

    @Override
    public void onRepositoryStart(Context context) {
        
        push(new StackRepository(context));
    }

    @Override
    public void onReleasesStart(Context context) {

        push(new StackFiles(context));
    }

    @Override
    public void onReleasesEnd(Context context) {

        final StackFiles stackFiles = pop();
        
        final StackRepository stackRepository = get();
        
        final MavenReleases releases = new MavenReleases(
                                stackFiles.getEnabled(),
                                stackFiles.getUpdatePolicy(),
                                stackFiles.getChecksumPolicy());
        
        stackRepository.setReleases(releases);
    }

    @Override
    public void onEnabledStart(Context context) {

        push(new StackEnabled(context));
        
    }

    @Override
    public void onEnabledEnd(Context context) {

        final StackEnabled stackEnabled = pop();
        
        final StackFiles stackFiles = get();
        
        stackFiles.setEnabled(stackEnabled.getValue());
    }

    @Override
    public void onUpdatePolicyStart(Context context) {

        push(new StackUpdatePolicy(context));

    }

    @Override
    public void onUpdatePolicyEnd(Context context) {

        final StackUpdatePolicy stackUpdatePolicy = pop();
        
        final StackFiles stackFiles = get();
        
        stackFiles.setUpdatePolicy(stackUpdatePolicy.getText());
    }

    @Override
    public void onChecksumPolicyStart(Context context) {

        push(new StackChecksumPolicy(context));
    }

    @Override
    public void onChecksumPolicyEnd(Context context) {

        final StackChecksumPolicy stackChecksumPolicy = pop();
        
        final StackFiles stackFiles = get();
        
        stackFiles.setChecksumPolicy(stackChecksumPolicy.getText());
    }

    @Override
    public void onSnapshotsStart(Context context) {

        push(new StackFiles(context));
    }

    @Override
    public void onSnapshotsEnd(Context context) {

        final StackFiles stackFiles = pop();
        
        final StackRepository stackRepository = get();
        
        final MavenSnapshots snapshots = new MavenSnapshots(
                stackFiles.getEnabled(),
                stackFiles.getUpdatePolicy(),
                stackFiles.getChecksumPolicy());
        
        stackRepository.setSnapshots(snapshots);
    }

    @Override
    public void onNameStart(Context context) {

        push(new StackName(context));
    }

    @Override
    public void onNameEnd(Context context) {

        final StackName stackName = pop();
        
        final NameSetter nameSetter = get();
    
        nameSetter.setName(stackName.getText());
    }

    @Override
    public void onIdStart(Context context) {

        push(new StackId(context));
    }

    @Override
    public void onIdEnd(Context context) {

        final StackId stackId = pop();
        
        final IdSetter idSetter = get();
        
        idSetter.setId(stackId.getText());
    }

    @Override
    public void onUrlStart(Context context) {

        push(new StackUrl(context));
    }

    @Override
    public void onUrlEnd(Context context) {

        final StackUrl stackUrl = pop();
        
        final UrlSetter urlSetter = get();
        
        urlSetter.setUrl(stackUrl.getText());
    }

    @Override
    public void onLayoutStart(Context context) {

        push(new StackLayout(context));
    }

    @Override
    public void onLayoutEnd(Context context) {

        final StackLayout stackLayout = pop();
        
        final StackRepository stackRepository = get();
        
        stackRepository.setLayout(stackLayout.getText());
    }

    @Override
    public void onRepositoryEnd(Context context) {

        final StackRepository stackRepository = pop();
        
        final StackRepositories<MavenRepository> stackRepositories = get();
        
        final MavenRepository repository = new MavenRepository(
                stackRepository.getReleases(),
                stackRepository.getSnapshots(),
                stackRepository.getName(),
                stackRepository.getId(),
                stackRepository.getUrl(),
                stackRepository.getLayout());
        
        stackRepositories.add(repository);
    }

    @Override
    public void onPluginRepositoriesStart(Context context) {

        push(new StackRepositories<>(context));
    }

    @Override
    public void onPluginRepositoryStart(Context context) {

        push(new StackRepository(context));
    }

    @Override
    public void onRepositoriesEnd(Context context) {

        final StackRepositories<MavenRepository> stackRepositories = pop();
        
        final RepositoriesSetter repositoriesSetter = get();
        
        repositoriesSetter.setRepositories(stackRepositories.getRepositories());
    }

    @Override
    public void onPluginRepositoryEnd(Context context) {

        final StackRepository stackRepository = pop();
        
        final StackRepositories<MavenPluginRepository> stackRepositories = get();
        
        final MavenPluginRepository pluginRepository = new MavenPluginRepository(
                stackRepository.getReleases(),
                stackRepository.getSnapshots(),
                stackRepository.getName(),
                stackRepository.getId(),
                stackRepository.getUrl(),
                stackRepository.getLayout());
        
        stackRepositories.add(pluginRepository);
    }

    @Override
    public void onPluginRepositoriesEnd(Context context) {

        final StackRepositories<MavenPluginRepository> stackRepositories = pop();
        
        final PluginRepositoriesSetter pluginRepositoriesSetter = get();
        
        pluginRepositoriesSetter.setPluginRepositories(stackRepositories.getRepositories());
    }

    @Override
    public void onProfilesStart(Context context) {
        push(new StackProfiles(context));
    }

    @Override
    public void onProfileStart(Context context) {
        push(new StackProfile(context));
    }

    @Override
    public void onActivationStart(Context context) {
        push(new StackActivation(context));
    }

    @Override
    public void onActiveByDefaultStart(Context context) {
        push(new StackActiveByDefault(context));
    }

    @Override
    public void onActiveByDefaultEnd(Context context) {

        final StackActiveByDefault stackActiveByDefault = pop();
        
        final StackActivation stackActivation = get();
        
        stackActivation.setActiveByDefault(stackActiveByDefault.getValue());
    }

    @Override
    public void onJdkStart(Context context) {
        push(new StackJdk(context));
    }

    @Override
    public void onJdkEnd(Context context) {

        final StackJdk stackJdk = pop();
        
        final StackActivation stackActivation = get();
    
        stackActivation.setJdk(stackJdk.getText());
    }

    @Override
    public void onOsStart(Context context) {
        push(new StackOs(context));
    }

    @Override
    public void onFamilyStart(Context context) {
        push(new StackFamily(context));
    }

    @Override
    public void onFamilyEnd(Context context) {

        final StackFamily stackFamily = pop();
        
        final StackOs stackOs = get();
        
        stackOs.setFamily(stackFamily.getText());
    }

    @Override
    public void onArchStart(Context context) {
        push(new StackArch(context));
    }

    @Override
    public void onArchEnd(Context context) {

        final StackArch stackArch = pop();
        
        final StackOs stackOs = get();
        
        stackOs.setArch(stackArch.getText());
    }

    @Override
    public void onOsEnd(Context context) {

        final StackOs stackOs = pop();
        
        final StackActivation stackActivation = get();
    
        final MavenActivationOS os = new MavenActivationOS(
                stackOs.getName(),
                stackOs.getFamily(),
                stackOs.getArch(),
                stackOs.getVersion());
        
        stackActivation.setOs(os);
    }

    @Override
    public void onPropertyStart(Context context) {
        push(new StackActivationProperty(context));
    }

    @Override
    public void onValueStart(Context context) {
        push(new StackValue(context));
    }

    @Override
    public void onValueEnd(Context context) {

        final StackValue stackValue = pop();
        
        final StackActivationProperty stackActivationProperty = get();
    
        stackActivationProperty.setValue(stackValue.getText());
    }

    @Override
    public void onPropertyEnd(Context context) {

        final StackActivationProperty stackActivationProperty = pop();
        
        final StackActivation stackActivation = get();
        
        final MavenActivationProperty activationProperty
                = new MavenActivationProperty(
                        stackActivationProperty.getName(),
                        stackActivationProperty.getValue());
    
        stackActivation.setProperty(activationProperty);
    }

    @Override
    public void onFileStart(Context context) {
        push(new StackFile(context));
    }

    @Override
    public void onExistsStart(Context context) {
        push(new StackExists(context));
    }

    @Override
    public void onExistsEnd(Context context) {

        final StackExists stackExists = pop();
        
        final StackFile stackFile = get();
        
        stackFile.setExists(stackExists.getText());
    }

    @Override
    public void onMissingStart(Context context) {
        push(new StackMissing(context));
    }

    @Override
    public void onMissingEnd(Context context) {

        final StackMissing stackMissing = pop();
        
        final StackFile stackFile = get();
        
        stackFile.setMissing(stackMissing.getText());
    }

    @Override
    public void onFileEnd(Context context) {

        final StackFile stackFile = pop();
        
        final StackActivation stackActivation = get();
        
        final MavenActivationFile activationFile
                = new MavenActivationFile(stackFile.getExists(), stackFile.getMissing());
    
        stackActivation.setFile(activationFile);
    }

    @Override
    public void onActivationEnd(Context context) {

        final StackActivation stackActivation = pop();
        
        final StackProfile stackProfile = get();
        
        final MavenActivation activation = new MavenActivation(
                                                stackActivation.getActiveByDefault(),
                                                stackActivation.getJdk(),
                                                stackActivation.getOs(),
                                                stackActivation.getProperty(),
                                                stackActivation.getFile());
        
        stackProfile.setActivation(activation);
    }

    @Override
    public void onProfileEnd(Context context) {

        final StackProfile stackProfile = pop();
        
        final StackProfiles stackProfiles = get();
        
        final MavenProfile profile = new MavenProfile(
                                            stackProfile.getId(),
                                            stackProfile.getActivation(),
                                            stackProfile.getCommon().makeMavenCommon());
        
        stackProfiles.add(profile);
    }

    @Override
    public void onProfilesEnd(Context context) {

        final StackProfiles stackProfiles = pop();
        
        final StackProject stackProject = get();
        
        stackProject.setProfiles(stackProfiles.getProfiles());
    }
}
