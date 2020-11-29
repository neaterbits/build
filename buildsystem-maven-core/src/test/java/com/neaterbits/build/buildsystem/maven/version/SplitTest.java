package com.neaterbits.build.buildsystem.maven.version;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class SplitTest {

    @Test
    public void testTrim_1_0_0() {
        
        final Split split = new Split(
                new String [] { "1", "0", "0" },
                new char [] { '.', '.' });
        
        final Split trimmed = split.trim();
        
        assertThat(trimmed.getStrings()).isEqualTo(new String [] { "1" });
        assertThat(trimmed.getSeparators()).isEqualTo(new char[] { });
    }

    @Test
    public void testTrim_1_foo10() {
        
        final Split split = new Split(
                new String [] { "1", "foo", "10" },
                new char [] { '-', '-' });
        
        final Split trimmed = split.trim();
        
        assertThat(trimmed.getStrings()).isEqualTo(new String [] { "1", "foo", "10" });
        assertThat(trimmed.getSeparators()).isEqualTo(new char[] { '-', '-' });
    }

    @Test
    public void testTrim_1_ga() {
        
        final Split split = new Split(
                new String [] { "1", "ga" },
                new char [] { '.' });
        
        final Split trimmed = split.trim();
        
        assertThat(trimmed.getStrings()).isEqualTo(new String [] { "1" });
        assertThat(trimmed.getSeparators()).isEqualTo(new char[] { });
    }

    @Test
    public void testTrim_1_final() {
        
        final Split split = new Split(
                new String [] { "1", "final" },
                new char [] { '.' });
        
        final Split trimmed = split.trim();
        
        assertThat(trimmed.getStrings()).isEqualTo(new String [] { "1" });
        assertThat(trimmed.getSeparators()).isEqualTo(new char[] { });
    }

    @Test
    public void testTrim_1_0() {
        
        final Split split = new Split(
                new String [] { "1", "0" },
                new char [] { '.' });
        
        final Split trimmed = split.trim();
        
        assertThat(trimmed.getStrings()).isEqualTo(new String [] { "1" });
        assertThat(trimmed.getSeparators()).isEqualTo(new char[] { });
    }

    @Test
    public void testTrim_1_() {
        
        final Split split = MavenVersion.parseToSplit("1.");
        
        final Split trimmed = split.trim();
        
        assertThat(trimmed.getStrings()).isEqualTo(new String [] { "1" });
        assertThat(trimmed.getSeparators()).isEqualTo(new char[] { });
    }

    @Test
    public void testTrim_1dash() {
        
        final Split split = MavenVersion.parseToSplit("1-");
        
        final Split trimmed = split.trim();
        
        assertThat(trimmed.getStrings()).isEqualTo(new String [] { "1" });
        assertThat(trimmed.getSeparators()).isEqualTo(new char[] { });
    }

    @Test
    public void testTrim_1_dash_sp_1() {
        
        final Split split = MavenVersion.parseToSplit("1-sp-1");
        
        final Split trimmed = split.trim();
        
        assertThat(trimmed.getStrings()).isEqualTo(new String [] { "1", "sp", "1" });
        assertThat(trimmed.getSeparators()).isEqualTo(new char[] { '-', '-' });
    }

    @Test
    public void testTrim_1_0_0_dash_foo_0_0() {
        
        final Split split = new Split(
                new String [] { "1", "0", "0", "foo", "0", "0" },
                new char [] { '.', '.', '-', '.', '.' });
        
        final Split trimmed = split.trim();
        
        assertThat(trimmed.getStrings()).isEqualTo(new String [] { "1", "foo" });
        assertThat(trimmed.getSeparators()).isEqualTo(new char[] { '-' });
    }

    @Test
    public void testTrim_1_0_0_dash_foo_0_0_string() {

        final Split split = MavenVersion.parseToSplit("1.0.0-foo.0.0");

        final Split trimmed = split.trim();
        
        assertThat(trimmed.getStrings()).isEqualTo(new String [] { "1", "foo" });
        assertThat(trimmed.getSeparators()).isEqualTo(new char[] { '-' });
    }

    @Test
    public void testTrim_1_0_0_dash_0_0_0() {
        
        final Split split = new Split(
                new String [] { "1", "0", "0", "0", "0", "0" },
                new char [] { '.', '.', '-', '.', '.' });
        
        final Split trimmed = split.trim();
        
        assertThat(trimmed.getStrings()).isEqualTo(new String [] { "1" });
        assertThat(trimmed.getSeparators()).isEqualTo(new char[] { });
    }
}
