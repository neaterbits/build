package com.neaterbits.build.buildsystem.maven.plugins.initialize;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class MojoFieldUtilTest {

    @Test
    public void testIsPrimitive() {

        assertThat(MojoFieldUtil.isPrimitiveType(byte.class)).isTrue();
        assertThat(MojoFieldUtil.isPrimitiveType(short.class)).isTrue();
        assertThat(MojoFieldUtil.isPrimitiveType(int.class)).isTrue();
        assertThat(MojoFieldUtil.isPrimitiveType(long.class)).isTrue();
        assertThat(MojoFieldUtil.isPrimitiveType(float.class)).isTrue();
        assertThat(MojoFieldUtil.isPrimitiveType(double.class)).isTrue();
        assertThat(MojoFieldUtil.isPrimitiveType(boolean.class)).isTrue();
        assertThat(MojoFieldUtil.isPrimitiveType(char.class)).isTrue();
        
        assertThat(MojoFieldUtil.isPrimitiveType(Byte.class)).isFalse();
        assertThat(MojoFieldUtil.isPrimitiveType(Short.class)).isFalse();
        assertThat(MojoFieldUtil.isPrimitiveType(Integer.class)).isFalse();
        assertThat(MojoFieldUtil.isPrimitiveType(Long.class)).isFalse();
        assertThat(MojoFieldUtil.isPrimitiveType(Float.class)).isFalse();
        assertThat(MojoFieldUtil.isPrimitiveType(Double.class)).isFalse();
        assertThat(MojoFieldUtil.isPrimitiveType(Boolean.class)).isFalse();
        assertThat(MojoFieldUtil.isPrimitiveType(Character.class)).isFalse();
    }
}
