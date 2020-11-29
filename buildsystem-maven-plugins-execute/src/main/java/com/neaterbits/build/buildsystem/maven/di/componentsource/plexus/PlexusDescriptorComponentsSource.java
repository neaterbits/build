package com.neaterbits.build.buildsystem.maven.di.componentsource.plexus;

import java.net.URL;
import java.util.Objects;

import org.eclipse.sisu.plexus.Strategies;

import com.neaterbits.build.buildsystem.maven.plexus.components.model.PlexusComponent;
import com.neaterbits.build.buildsystem.maven.plexus.components.model.PlexusComponentSet;
import com.neaterbits.build.buildsystem.maven.plexus.components.model.PlexusRequirement;
import com.neaterbits.util.di.Instantiation;
import com.neaterbits.util.di.componentsource.BaseComponentsSource;

final class PlexusDescriptorComponentsSource extends BaseComponentsSource<PlexusComponent> {
    
    private final PlexusComponentSet descriptor;

    public PlexusDescriptorComponentsSource(PlexusComponentSet descriptor, URL source) {
        super(source);

        Objects.requireNonNull(descriptor);
        
        this.descriptor = descriptor;
    }

    @Override
    public void scanForComponentSpecs(ComponentSpecProcessor<PlexusComponent> processor) {

        if (descriptor.getComponents() != null) {
            descriptor.getComponents().forEach(c -> processor.onComponentSpec(
                                                                        c,
                                                                        this,
                                                                        c.getImplementation(),
                                                                        c.getRole(),
                                                                        c.getRoleHint(),
                                                                        getInstantiation(c.getInstantiationStrategy())));
        }
    }

    private static Instantiation getInstantiation(String instantiationStrategy) {
        
        final Instantiation defaultInstantiation = Instantiation.SINGLETON;
        
        final Instantiation instantiation;
        
        if (instantiationStrategy == null) {
            instantiation = defaultInstantiation;
        }
        else {
            switch (instantiationStrategy) {
            case Strategies.LOAD_ON_START:
                instantiation = Instantiation.LOAD_ON_START;
                break;
                
            case Strategies.SINGLETON:
                instantiation = Instantiation.SINGLETON;
                break;
                
            case Strategies.PER_LOOKUP:
                instantiation = Instantiation.PROTOTYPE;
                break;

            default:
                instantiation = defaultInstantiation;
                break;
            }
        }
        
        return instantiation;
    }
    
    @Override
    public void scanForRequirements(PlexusComponent componentSpec, RequirementProcessor processor) {

        if (componentSpec.getRequirements() != null) {

            for (PlexusRequirement requirement : componentSpec.getRequirements()) {

                processor.onRequirement(
                        requirement.getRole(),
                        requirement.getRoleHint(),
                        requirement.getFieldName());
            }
        }
    }
}
