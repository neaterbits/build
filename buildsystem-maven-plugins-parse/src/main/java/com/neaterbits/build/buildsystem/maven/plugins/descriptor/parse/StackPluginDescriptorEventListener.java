package com.neaterbits.build.buildsystem.maven.plugins.descriptor.parse;

import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.build.buildsystem.maven.common.parse.BaseEntityStackEventListener;
import com.neaterbits.build.buildsystem.maven.common.parse.TypeSetter;
import com.neaterbits.build.buildsystem.maven.plexus.components.parse.common.StackField;
import com.neaterbits.build.buildsystem.maven.plexus.components.parse.common.StackFieldName;
import com.neaterbits.build.buildsystem.maven.plexus.components.parse.common.StackImplementation;
import com.neaterbits.build.buildsystem.maven.plexus.components.parse.common.StackIsolatedRealm;
import com.neaterbits.build.buildsystem.maven.plexus.components.parse.common.StackRequirement;
import com.neaterbits.build.buildsystem.maven.plexus.components.parse.common.StackRole;
import com.neaterbits.build.buildsystem.maven.plexus.components.parse.common.StackRoleHint;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoRequirement;
import com.neaterbits.build.buildsystem.maven.xml.XMLAttribute;
import com.neaterbits.util.parse.context.Context;

