package com.neaterbits.build.buildsystem.maven.plugins.descriptor.parse;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoParameter;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoRequirement;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.stream.JavaxXMLStreamReaderFactory;

public class MavenPluginDescriptorParserTest {

    @Test
    public void testParser() throws XMLReaderException, IOException {

        final JavaxXMLStreamReaderFactory xmlReaderFactory = new JavaxXMLStreamReaderFactory();

        try (InputStream inputStream = getClass().getResourceAsStream("/plugin-test.xml")) {

            final MavenPluginDescriptor descriptor
                = MavenPluginDescriptorParser.read(inputStream, xmlReaderFactory, "plugin.xml");

            assertThat(descriptor).isNotNull();

            assertThat(descriptor.getDescription()).isEqualTo("Plugin description");
            assertThat(descriptor.getModuleId().getGroupId()).isEqualTo("com.test.plugin");
            assertThat(descriptor.getModuleId().getArtifactId()).isEqualTo("test-plugin");
            assertThat(descriptor.getModuleId().getVersion()).isEqualTo("1.0");
            
            assertThat(descriptor.getIsolatedRealm()).isFalse();
            assertThat(descriptor.getInheritedByDefault()).isFalse();

            assertThat(descriptor.getMojos().size()).isEqualTo(1);
            
            final MojoDescriptor mojo = descriptor.getMojos().get(0); 
            
            assertThat(mojo.getGoal()).isEqualTo("the-mojo-goal");
            assertThat(mojo.getDescription()).isEqualTo("Plugin Mojo description");
            assertThat(mojo.getImplementation()).isEqualTo("com.test.plugin.TestPlugin");
            assertThat(mojo.getLanguage()).isEqualTo("Java");
            assertThat(mojo.getPhase()).isEqualTo("compile");
            assertThat(mojo.getExecutePhase()).isEqualTo("install");
            assertThat(mojo.getExecuteGoal()).isEqualTo("the-execute-goal");
            assertThat(mojo.getExecuteLifecycle()).isEqualTo("the-execute-lifecycle");
            assertThat(mojo.getRequiresDependencyResolution()).isEqualTo("requires-dependency-resolution");
            assertThat(mojo.getRequiresDependencyCollection()).isEqualTo("requires-dependency-collection");

            assertThat(mojo.getRequiresDirectInvocation()).isTrue();
            assertThat(mojo.getRequiresProject()).isFalse();
            assertThat(mojo.getRequiresReports()).isFalse();
            assertThat(mojo.getRequiresOnline()).isFalse();
            
            assertThat(mojo.getAggregator()).isFalse();
            
            assertThat(mojo.getInheritedByDefault()).isFalse();
            
            assertThat(mojo.getThreadSafe()).isTrue();
            
            assertThat(mojo.getInstantiationStrategy()).isEqualTo("per-lookup");
            assertThat(mojo.getExecutionStrategy()).isEqualTo("once-per-session");

            assertThat(mojo.getSince()).isEqualTo("0.9");
            assertThat(mojo.getDeprecated()).isEqualTo("0.9.1");
            
            assertThat(mojo.getConfigurator()).isEqualTo("the-mojo-configurator");
         
            assertThat(mojo.getComposer()).isEqualTo("the-mojo-composer");

            assertThat(mojo.getParameters().size()).isEqualTo(1);
            
            final MojoParameter param = mojo.getParameters().get(0);
            
            assertThat(param.getName()).isEqualTo("The parameter");
            assertThat(param.getAlias()).isEqualTo("theParameter");
            assertThat(param.getType()).isEqualTo("java.lang.String");
            assertThat(param.getRequired()).isTrue();
            assertThat(param.getEditable()).isFalse();
            assertThat(param.getImplementation()).isEqualTo("com.test.plugin.TestParam");
            assertThat(param.getDescription()).isEqualTo("The parameter description");
            assertThat(param.getSince()).isEqualTo("0.8");
            assertThat(param.getDeprecated()).isEqualTo("0.8.1");
            
            assertThat(mojo.getConfigurations().size()).isEqualTo(2);
            
            assertThat(mojo.getConfigurations().get(0).getParamName()).isEqualTo("testString");
            assertThat(mojo.getConfigurations().get(0).getImplementation()).isEqualTo("java.lang.String");
            assertThat(mojo.getConfigurations().get(0).getDefaultValue()).isEqualTo("abc");
            
            assertThat(mojo.getConfigurations().get(1).getParamName()).isEqualTo("testInteger");
            assertThat(mojo.getConfigurations().get(1).getImplementation()).isEqualTo("java.lang.Integer");
            assertThat(mojo.getConfigurations().get(1).getDefaultValue()).isEqualTo("123");

            assertThat(mojo.getRequirements().size()).isEqualTo(1);
            
            final MojoRequirement requirement = mojo.getRequirements().get(0);
            
            assertThat(requirement.getRole()).isEqualTo("the-mojo-role");
            assertThat(requirement.getRoleHint()).isEqualTo("the-mojo-role-hint");
            assertThat(requirement.getFieldName()).isEqualTo("the-mojo-field-name");
        
            assertThat(descriptor.getDependencies().size()).isEqualTo(1);
            
            final MavenDependency dependency = descriptor.getDependencies().get(0);
            
            assertThat(dependency.getModuleId().getGroupId()).isEqualTo("com.test.plugin-dependencies");
            assertThat(dependency.getModuleId().getArtifactId()).isEqualTo("plugin-dependency");
            assertThat(dependency.getModuleId().getVersion()).isEqualTo("0.1");
        }
    }
}
