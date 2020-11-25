package com.neaterbits.build.buildsystem.maven.plexus.components.parse;

import com.neaterbits.build.buildsystem.maven.common.parse.configuration.BasePlexusConfigurationStackEventListener;
import com.neaterbits.build.buildsystem.maven.plexus.components.model.PlexusComponent;
import com.neaterbits.build.buildsystem.maven.plexus.components.model.PlexusComponentSet;
import com.neaterbits.build.buildsystem.maven.plexus.components.model.PlexusRequirement;
import com.neaterbits.build.buildsystem.maven.plexus.components.parse.common.RoleHintSetter;
import com.neaterbits.build.buildsystem.maven.plexus.components.parse.common.RoleSetter;
import com.neaterbits.build.buildsystem.maven.plexus.components.parse.common.StackField;
import com.neaterbits.build.buildsystem.maven.plexus.components.parse.common.StackFieldName;
import com.neaterbits.build.buildsystem.maven.plexus.components.parse.common.StackImplementation;
import com.neaterbits.build.buildsystem.maven.plexus.components.parse.common.StackRequirement;
import com.neaterbits.build.buildsystem.maven.plexus.components.parse.common.StackRole;
import com.neaterbits.build.buildsystem.maven.plexus.components.parse.common.StackRoleHint;
import com.neaterbits.util.parse.context.Context;

final class StackPlexusComponentDescriptorEventListener
        extends BasePlexusConfigurationStackEventListener
        implements PlexusComponentDescriptorEventListener {

    private PlexusComponentSet componentSet;
    
    @Override
    public void onComponentSetStart(Context context) {
        push(new StackComponentSet(context));
    }

    @Override
    public void onComponentsStart(Context context) {
        push(new StackComponents(context));
    }

    @Override
    public void onComponentStart(Context context) {
        push(new StackComponent(context));
    }

    @Override
    public void onImplementationStart(Context context) {
        push(new StackImplementation(context));
    }

    @Override
    public void onImplementationEnd(Context context) {

        final StackImplementation stackImplementation = pop();
        
        final StackComponent stackComponent = get();
    
        stackComponent.setImplementation(stackImplementation.getText());
    }

    @Override
    public void onInstantiationStrategyStart(Context context) {
        push(new StackInstantiationStrategy(context));
    }

    @Override
    public void onInstantiationStrategyEnd(Context context) {

        final StackInstantiationStrategy stackInstantiationStrategy = pop();
        
        final StackComponent stackComponent = get();
        
        stackComponent.setInstantiationStrategy(stackInstantiationStrategy.getText());
    }

    @Override
    public void onRequirementsStart(Context context) {
        push(new StackRequirements(context));
    }

    @Override
    public void onRequirementStart(Context context) {
        push(new StackRequirement(context));
    }

    @Override
    public void onRoleStart(Context context) {
        push(new StackRole(context));
    }

    @Override
    public void onRoleEnd(Context context) {

        final StackRole stackRole = pop();
        
        final RoleSetter roleSetter = get();

        roleSetter.setRole(stackRole.getText());
    }

    @Override
    public void onRoleHintStart(Context context) {
        push(new StackRoleHint(context));
    }

    @Override
    public void onRoleHintEnd(Context context) {

        final StackRoleHint stackRoleHint = pop();
        
        final RoleHintSetter roleHintSetter = get();
        
        roleHintSetter.setRoleHint(stackRoleHint.getText());
    }

    @Override
    public void onFieldNameStart(Context context) {
        push(new StackFieldName(context));
    }

    @Override
    public void onFieldNameEnd(Context context) {

        final StackFieldName stackFieldName = pop();
        
        final StackRequirement stackRequirement = get();
    
        stackRequirement.setFieldName(stackFieldName.getText());
    }

    @Override
    public void onFieldStart(Context context) {
        push(new StackField(context));
    }

    @Override
    public void onFieldEnd(Context context) {

        final StackField stackField = pop();
        
        final StackRequirement stackRequirement = get();
        
        stackRequirement.setField(stackField.getText());
    }

    @Override
    public void onRequirementEnd(Context context) {

        final StackRequirement stackRequirement = pop();
        
        final StackRequirements stackRequirements = get();
    
        final PlexusRequirement requirement = new PlexusRequirement(
                                                    stackRequirement.getRole(),
                                                    stackRequirement.getRoleHint(),
                                                    stackRequirement.getFieldName(),
                                                    stackRequirement.getField());
    
        stackRequirements.add(requirement);
    }

    @Override
    public void onRequirementsEnd(Context context) {

        final StackRequirements stackRequirements = pop();
        
        final StackComponent stackComponent = get();
        
        stackComponent.setRequirements(stackRequirements.getRequirements());
    }

    @Override
    public void onComponentEnd(Context context) {

        final StackComponent stackComponent = pop();
        
        final StackComponents stackComponents = get();
        
        final PlexusComponent component = new PlexusComponent(
                                                    stackComponent.getRole(),
                                                    stackComponent.getRoleHint(),
                                                    stackComponent.getImplementation(),
                                                    stackComponent.getInstantiationStrategy(),
                                                    stackComponent.getRequirements());
        stackComponents.add(component);
    }

    @Override
    public void onComponentsEnd(Context context) {

        final StackComponents stackComponents = pop();
        
        final StackComponentSet stackComponentSet = get();

        stackComponentSet.addAll(stackComponents.getComponents());
    }

    @Override
    public void onComponentSetEnd(Context context) {

        final StackComponentSet stackComponentSet = pop();
        
        this.componentSet = new PlexusComponentSet(stackComponentSet.getComponents());
    }

    PlexusComponentSet getComponentSet() {
        return componentSet;
    }
}