final class StackPluginDescriptorEventListener
    extends BaseEntityStackEventListener
    implements PluginDescriptorEventListener {

    private MavenPluginDescriptor pluginDescriptor;
    
    @Override
    public boolean inConfigurationElement(boolean startTag) {
    
        final boolean inConfiguration;
        
        if (hasEntries()) {
            final StackBase cur = get();
        
            inConfiguration = 
                       (startTag && cur instanceof StackConfiguration) 
                    || (!startTag && cur instanceof StackParamName);
        }
        else {
            inConfiguration = false;
        }
        
        return inConfiguration;
    }

    @Override
    public void onPluginStart(Context context) {
        push(new StackPluginDescriptor(context));
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
    public void onDescriptionStart(Context context) {

        push(new StackDescription(context));

    }

    @Override
    public void onDescriptionEnd(Context context) {

        final StackDescription stackDescription = pop();
        
        final DescriptionSetter descriptionSetter = get();
    
        descriptionSetter.setDescription(stackDescription.getText());
    }

    @Override
    public void onGoalPrefixStart(Context context) {

        push(new StackGoalPrefix(context));
        
    }

    @Override
    public void onGoalPrefixEnd(Context context) {

        final StackGoalPrefix stackGoalPrefix = pop();
        
        final StackPluginDescriptor stackPluginDescriptor = get();

        stackPluginDescriptor.setGoalPrefix(stackGoalPrefix.getText());
    }

    @Override
    public void onIsolatedRealmStart(Context context) {

        push(new StackIsolatedRealm(context));
        
    }

    @Override
    public void onIsolatedRealmEnd(Context context) {

        final StackIsolatedRealm stackIsolatedRealm = pop();
        
        final StackPluginDescriptor stackPluginDescriptor = get();
        
        if (stackIsolatedRealm.getValue() != null) {
            stackPluginDescriptor.setIsolatedRealm(stackIsolatedRealm.getValue());
        }
    }

    @Override
    public void onInheritedByDefaultStart(Context context) {

        push(new StackInheritedByDefault(context));
        
    }

    @Override
    public void onInheritedByDefaultEnd(Context context) {
        
        final StackInheritedByDefault stackInheritedByDefault = pop();
        
        final InheritedByDefaultSetter inheritedByDefaultSetter = get();
        
        if (stackInheritedByDefault.getValue() != null) {
            inheritedByDefaultSetter.setInheritedByDefault(stackInheritedByDefault.getValue());
        }
    }

    @Override
    public void onMojosStart(Context context) {

        // Just add directly to StackPluginDescriptor
    }

    @Override
    public void onMojoStart(Context context) {

        push(new StackMojo(context));
        
    }

    @Override
    public void onGoalStart(Context context) {
        push(new StackGoal(context));
    }

    @Override
    public void onGoalEnd(Context context) {

        final StackGoal stackGoal = pop();
        
        final StackMojo stackMojo = get();
        
        stackMojo.setGoal(stackGoal.getText());
    }

    @Override
    public void onImplementationStart(Context context) {

        push(new StackImplementation(context));
        
    }

    @Override
    public void onImplementationEnd(Context context) {
        
        final StackImplementation stackImplementation = pop();
        
        final ImplementationSetter implementationSetter = get();
        
        implementationSetter.setImplementation(stackImplementation.getText());
    }

    @Override
    public void onLanguageStart(Context context) {

        push(new StackLanguage(context));
        
    }

    @Override
    public void onLanguageEnd(Context context) {

        final StackLanguage stackLanguage = pop();
        
        final StackMojo stackMojo = get();
        
        stackMojo.setLanguage(stackLanguage.getText());
    }

    @Override
    public void onPhaseStart(Context context) {

        push(new StackPhase(context));
        
    }

    @Override
    public void onPhaseEnd(Context context) {

        final StackPhase stackPhase = pop();
        
        final StackMojo stackMojo = get();
    
        stackMojo.setPhase(stackPhase.getText());
    }

    @Override
    public void onExecutePhaseStart(Context context) {

        push(new StackExecutePhase(context));
        
    }

    @Override
    public void onExecutePhaseEnd(Context context) {

        final StackExecutePhase stackExecutePhase = pop();
        
        final StackMojo stackMojo = get();
        
        stackMojo.setExecutePhase(stackExecutePhase.getText());
    }

    @Override
    public void onExecuteGoalStart(Context context) {

        push(new StackExecuteGoal(context));
        
    }

    @Override
    public void onExecuteGoalEnd(Context context) {

        final StackExecuteGoal stackExecuteGoal = pop();
        
        final StackMojo stackMojo = get();
        
        stackMojo.setExecuteGoal(stackExecuteGoal.getText());
    }

    @Override
    public void onExecuteLifecycleStart(Context context) {

        push(new StackExecuteLifecycle(context));
        
    }

    @Override
    public void onExecuteLifecycleEnd(Context context) {

        final StackExecuteLifecycle stackExecuteLifecycle = pop();
        
        final StackMojo stackMojo = get();
    
        stackMojo.setExecuteLifecycle(stackExecuteLifecycle.getText());
    }

    @Override
    public void onRequiresDependencyResolutionStart(Context context) {

        push(new StackRequiresDependencyResolution(context));
        
    }

    @Override
    public void onRequiresDependencyResolutionEnd(Context context) {

        final StackRequiresDependencyResolution stackRequiresDependencyResolution = pop();
        
        final StackMojo stackMojo = get();
        
        stackMojo.setRequiresDependencyResolution(stackRequiresDependencyResolution.getText());
    }

    @Override
    public void onRequiresDependencyCollectionStart(Context context) {

        push(new StackRequiresDependencyCollection(context));
    }

    @Override
    public void onRequiresDependencyCollectionEnd(Context context) {

        final StackRequiresDependencyCollection stackRequiresDependencyCollection = pop();
        
        final StackMojo stackMojo = get();
        
        stackMojo.setRequiresDependencyCollection(stackRequiresDependencyCollection.getText());
        
    }

    @Override
    public void onRequiresDirectInvocationStart(Context context) {

        push(new StackRequiresDirectInvocation(context));

    }

    @Override
    public void onRequiresDirectInvocationEnd(Context context) {

        final StackRequiresDirectInvocation stackRequiresDirectInvocation = pop();
        
        final StackMojo stackMojo = get();
        
        stackMojo.setRequiresDirectInvocation(stackRequiresDirectInvocation.getValue());
    }

    @Override
    public void onRequiresProjectStart(Context context) {

        push(new StackRequiresProject(context));
        
    }

    @Override
    public void onRequiresProjectEnd(Context context) {

        final StackRequiresProject stackRequiresProject = pop();
        
        final StackMojo stackMojo = get();
        
        stackMojo.setRequiresProject(stackRequiresProject.getValue());
    }

    @Override
    public void onRequiresReportsStart(Context context) {

        push(new StackRequiresReports(context));

    }

    @Override
    public void onRequiresReportsEnd(Context context) {

        final StackRequiresReports stackRequiresReports = pop();
        
        final StackMojo stackMojo = get();
        
        stackMojo.setRequiresReports(stackRequiresReports.getValue());
    }

    @Override
    public void onRequiresOnlineStart(Context context) {

        push(new StackRequiresOnline(context));

    }

    @Override
    public void onRequiresOnlineEnd(Context context) {

        final StackRequiresOnline stackRequiresOnline = pop();

        final StackMojo stackMojo = get();
        
        stackMojo.setRequiresOnline(stackRequiresOnline.getValue());
    }

    @Override
    public void onAggregatorStart(Context context) {

        push(new StackAggregator(context));
        
    }

    @Override
    public void onAggregatorEnd(Context context) {
    
        final StackAggregator stackAggregator = pop();
        
        final StackMojo stackMojo = get();
        
        stackMojo.setAggregator(stackAggregator.getValue());
    }

    @Override
    public void onThreadSafeStart(Context context) {

        push(new StackThreadSafe(context));
        
    }

    @Override
    public void onThreadSafeEnd(Context context) {

        final StackThreadSafe stackThreadSafe = pop();
        
        final StackMojo stackMojo = get();
        
        stackMojo.setThreadSafe(stackThreadSafe.getValue());
    }

    @Override
    public void onInstantiationStrategyStart(Context context) {

        push(new StackInstantiationStrategy(context));
    }

    @Override
    public void onInstantiationStrategyEnd(Context context) {

        final StackInstantiationStrategy stackInstantiationStrategy = pop();
        
        final StackMojo stackMojo = get();
    
        stackMojo.setInstantiationStrategy(stackInstantiationStrategy.getText());
    }

    @Override
    public void onExecutionStrategyStart(Context context) {

        push(new StackExecutionStrategy(context));

    }

    @Override
    public void onExecutionStrategyEnd(Context context) {

        final StackExecutionStrategy stackExecutionStrategy = pop();
        
        final StackMojo stackMojo = get();
        
        stackMojo.setExecutionStrategy(stackExecutionStrategy.getText());
    }

    @Override
    public void onSinceStart(Context context) {

        push(new StackSince(context));
        
    }

    @Override
    public void onSinceEnd(Context context) {

        final StackSince stackSince = pop();
        
        final SinceSetter sinceSetter = get();
        
        sinceSetter.setSince(stackSince.getText());
    }

    @Override
    public void onDeprecatedStart(Context context) {

        push(new StackDeprecated(context));

    }

    @Override
    public void onDeprecatedEnd(Context context) {

        final StackDeprecated stackDeprecated = pop();
        
        final DeprecatedSetter deprecatedSetter = get();
        
        deprecatedSetter.setDeprecated(stackDeprecated.getText());
    }

    @Override
    public void onConfiguratorStart(Context context) {

        push(new StackConfigurator(context));
        
    }

    @Override
    public void onConfiguratorEnd(Context context) {

        final StackConfigurator stackConfigurator = pop();
        
        final StackMojo stackMojo = get();
        
        stackMojo.setConfigurator(stackConfigurator.getText());
    }

    @Override
    public void onComposerStart(Context context) {

        push(new StackComposer(context));
        
    }

    @Override
    public void onComposerEnd(Context context) {

        final StackComposer stackComposer = pop();
        
        final StackMojo stackMojo = get();
        
        stackMojo.setComposer(stackComposer.getText());
    }

    @Override
    public void onParametersStart(Context context) {
        
    }

    @Override
    public void onParameterStart(Context context) {

        push(new StackParameter(context));
        
    }

    @Override
    public void onAliasStart(Context context) {

        push(new StackAlias(context));
        
    }

    @Override
    public void onAliasEnd(Context context) {

        final StackAlias stackAlias = pop();
        
        final StackParameter stackParameter = get();
        
        stackParameter.setAlias(stackAlias.getText());
    }

    @Override
    public void onTypeStart(Context context) {
        push(new StackType(context));
    }

    @Override
    public void onTypeEnd(Context context) {

        final StackType stackType = pop();
        
        final TypeSetter typeSetter = get();
        
        typeSetter.setType(stackType.getText());
    }

    @Override
    public void onRequiredStart(Context context) {

        push(new StackRequired(context));
        
    }

    @Override
    public void onRequiredEnd(Context context) {

        final StackRequired stackRequired = pop();
        
        final StackParameter stackParameter = get();
    
        stackParameter.setRequired(stackRequired.getValue());
    }

    @Override
    public void onEditableStart(Context context) {

        push(new StackEditable(context));
        
    }

    @Override
    public void onEditableEnd(Context context) {

        final StackEditable stackEditable = pop();
        
        final StackParameter stackParameter = get();
        
        stackParameter.setEditable(stackEditable.getValue());
    }

    @Override
    public void onParameterEnd(Context context) {

        final StackParameter stackParameter = pop();
        
        final StackMojo stackMojo = get();
        
        stackMojo.addParameter(stackParameter.build());
    }

    @Override
    public void onParametersEnd(Context context) {
        
    }

    @Override
    public void onConfigurationStart(Context context) {

        push(new StackConfiguration(context));
        
    }

    @Override
    public void onConfigurationEnd(Context context) {
        
        final StackConfiguration stackConfiguration = pop();
        
        final StackMojo stackMojo = get();
        
        stackMojo.setConfigurations(stackConfiguration.getParams());
    }

    @Override
    public void onRequirementsStart(Context context) {
        
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
        
        final StackRequirement stackRequirement = get();
        
        stackRequirement.setRole(stackRole.getText());
    }

    @Override
    public void onRoleHintStart(Context context) {

        push(new StackRoleHint(context));
    }

    @Override
    public void onRoleHintEnd(Context context) {

        final StackRoleHint stackRoleHint = pop();
        
        final StackRequirement stackRequirement = get();
        
        stackRequirement.setRoleHint(stackRoleHint.getText());
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
        
        final StackMojo stackMojo = get();
        
        final MojoRequirement requirement = new MojoRequirement(
                                                    stackRequirement.getRole(),
                                                    stackRequirement.getRoleHint(),
                                                    stackRequirement.getFieldName());

        stackMojo.addRequirement(requirement);
    }

    @Override
    public void onRequirementsEnd(Context context) {
        
    }

    @Override
    public void onMojoEnd(Context context) {

        final StackMojo stackMojo = pop();
        
        final StackPluginDescriptor stackPluginDescriptor = get();
        
        stackPluginDescriptor.addMojo(stackMojo.build());
    }

    @Override
    public void onMojosEnd(Context context) {
        
    }

    @Override
    public MavenPluginDescriptor onPluginEnd(Context context) {

        final StackPluginDescriptor stackPluginDescriptor = pop();
        
        this.pluginDescriptor = stackPluginDescriptor.build();

        return pluginDescriptor;
    }

    @Override
    public void onUnknownTagStart(Context context, String name, List<XMLAttribute> attributes) {
        
        final Object cur = get();
        
        if (cur instanceof StackConfiguration) {
            
            final StackParamName stackParamName = new StackParamName(context, name);
            
            final String implementation = findAttribute(attributes, "implementation");
            
            if (implementation != null) {
                stackParamName.setImplementation(implementation);
            }

            final String defaultValue = findAttribute(attributes, "default-value");
            
            if (defaultValue != null) {
                stackParamName.setDefaultValue(defaultValue);
            }
            
            push(stackParamName);
        }
    }

    private static String findAttribute(List<XMLAttribute> attributes, String name) {
        
        Objects.requireNonNull(name);
        
        return attributes.stream()
                .filter(attribute -> attribute.getName().equals(name))
                .findFirst()
                .map(XMLAttribute::getValue)
                .orElse(null);
    }
    
    @Override
    public void onUnknownTagEnd(Context context, String name) {
        
        final Object cur = get();
        
        if (cur instanceof StackParamName) {
            
            final StackParamName stackParamName = pop();

            final StackConfiguration stackConfiguration = get();
            
            stackConfiguration.addParam(stackParamName.build());
        }
    }

    MavenPluginDescriptor getPluginDescriptor() {
        return pluginDescriptor;
    }
}
