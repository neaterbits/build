package com.neaterbits.build.buildsystem.maven.plugins.initialize;

import org.apache.maven.plugin.Mojo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.common.model.configuration.MavenConfiguration;
import com.neaterbits.build.buildsystem.maven.common.model.configuration.PlexusConfigurationMap;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoParameter;

public class ConfigurerTest {

    @Test
    public void testStringScalar() throws MojoInitializeException, ClassNotFoundException {

        class TestMojo extends BaseTestMojo {
            
            private String stringParam;
        }

        final TestMojo mojo = new TestMojo();
        
        final boolean applied = apply(mojo, "stringParam", String.class.getName(), "StringValue");
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.stringParam).isEqualTo("StringValue");
    }

    @Test
    public void testStringRequired() throws MojoInitializeException {

        class TestMojo extends BaseTestMojo {
            
        }

        final TestMojo mojo = new TestMojo();
        
        try {
            apply(mojo, "stringParam", String.class.getName(), null, true, true);

            fail("Expected exception");
        }
        catch (MissingRequiredValueException ex) {
            
        }
    }

    @Test
    public void testIntScalar() throws MojoInitializeException {

        class TestMojo extends BaseTestMojo {
            
            private int intParam;
        }

        final TestMojo mojo = new TestMojo();
        
        final boolean applied = apply(mojo, "intParam", int.class.getName(), "123");
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.intParam).isEqualTo(123);
    }

    @Test
    public void testIntScalarNull() throws MojoInitializeException {

        class TestMojo extends BaseTestMojo {
            
        }

        final TestMojo mojo = new TestMojo();
        
        try {
            apply(mojo, "intParam", int.class.getName(), null);

            fail("Expected exception");
        }
        catch (MissingRequiredValueException ex) {
            
        }
    }

    @Test
    public void testInteger() throws MojoInitializeException {

        class TestMojo extends BaseTestMojo {
            
            private Integer integerParam;
        }

        final TestMojo mojo = new TestMojo();
        
        final boolean applied = apply(mojo, "integerParam", Integer.class.getName(), "123");
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.integerParam).isEqualTo(123);
    }

    @Test
    public void testIntegerNull() throws MojoInitializeException {

        class TestMojo extends BaseTestMojo {
            
            private Integer integerParam;
        }

        final TestMojo mojo = new TestMojo();
        
        final boolean applied = apply(mojo, "integerParam", Integer.class.getName(), null);
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.integerParam).isNull();
    }

    @Test
    public void testIntegerRequiredNull() throws MojoInitializeException {

        class TestMojo extends BaseTestMojo {
            
        }

        final TestMojo mojo = new TestMojo();
        
        try {
            apply(mojo, "integerParam", Integer.class.getName(), null, true, true);

            fail("Expected exception");
        }
        catch (MissingRequiredValueException ex) {
            
        }
    }

    @Test
    public void testIntegerTypeMismatch() throws MojoInitializeException {

        class TestMojo extends BaseTestMojo {
         
            @SuppressWarnings("unused")
            private Integer integerParam;
        }

        final TestMojo mojo = new TestMojo();
        
        try {
            apply(mojo, "integerParam", int.class.getName(), "123");

            fail("Expected exception");
        }
        catch (TypeMismatchException ex) {
            
        }
    }

    @Test
    public void testFloatScalar() throws MojoInitializeException {

        class TestMojo extends BaseTestMojo {
            
            private float floatParam;
        }

        final TestMojo mojo = new TestMojo();
        
        final boolean applied = apply(mojo, "floatParam", float.class.getName(), "123.4");
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.floatParam).isEqualTo(123.4f);
    }

    @Test
    public void testFloatScalarNull() throws MojoInitializeException {

        class TestMojo extends BaseTestMojo {
            
        }

        final TestMojo mojo = new TestMojo();
        
        try {
            apply(mojo, "floatParam", float.class.getName(), null);

            fail("Expected exception");
        }
        catch (MissingRequiredValueException ex) {
            
        }
    }
    
    @Test
    public void testBooleanScalar() throws MojoInitializeException {

        class TestMojo extends BaseTestMojo {
            
            private boolean booleanParam;
        }

        final TestMojo mojo = new TestMojo();
        
        final boolean applied = apply(mojo, "booleanParam", boolean.class.getName(), "true");
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.booleanParam).isTrue();
    }

    @Test
    public void testBooleanObject() throws MojoInitializeException {

        class TestMojo extends BaseTestMojo {
            
            private Boolean booleanParam;
        }

        final TestMojo mojo = new TestMojo();
        
        final boolean applied = apply(mojo, "booleanParam", Boolean.class.getName(), "true");
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.booleanParam).isTrue();
    }

    @Test
    public void testCharScalar() throws MojoInitializeException {

        class TestMojo extends BaseTestMojo {
            
            private char charParam;
        }

        final TestMojo mojo = new TestMojo();
        
        final boolean applied = apply(mojo, "charParam", char.class.getName(), "c");
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.charParam).isEqualTo('c');
    }

    @Test
    public void testCharacterObject() throws MojoInitializeException {

        class TestMojo extends BaseTestMojo {
            
            private Character characterParam;
        }

        final TestMojo mojo = new TestMojo();
        
        final boolean applied = apply(mojo, "characterParam", Character.class.getName(), "c");
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.characterParam).isEqualTo('c');
    }

    @Test
    public void testCharacterFormat() throws MojoInitializeException {

        class TestMojo extends BaseTestMojo {
            
            @SuppressWarnings("unused")
            private Character characterParam;
        }

        final TestMojo mojo = new TestMojo();
        
        try {
            apply(mojo, "characterParam", Character.class.getName(), "abc");

            fail("Expected exception");
        }
        catch (ValueFormatException ex) {
            
        }
    }

    @Test
    public void testStringArray() throws MojoInitializeException {

        class TestMojo extends BaseTestMojo {
            
            private String [] stringArrayParam;
        }

        final TestMojo mojo = new TestMojo();
        
        final boolean applied = apply(
                mojo,
                "stringArrayParam",
                String[].class.getName(),
                new String [] {
                        "StringValue1",
                        "StringValue2",
                        "StringValue3"
                });
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.stringArrayParam).containsExactly(
                                        "StringValue1",
                                        "StringValue2",
                                        "StringValue3");
    }

    @Test
    public void testStringArrayFromEmptyList() throws MojoInitializeException {

        class TestMojo extends BaseTestMojo {
            
            private String [] stringArrayParam;
        }

        final TestMojo mojo = new TestMojo();
        
        final boolean applied = apply(
                mojo,
                "stringArrayParam",
                String[].class.getName(),
                Collections.emptyList());
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.stringArrayParam).isEmpty();
    }

    @Test
    public void testStringArrayFromStringList() throws MojoInitializeException {

        class TestMojo extends BaseTestMojo {
            
            private String [] stringArrayParam;
        }

        final TestMojo mojo = new TestMojo();
        
        
        final boolean applied = apply(
                mojo,
                "stringArrayParam",
                String[].class.getName(),
                Arrays.asList("StringValue1", "StringValue2", "StringValue3"));
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.stringArrayParam).containsExactly(
                                        "StringValue1",
                                        "StringValue2",
                                        "StringValue3");
    }
    @Test
    public void testIntArray() throws MojoInitializeException {

        class TestMojo extends BaseTestMojo {
            
            private int [] intArrayParam;
        }

        final TestMojo mojo = new TestMojo();
        
        final boolean applied = apply(
                mojo,
                "intArrayParam",
                int[].class.getName(),
                new String [] {
                        "123",
                        "234",
                        "345"
                });
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.intArrayParam).containsExactly(123, 234, 345);
    }

    @Test
    public void testIntArrayFromEmptyArray() throws MojoInitializeException {

        class TestMojo extends BaseTestMojo {
            
            private int [] intArrayParam;
        }

        final TestMojo mojo = new TestMojo();
        
        final boolean applied = apply(
                mojo,
                "intArrayParam",
                int[].class.getName(),
                new String [] { });
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.intArrayParam).isEmpty();
    }

    @Test
    public void testIntArrayFromStringList() throws MojoInitializeException {

        class TestMojo extends BaseTestMojo {
            
            private int [] intArrayParam;
        }

        final TestMojo mojo = new TestMojo();
        
        final boolean applied = apply(
                mojo,
                "intArrayParam",
                int[].class.getName(),
                Arrays.asList("123", "234", "345"));
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.intArrayParam).containsExactly(123, 234, 345);
    }

    @Test
    public void testIntArrayFromEmptyList() throws MojoInitializeException {

        class TestMojo extends BaseTestMojo {
            
            private int [] intArrayParam;
        }

        final TestMojo mojo = new TestMojo();
        
        final boolean applied = apply(
                mojo,
                "intArrayParam",
                int[].class.getName(),
                Collections.emptyList());
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.intArrayParam).isEmpty();
    }

    @Test
    public void testStringListFromStringArray() throws MojoInitializeException {

        class TestMojo extends BaseTestMojo {
            
            private List<String> stringList;
        }

        final TestMojo mojo = new TestMojo();
        
        final boolean applied = apply(
                mojo,
                "stringList",
                List.class.getName(),
                new String [] {
                        "StringValue1",
                        "StringValue2",
                        "StringValue3"
                });
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.stringList).containsExactly("StringValue1", "StringValue2", "StringValue3");    
    }

    @Test
    public void testStringListFromStringList() throws MojoInitializeException {

        class TestMojo extends BaseTestMojo {
            
            private List<String> stringList;
        }

        final TestMojo mojo = new TestMojo();
        
        final boolean applied = apply(
                mojo,
                "stringList",
                List.class.getName(),
                Arrays.asList(
                        "StringValue1",
                        "StringValue2",
                        "StringValue3"
                ));
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.stringList).containsExactly("StringValue1", "StringValue2", "StringValue3");    
    }

    @Test
    public void testObjectFromMap() throws MojoInitializeException {

        final ObjectTestMojo mojo = new ObjectTestMojo();

        final Map<String, Object> values = new HashMap<>();
        
        values.put("field1", "Field1Value");
        values.put("field2", "true");
        values.put("field3", Arrays.asList("123", "234", "345"));
        
        final MojoParameter stringParam
            = new MojoParameter(
                    "complexTestObject",
                    null,
                    ComplexTestObject.class.getName(),
                    false,
                    true,
                    null,
                    null,
                    null,
                    null);

        final boolean applied = apply(mojo, stringParam, new PlexusConfigurationMap(values));
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.complexTestObject.field1).isEqualTo("Field1Value");    
        assertThat(mojo.complexTestObject.field2).isTrue();       
        assertThat(mojo.complexTestObject.field3).containsExactly(123, 234, 345);    
    }

    @Test
    public void testObjectFromMapWithParameterImplementation() throws MojoInitializeException {

        final ImplementationNameTestMojo mojo = new ImplementationNameTestMojo();

        final Map<String, Object> values = new HashMap<>();
        
        values.put("field1", "Field1Value");
        values.put("field2", "true");
        values.put("field3", Arrays.asList("123", "234", "345"));
        
        final MojoParameter stringParam
            = new MojoParameter(
                    "complexField",
                    null,
                    ComplexTestObject.class.getName(),
                    false,
                    true,
                    ComplexTestObject.class.getName(),
                    null,
                    null,
                    null);

        final boolean applied = apply(mojo, stringParam, new PlexusConfigurationMap(values));
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.complexField.field1).isEqualTo("Field1Value");    
        assertThat(mojo.complexField.field2).isTrue();       
        assertThat(mojo.complexField.field3).containsExactly(123, 234, 345);    
    }

    @Test
    public void testObjectFromMapWithSubObject() throws MojoInitializeException {

        final SubObjectTestMojo mojo = new SubObjectTestMojo();

        final Map<String, Object> subValues = new HashMap<>();
        
        subValues.put("intField", 321);
        subValues.put("intArrayField", Arrays.asList("123", "234", "345"));
        
        final Map<String, Object> values = new HashMap<>();
        
        values.put("stringField", "StringValue");
        
        values.put("subTestObject", new PlexusConfigurationMap(subValues));
        
        final MojoParameter stringParam
            = new MojoParameter(
                    "subComplexTestObject",
                    null,
                    SubComplexTestObject.class.getName(),
                    false,
                    true,
                    null,
                    null,
                    null,
                    null);

        final boolean applied = apply(mojo, stringParam, new PlexusConfigurationMap(values));
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.subComplexTestObject.stringField).isEqualTo("StringValue");    
        assertThat(mojo.subComplexTestObject.subTestObject.intField).isEqualTo(321);       
        assertThat(mojo.subComplexTestObject.subTestObject.intArrayField).containsExactly(123, 234, 345);    
    }

    @Test
    public void testMapFromMap() throws MojoInitializeException {
        
        class TestMojo extends BaseTestMojo {
            
            private Map<String, Object> map;
        }

        final Map<String, Object> values = new HashMap<>();
        
        values.put("field1", "Field1Value");
        values.put("field2", "true");
        values.put("field3", Arrays.asList("123", "234", "345"));
        
        final MojoParameter mapParam
            = new MojoParameter(
                    "map",
                    null,
                    Map.class.getName(),
                    false,
                    true,
                    null,
                    null,
                    null,
                    null);

        final TestMojo mojo = new TestMojo();
        
        final boolean applied = apply(mojo, mapParam, new PlexusConfigurationMap(values));
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.map.get("field1")).isEqualTo("Field1Value");    
        assertThat(mojo.map.get("field2")).isEqualTo("true");       
        assertThat(mojo.map.get("field3")).isEqualTo(Arrays.asList("123", "234", "345"));    
    }

    @Test
    public void testMapsFromMap() throws MojoInitializeException {
        
        class TestMojo extends BaseTestMojo {
            
            private Map<String, Object> map;
        }

        final Map<String, Object> subValues = new HashMap<>();
        
        subValues.put("subField1", "SubFieldValue");
        
        final Map<String, Object> values = new HashMap<>();
        
        values.put("field1", "Field1Value");
        values.put("field2", "true");
        values.put("field3", new PlexusConfigurationMap(subValues));
        
        final MojoParameter mapParam
            = new MojoParameter(
                    "map",
                    null,
                    Map.class.getName(),
                    false,
                    true,
                    null,
                    null,
                    null,
                    null);

        final TestMojo mojo = new TestMojo();
        
        final boolean applied = apply(mojo, mapParam, new PlexusConfigurationMap(values));
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.map.get("field1")).isEqualTo("Field1Value");    
        assertThat(mojo.map.get("field2")).isEqualTo("true");
        
        @SuppressWarnings("unchecked")
        final Map<String, Object> sub = (Map<String, Object>) mojo.map.get("field3");
        
        assertThat(sub).isNotNull();
        
        assertThat(sub.get("subField1")).isEqualTo("SubFieldValue");    
    }

    @Test
    public void testMapWithComplexFromMap() throws MojoInitializeException {
        
        class TestMojo extends BaseTestMojo {
            
            private Map<String, Object> map;
        }

        final Map<String, Object> subValues = new HashMap<>();
        
        subValues.put("field1", "Field1Value");
        subValues.put("field2", "true");
        subValues.put("field3", Arrays.asList("123", "234", "345"));
        
        final PlexusConfigurationMap subMap = new PlexusConfigurationMap(
                                                        subValues,
                                                        ComplexTestObject.class.getName());
        
        final Map<String, Object> values = new HashMap<>();
        
        values.put("field1", "Field1Value");
        values.put("field2", "true");
        values.put("field3", subMap);
        
        final MojoParameter mapParam
            = new MojoParameter(
                    "map",
                    null,
                    Map.class.getName(),
                    false,
                    true,
                    null,
                    null,
                    null,
                    null);

        final TestMojo mojo = new TestMojo();
        
        final boolean applied = apply(mojo, mapParam, new PlexusConfigurationMap(values));
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.map.get("field1")).isEqualTo("Field1Value");    
        assertThat(mojo.map.get("field2")).isEqualTo("true");
        
        final ComplexTestObject sub = (ComplexTestObject) mojo.map.get("field3");
        
        assertThat(sub).isNotNull();
        
        assertThat(sub.field1).isEqualTo("Field1Value");    
        assertThat(sub.field2).isTrue();
        assertThat(sub.field3).containsExactly(123, 234, 345);    
    }

    @Test
    public void testPropertiesFromMap() throws MojoInitializeException {
        
        class TestMojo extends BaseTestMojo {
            
            private Properties testProperties;
        }

        final Map<String, Object> values1 = new HashMap<>();
        values1.put("name", "Name1");
        values1.put("value", "Value1");

        final Map<String, Object> values2 = new HashMap<>();
        values2.put("name", "Name2");
        values2.put("value", "Value2");
        
        final Map<String, Object> values3 = new HashMap<>();
        values3.put("name", "Name3");
        values3.put("value", "Value3");
        
        final List<PlexusConfigurationMap> list = Arrays.asList(
                new PlexusConfigurationMap(values1),
                new PlexusConfigurationMap(values2),
                new PlexusConfigurationMap(values3));
        
        final Map<String, Object> map = new HashMap<>();
        
        map.put("property", list);
        
        final MojoParameter mapParam
            = new MojoParameter(
                    "testProperties",
                    null,
                    Properties.class.getName(),
                    false,
                    true,
                    null,
                    null,
                    null,
                    null);

        final TestMojo mojo = new TestMojo();
        
        final boolean applied = apply(mojo, mapParam, new PlexusConfigurationMap(map));
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.testProperties.get("Name1")).isEqualTo("Value1");    
        assertThat(mojo.testProperties.get("Name2")).isEqualTo("Value2");    
        assertThat(mojo.testProperties.get("Name3")).isEqualTo("Value3");    
    }

    @Test
    public void testObjectFromMapWithImplementation() throws MojoInitializeException {

        final ImplementationNameTestMojo mojo = new ImplementationNameTestMojo();

        final Map<String, Object> values = new HashMap<>();
        
        values.put("field1", "Field1Value");
        values.put("field2", "true");
        values.put("field3", Arrays.asList("123", "234", "345"));
        
        final PlexusConfigurationMap map = new PlexusConfigurationMap(
                                                values,
                                                ComplexTestObject.class.getName());
        
        final MojoParameter stringParam
            = new MojoParameter(
                    "complexField",
                    null,
                    ComplexTestObject.class.getName(),
                    false,
                    true,
                    null,
                    null,
                    null,
                    null);

        final boolean applied = apply(mojo, stringParam, map);
        
        assertThat(applied).isTrue();
        
        assertThat(mojo.complexField.field1).isEqualTo("Field1Value");    
        assertThat(mojo.complexField.field2).isTrue();       
        assertThat(mojo.complexField.field3).containsExactly(123, 234, 345);    
    }

    private boolean apply(Mojo mojo, String fieldName, String fieldType, Object value) throws MojoInitializeException {
        
        return apply(mojo, fieldName, fieldType, value, false, true);
    }

    private boolean apply(Mojo mojo, String fieldName, String fieldType, Object value, boolean required, boolean editable) throws MojoInitializeException {

        final MojoParameter stringParam
            = new MojoParameter(
                    fieldName,
                    null,
                    fieldType,
                    required,
                    editable,
                    fieldType,
                    null,
                    null,
                    null);
    
        return apply(mojo, stringParam, value);
    }

    private boolean apply(Mojo mojo, MojoParameter parameter, Object value) throws MojoInitializeException {

        final MojoExecutionContext context = new MojoExecutionContext(mojo);

        final MavenConfiguration configuration = makeConfiguration(parameter.getName(), value);
        
        final boolean applied = Configurer.applyConfiguration(context, parameter, configuration);
        
        return applied;
    }
    
    private static MavenConfiguration makeConfiguration(String key, Object value) {

        Objects.requireNonNull(key);
        
        final Map<String, Object> map = new HashMap<>();
        
        map.put(key, value);
        
        final PlexusConfigurationMap configurationMap = new PlexusConfigurationMap(map);
        final MavenConfiguration configuration = new MavenConfiguration(false, configurationMap);
        
        return configuration;
    }
}
