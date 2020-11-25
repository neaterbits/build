package com.neaterbits.build.buildsystem.maven.plexus.components.parse;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.plexus.components.model.PlexusComponent;
import com.neaterbits.build.buildsystem.maven.plexus.components.model.PlexusComponentSet;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.stream.JavaxXMLStreamReaderFactory;

public class PlexusComponentDescriptorParserTest {

    @Test
    public void testParseDescriptor() throws XMLReaderException, IOException {

        final String xml =
                  "<component-set>"
                + "  <components>"
                + "    <component>"
                + "      <role>theComponentRole</role>"
                + "      <role-hint>theComponentRoleHint</role-hint>"
                + "      <implementation>theComponentImplementation</implementation>"

                + "      <requirements>"
                + "        <requirement>"
                + "          <role>theRequirementRole</role>"
                + "          <role-hint>theRequirementRoleHint</role-hint>"
                + "          <field-name>theRequirementFieldName</field-name>"
                + "          <field>theRequirementField</field>"
                + "        </requirement>"
                + "      </requirements>"

                + "    </component>"
                + "  </components>"
                + "</component-set>";
        
        final PlexusComponentSet componentSet
                    = PlexusComponentDescriptorParser.readDescriptor(
                            new ByteArrayInputStream(xml.getBytes()),
                            new JavaxXMLStreamReaderFactory(),
                            "components.xml");
        
        assertThat(componentSet).isNotNull();
        assertThat(componentSet.getComponents().size()).isEqualTo(1);
        
        final PlexusComponent component = componentSet.getComponents().get(0);
        
        assertThat(component.getRole()).isEqualTo("theComponentRole");
        assertThat(component.getRoleHint()).isEqualTo("theComponentRoleHint");
        assertThat(component.getImplementation()).isEqualTo("theComponentImplementation");

        assertThat(component.getRequirements().size()).isEqualTo(1);
        assertThat(component.getRequirements().get(0).getRole()).isEqualTo("theRequirementRole");
        assertThat(component.getRequirements().get(0).getRoleHint()).isEqualTo("theRequirementRoleHint");
        assertThat(component.getRequirements().get(0).getFieldName()).isEqualTo("theRequirementFieldName");
        assertThat(component.getRequirements().get(0).getField()).isEqualTo("theRequirementField");
    }
}
