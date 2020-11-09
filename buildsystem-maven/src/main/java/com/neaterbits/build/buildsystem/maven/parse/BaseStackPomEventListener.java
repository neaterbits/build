package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenBuild;
import com.neaterbits.build.buildsystem.maven.elements.MavenExtension;
import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.elements.MavenReporting;
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
}
