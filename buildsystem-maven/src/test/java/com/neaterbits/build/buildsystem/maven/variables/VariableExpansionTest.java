package com.neaterbits.build.buildsystem.maven.variables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

public class VariableExpansionTest {

    @Test
    public void testOneVariable() {

        final String expanded = VariableExpansion.expandVariables(
                "${var}",
                name -> {
                    assertThat(name).isEqualTo("var");
                    
                    return "value";
                });
                
        assertThat(expanded).isEqualTo("value");
        
    }

    @Test
    public void testOneVariableWithoutValue() {

        final String expanded = VariableExpansion.expandVariables(
                "${var}",
                name -> {
                    assertThat(name).isEqualTo("var");
                    
                    return null;
                });
                
        assertThat(expanded).isEqualTo("${var}");
        
    }

    @Test
    public void testOneVariableWithoutName() {

        final String expanded = VariableExpansion.expandVariables(
                "${}",
                name -> {
                    fail("Should not be called");
                    
                    return null;
                });
                
        assertThat(expanded).isEqualTo("${}");
    }

    @Test
    public void testVariableOfNameLength1() {

        final String expanded = VariableExpansion.expandVariables(
                "${x}",
                name -> {
                    assertThat(name).isEqualTo("x");
                    
                    return "value";
                });
                
        assertThat(expanded).isEqualTo("value");
        
    }

    @Test
    public void testOneVariableWithPrefix() {

        final String expanded = VariableExpansion.expandVariables(
                "x${var}",
                name -> {
                    assertThat(name).isEqualTo("var");
                    
                    return "value";
                });
                
        assertThat(expanded).isEqualTo("xvalue");
        
    }

    @Test
    public void testOneVariableWithPrefixWithoutValue() {

        final String expanded = VariableExpansion.expandVariables(
                "x${var}",
                name -> {
                    assertThat(name).isEqualTo("var");
                    
                    return null;
                });
                
        assertThat(expanded).isEqualTo("x${var}");
        
    }

    @Test
    public void testMultipleVariablesWithSuffix() {

        final String expanded = VariableExpansion.expandVariables(
                "${var1}x${var2}y",
                name -> {

                    final String value;
                    
                    switch (name) {
                    case "var1":
                        value = "value1";
                        break;
                        
                    case "var2":
                        value = "value2";
                        break;
                        
                    default:
                        throw new IllegalArgumentException();
                    }
                    
                    return value;
                });
                
        assertThat(expanded).isEqualTo("value1xvalue2y");
        
    }

    @Test
    public void testMultipleVariablesWithSuffixWithoutValue() {

        final String expanded = VariableExpansion.expandVariables(
                "${var1}x${var2}y",
                name -> {
                    return null;
                });
                
        assertThat(expanded).isEqualTo("${var1}x${var2}y");
    }
}
