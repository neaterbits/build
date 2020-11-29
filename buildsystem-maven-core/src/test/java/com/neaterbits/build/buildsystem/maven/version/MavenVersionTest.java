package com.neaterbits.build.buildsystem.maven.version;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class MavenVersionTest {

    @Test
    public void testSplit() {
    
        checkSplit("1", "1", new String [] { "1" }, new char [] { });
        
        checkSplit("1.0", "1.0", new String [] { "1", "0" }, new char [] { '.' });

        checkSplit(
                "1-1.foo-bar1baz-.1",
                "1-1.foo-bar-1-baz-0.1",
                new String [] { "1", "1", "foo", "bar", "1", "baz", "0", "1" },
                new char [] { '-', '.', '-', '-', '-', '-', '.' });
        
        checkSplit(
                "1-foo10",
                "1-foo-10",
                new String [] { "1", "foo", "10" },
                new char [] { '-', '-' });
    }
    
    @Test
    public void testCompare() {
        
        checkCompare("1", "1.1", -1);

        checkCompare("1-snapshot", "1-snapshot", 0);
        checkCompare("1-snapshot", "1", -1);
        checkCompare("1", "1-sp", -1);
        
        checkCompare("1-foo2", "1-foo10", -1);
        
        checkCompare("1.foo", "1-foo", -1);

        checkCompare("1-foo", "1-1", -1);
        checkCompare("1-1", "1.1", -1);
        
        checkCompare("1.ga", "1.ga", 0);
        checkCompare("1.ga", "1-ga", 0);
        checkCompare("1.ga", "1-0", 0);
        checkCompare("1.ga", "1.0", 0);
        checkCompare("1.ga", "1", 0);

        checkCompare("1-ga", "1-ga", 0);
        checkCompare("1-ga", "1-0", 0);
        checkCompare("1-ga", "1.0", 0);
        checkCompare("1-ga", "1", 0);

        checkCompare("1-0", "1-0", 0);
        checkCompare("1-0", "1.0", 0);
        checkCompare("1-0", "1", 0);

        checkCompare("1.0", "1.0", 0);
        checkCompare("1.0", "1", 0);

        checkCompare("1", "1", 0);
     
        checkCompare("1-sp", "1-ga", 1);
        checkCompare("1-sp", "1.ga", 1);

        checkCompare("1-sp.1", "1-ga.1", 1);

        checkCompare("1-ga-1", "1-1", 0);
        checkCompare("1-sp-1", "1-ga-1", -1);

        checkCompare("1-a1", "1-alpha-1", 0);
    }
    
    private void checkSplit(
            String version,
            String expectedString,
            String [] expectedStrings,
            char [] expectedSeparators) {

        final CharArray separators = new CharArray(); 
        
        final String [] strings = MavenVersion.split(version, separators);
        
        final char [] sep = separators.get();
        
        assertThat(sep.length).isEqualTo(strings.length - 1);
        
        assertThat(strings).isEqualTo(expectedStrings);

        final StringBuilder joined = new StringBuilder();
        
        for (int i = 0; i < expectedStrings.length; ++ i) {
            
            if (i > 0) {
                joined.append(sep[i - 1]);
            }
            
            joined.append(strings[i]);
        }
        
        assertThat(joined.toString()).isEqualTo(expectedString);
    }

    private void checkCompare(String versionString1, String versionString2, int result) {
        
        final MavenVersion version1 = MavenVersion.parse(versionString1);
        final MavenVersion version2 = MavenVersion.parse(versionString2);
        
        assertThat(version1.compareTo(version2)).isEqualTo(result);
        assertThat(version2.compareTo(version1)).isEqualTo(- result);
    }
}
