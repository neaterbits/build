package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenBuild;
import com.neaterbits.build.buildsystem.maven.elements.MavenExtension;
import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.elements.MavenPluginRepository;
import com.neaterbits.build.buildsystem.maven.elements.MavenReleases;
import com.neaterbits.build.buildsystem.maven.elements.MavenReporting;
import com.neaterbits.build.buildsystem.maven.elements.MavenRepository;
import com.neaterbits.build.buildsystem.maven.elements.MavenSnapshots;
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

        final StackProject stackProject = get();

        stackProject.setSubModules(stackModules.getModules());
    }

    @Override
    public final void onReportingStart(Context context) {

        push(new StackReporting(context));
    }

    @Override
    public final void onReportingEnd(Context context) {

        final StackReporting stackReporting = pop();

        final MavenReporting reporting = new MavenReporting(stackReporting.getPlugins());

        final StackProject stackProject = get();

        stackProject.setReporting(reporting);
    }

    @Override
    public final void onBuildStart(Context context) {
        push(new StackBuild(context));
    }

    @Override
    public final void onPluginsStart(Context context) {
        push(new StackPlugins(context));
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

        final MavenBuild build = new MavenBuild(stackBuild.getPlugins());

        final StackProject stackProject = get();

        stackProject.setBuild(build);
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
        
        final StackProject stackProject = get();
        
        stackProject.setRepositories(stackRepositories.getRepositories());
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
        
        final StackProject stackProject = get();
        
        stackProject.setPluginRepositories(stackRepositories.getRepositories());
    }
}
