package com.neaterbits.build.buildsystem.maven.plugins.initialize;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class FieldUtilTest {

    @Test
    public void testIsPrimitive() {

        assertThat(FieldUtil.isPrimitiveType(byte.class)).isTrue();
        assertThat(FieldUtil.isPrimitiveType(short.class)).isTrue();
        assertThat(FieldUtil.isPrimitiveType(int.class)).isTrue();
        assertThat(FieldUtil.isPrimitiveType(long.class)).isTrue();
        assertThat(FieldUtil.isPrimitiveType(float.class)).isTrue();
        assertThat(FieldUtil.isPrimitiveType(double.class)).isTrue();
        assertThat(FieldUtil.isPrimitiveType(boolean.class)).isTrue();
        assertThat(FieldUtil.isPrimitiveType(char.class)).isTrue();
        
        assertThat(FieldUtil.isPrimitiveType(Byte.class)).isFalse();
        assertThat(FieldUtil.isPrimitiveType(Short.class)).isFalse();
        assertThat(FieldUtil.isPrimitiveType(Integer.class)).isFalse();
        assertThat(FieldUtil.isPrimitiveType(Long.class)).isFalse();
        assertThat(FieldUtil.isPrimitiveType(Float.class)).isFalse();
        assertThat(FieldUtil.isPrimitiveType(Double.class)).isFalse();
        assertThat(FieldUtil.isPrimitiveType(Boolean.class)).isFalse();
        assertThat(FieldUtil.isPrimitiveType(Character.class)).isFalse();
    }
}
