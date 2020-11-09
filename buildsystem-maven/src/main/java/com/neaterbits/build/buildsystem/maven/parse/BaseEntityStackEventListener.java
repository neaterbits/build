package com.neaterbits.build.buildsystem.maven.parse;

import com.neaterbits.build.buildsystem.maven.elements.MavenDependency;
import com.neaterbits.util.parse.context.Context;

public abstract class BaseEntityStackEventListener
    extends BaseStackEventListener
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

        final EntitySetter entitySetter = get();

        entitySetter.setVersion(stackVersion.getText());
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
                stackDependency.getType(),
                stackDependency.getOptional());

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
