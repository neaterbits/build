package com.neaterbits.build.buildsystem.maven.parse;

import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.elements.MavenBuild;
import com.neaterbits.build.buildsystem.maven.elements.MavenDependency;
import com.neaterbits.build.buildsystem.maven.elements.MavenExtension;
import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.elements.MavenReporting;
import com.neaterbits.util.ArrayStack;
import com.neaterbits.util.Stack;
import com.neaterbits.util.parse.context.Context;

abstract class BaseStackPomEventListener implements PomEventListener {

    private final Stack<StackBase> stack;

    BaseStackPomEventListener() {

        this.stack = new ArrayStack<>();
    }
    
    private void push(StackBase frame) {
        Objects.requireNonNull(frame);

        // System.out.println("push: " + frame.getClass().getSimpleName());

        stack.push(frame);
    }

    @SuppressWarnings("unchecked")
    final <T> T pop() {
        final T frame = (T)stack.pop();

        // System.out.println("pop: " + frame.getClass().getSimpleName());

        return frame;
    }

    @SuppressWarnings("unchecked")
    private <T> T get() {
        return (T)stack.get();
    }

    @Override
    public final void onProjectStart(Context context) {
        push(new StackProject(context));
    }

    @Override
    public final void onText(Context context, String text) {

        final StackBase stackBase = get();

        if (stackBase instanceof StackText) {
            final StackText stackText = (StackText)stackBase;

            stackText.setText(text.trim());
        }
    }

    @Override
    public final void onGroupIdStart(Context context) {

        push(new StackGroupId(context));

    }

    @Override
    public final void onGroupIdEnd(Context context) {

        final StackGroupId stackGroupId = pop();

        final EntitySetter entitySetter = get();

        entitySetter.setGroupId(stackGroupId.getText());
    }

    @Override
    public final void onArtifactIdStart(Context context) {

        push(new StackArtifactId(context));
    }

    @Override
    public final void onArtifactIdEnd(Context context) {

        final StackArtifactId stackArtifactId = pop();

        final EntitySetter entitySetter = get();

        entitySetter.setArtifactId(stackArtifactId.getText());
    }

    @Override
    public final void onVersionStart(Context context) {

        push(new StackVersion(context));

    }

    @Override
    public final void onVersionEnd(Context context) {

        final StackVersion stackVersion = pop();

        final EntitySetter entitySetter = get();

        entitySetter.setVersion(stackVersion.getText());
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
    public void onUnknownTagStart(Context context, String name) {

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
    public final void onDependenciesStart(Context context) {
        push(new StackDependencies(context));
    }

    @Override
    public final void onDependencyStart(Context context) {
        push(new StackDependency(context));
    }

    @Override
    public final void onScopeStart(Context context) {
        push(new StackScope(context));
    }

    @Override
    public final void onScopeEnd(Context context) {

        final StackScope stackScope = pop();

        final StackDependency stackDependency = get();

        stackDependency.setScope(stackScope.getText());
    }

    @Override
    public final void onOptionalStart(Context context) {
        push(new StackOptional(context));
    }

    @Override
    public final void onOptionalEnd(Context context) {

        final StackOptional stackOptional = pop();

        final StackDependency stackDependency = get();

        stackDependency.setOptional(stackOptional.getText());
    }

    @Override
    public final void onDependencyEnd(Context context) {

        final StackDependency stackDependency = pop();
        
        final MavenDependency dependency = new MavenDependency(
                stackDependency.makeModuleId(),
                stackDependency.getPackaging(),
                stackDependency.getScope(),
                stackDependency.getOptional());

        final StackDependencies stackDependencies = get();

        stackDependencies.addDependency(dependency);
    }


    @Override
    public final void onDependenciesEnd(Context context) {

        final StackDependencies stackDependencies = pop();

        final StackProject stackProject = get();

        stackProject.setDependencies(stackDependencies.getDependencies());
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

        final MavenPlugin mavenPlugin = new MavenPlugin(stackPlugin.makeModuleId(), null);

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
