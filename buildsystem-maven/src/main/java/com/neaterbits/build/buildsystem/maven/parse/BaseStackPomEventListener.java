package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenActivation;
import com.neaterbits.build.buildsystem.maven.elements.MavenActivationFile;
import com.neaterbits.build.buildsystem.maven.elements.MavenActivationOS;
import com.neaterbits.build.buildsystem.maven.elements.MavenActivationProperty;
import com.neaterbits.build.buildsystem.maven.elements.MavenBuild;
import com.neaterbits.build.buildsystem.maven.elements.MavenExtension;
import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.elements.MavenPluginRepository;
import com.neaterbits.build.buildsystem.maven.elements.MavenProfile;
import com.neaterbits.build.buildsystem.maven.elements.MavenReleases;
import com.neaterbits.build.buildsystem.maven.elements.MavenReporting;
import com.neaterbits.build.buildsystem.maven.elements.MavenRepository;
import com.neaterbits.build.buildsystem.maven.elements.MavenResource;
import com.neaterbits.build.buildsystem.maven.elements.MavenSnapshots;
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
        
        if (cur instanceof StackProperties) {
            push(new StackProperty(context, name));
        }
    }

    @Override
    public void onUnknownTagEnd(Context context, String name) {
        
        final Object cur = get();
        
        if (cur instanceof StackProperty) {
            final StackProperty stackProperty = pop();
            
            final StackProperties stackProperties = get();
            
            stackProperties.add(stackProperty.getName(), stackProperty.getText());
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
    public final void onReportingEnd(Context context) {

        final StackReporting stackReporting = pop();

        final MavenReporting reporting = new MavenReporting(
                                            stackReporting.getDirectory(),
                                            stackReporting.getFinalName(),
                                            stackReporting.getResources(),
                                            stackReporting.getTestResources(),
                                            stackReporting.getPluginManagement(),
                                            stackReporting.getPlugins());

        final StackProject stackProject = get();

        stackProject.setReporting(reporting);
    }

    @Override
    public final void onBuildStart(Context context) {
        push(new StackBuild(context));
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
        push(new StackPlugins(context));
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
        push(new StackPlugin(context));
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
    public void onExecutionEnd(Context context) {

        pop();
        
    }

    @Override
    public void onExecutionsEnd(Context context) {

        pop();
        
    }

    @Override
    public final void onPluginEnd(Context context) {

        final StackPlugin stackPlugin = pop();

        final MavenPlugin mavenPlugin = new MavenPlugin(stackPlugin.makeModuleId());

        final StackPlugins stackPlugins = get();

        stackPlugins.addPlugin(mavenPlugin);
    }

    @Override
    public final void onPluginsEnd(Context context) {

        final StackPlugins stackPlugins = pop();

        final PluginsSetter pluginsSetter = get();

        pluginsSetter.setPlugins(stackPlugins.getPlugins());
    }

    @Override
    public final void onExtensionsStart(Context context) {

        if (get() instanceof StackBuild) {
            push(new StackExtensions(context));
        }
    }


    @Override
    public final void onExtensionStart(Context context) {
        push(new StackExtension(context));
    }

    @Override
    public final void onExtensionEnd(Context context) {

        final StackExtension stackExtension = pop();

        final StackExtensions stackExtensions = get();

        stackExtensions.addExtension(new MavenExtension(stackExtension.makeModuleId()));
    }

    @Override
    public final void onExtensionsEnd(Context context) {

        if (get() instanceof StackExtensions) {

            final StackExtensions stackExtensions = pop();

            final StackBuild stackBuild = get();

            stackBuild.setExtensions(stackExtensions.getExtensions());
        }
    }

    @Override
    public final void onBuildEnd(Context context) {

        final StackBuild stackBuild = pop();

        final MavenBuild build = new MavenBuild(
                stackBuild.getDirectory(),
                stackBuild.getFinalName(),
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
