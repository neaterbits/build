package com.neaterbits.build.buildsystem.maven.common.parse;

import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.common.model.MavenExclusion;
import com.neaterbits.build.buildsystem.maven.common.parse.configuration.BasePlexusConfigurationStackEventListener;
import com.neaterbits.build.buildsystem.maven.common.parse.listeners.DependenciesListener;
import com.neaterbits.build.buildsystem.maven.common.parse.listeners.EntityEventListener;
import com.neaterbits.util.parse.context.Context;

public abstract class BaseEntityStackEventListener
    extends BasePlexusConfigurationStackEventListener
    implements EntityEventListener, DependenciesListener {

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

        final VersionSetter versionSetter = get();

        versionSetter.setVersion(stackVersion.getText());
    }

    @Override
    public void onPackagingStart(Context context) {
        push(new StackPackaging(context));
    }

    @Override
    public void onPackagingEnd(Context context) {

        final StackPackaging stackPackaging = pop();
        
        final EntitySetter entitySetter = get();
        
        entitySetter.setPackaging(stackPackaging.getText());
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
    public final void onClassifierStart(Context context) {
        push(new StackClassifier(context));
    }

    @Override
    public final void onClassifierEnd(Context context) {

        final StackClassifier stackClassifier = pop();
        
        final StackDependency stackDependency = get();
        
        stackDependency.setClassifier(stackClassifier.getText());
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
    public void onExclusionsStart(Context context) {

        push(new StackExclusions(context));
        
    }

    @Override
    public void onExclusionStart(Context context) {

        push(new StackExclusion(context));
        
    }

    @Override
    public void onExclusionEnd(Context context) {

        final StackExclusion stackExclusion = pop();
        
        final StackExclusions stackExclusions = get();
        
        final MavenExclusion exclusion = new MavenExclusion(stackExclusion.getGroupId(), stackExclusion.getArtifactId());
        
        stackExclusions.add(exclusion);
        
    }

    @Override
    public void onExclusionsEnd(Context context) {

        final StackExclusions stackExclusions = pop();
        
        final StackDependency stackDependency = get();
        
        stackDependency.setExclusions(stackExclusions.getExclusions());
    }

    @Override
    public final void onDependencyEnd(Context context) {

        final StackDependency stackDependency = pop();
        
        final MavenDependency dependency = new MavenDependency(
                stackDependency.makeModuleId(),
                stackDependency.getType(),
                stackDependency.getClassifier(),
                stackDependency.getScope(),
                stackDependency.getOptional(),
                stackDependency.getExclusions());

        final StackDependencies stackDependencies = get();

        stackDependencies.addDependency(dependency);
    }


    @Override
    public final void onDependenciesEnd(Context context) {

        final StackDependencies stackDependencies = pop();

        final DependenciesSetter dependenciesSetter = get();

        dependenciesSetter.setDependencies(stackDependencies.getDependencies());
    }
}
