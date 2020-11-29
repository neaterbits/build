package com.neaterbits.build.buildsystem.maven.di.componentsource.mavenplugin;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoRequirement;
import com.neaterbits.util.di.Instantiation;
import com.neaterbits.util.di.componentsource.BaseComponentsSource;
import com.neaterbits.util.di.componentsource.ComponentsSourceException;

final class MavenPluginComponentsSource extends BaseComponentsSource<MojoDescriptor> {

    private final MavenPluginDescriptor pluginDescriptor;
    
    MavenPluginComponentsSource(MavenPluginDescriptor pluginDescriptor, URL source) {
        super(source);
        
        Objects.requireNonNull(pluginDescriptor);
        
        this.pluginDescriptor = pluginDescriptor;
    }

    @Override
    public void scanForComponentSpecs(ComponentSpecProcessor<MojoDescriptor> processor)
            throws IOException, ComponentsSourceException {

        if (pluginDescriptor.getMojos() != null) {

            for (MojoDescriptor mojo : pluginDescriptor.getMojos()) {

                processor.onComponentSpec(
                        mojo,
                        this,
                        mojo.getImplementation(),
                        mojo.getImplementation(),
                        null,
                        Instantiation.SINGLETON);
            }
        }
    }

    @Override
    public void scanForRequirements(MojoDescriptor componentSpec, RequirementProcessor processor) {

        if (componentSpec.getRequirements() != null) {

            for (MojoRequirement mojoRequirement : componentSpec.getRequirements()) {

                processor.onRequirement(
                                mojoRequirement.getRole(),
                                mojoRequirement.getRoleHint(),
                                mojoRequirement.getFieldName());
            }
        }
    }
}
