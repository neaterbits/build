package com.neaterbits.build.buildsystem.maven.version;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MavenVersion implements Comparable<MavenVersion> {

    private static final String PAD0 = "0";
    
    private final String version;

    private final Split split;
    private final Split trimmed;

    public static MavenVersion parse(String versionString) {

        final Split split = parseToSplit(versionString);
        
        final Split trimmed = split.trim();
    
        return new MavenVersion(versionString, split, trimmed);
    }
    
    static Split parseToSplit(String versionString) {
        
        final CharArray separators = new CharArray();

        final String [] strings = split(versionString, separators);
        
        final Split split = new Split(strings, separators.get());

        return split;
    }
    
    static String [] split(String s, CharArray separators) {

        final int length = s.length();

        final List<String> strings = new ArrayList<String>(100);

        int last = -1;
        int lastSwitch = -1;
        
        char lastChar = 0;

        for (int i = 0; i < length; ++ i) {
            
            final char c = s.charAt(i);
            
            if (c == '.' || c == '-') {

                final String found;

                if (i == 0) {
                    // beginning of string
                    found = PAD0;
                }
                else if (i - last == 1) {
                    // no character since last
                    found = PAD0;
                }
                else {
                    // between this and last
                    found = toAdd(s, last, lastSwitch, i);
                }

                last = i;

                if (found == null) {
                    throw new IllegalStateException("should have found entry");
                }
                
                strings.add(found);
                
                separators.add(c);
            }
            else if (
                    i > 0
                    && (
                            (Character.isDigit(c) && !Character.isDigit(lastChar))
                         || (!Character.isDigit(c) && Character.isDigit(lastChar)))) {
                
                int addIndex = -1;
                
                if (lastSwitch == last) {
                    addIndex = lastSwitch;
                }
                else if (i - last > 1) {
                    addIndex = last + 1;
                }
                
                if (addIndex != -1) {
                    final String toAdd = s.substring(addIndex, i);
                    
                    last = i;
                    lastSwitch = i;
                    
                    strings.add(toAdd);
                    
                    separators.add('-');
                }
            }
            
            lastChar = c;
        }
        
        strings.add(toAdd(s, last, lastSwitch, length));

        return strings.toArray(new String[strings.size()]);
    }
    
    private static String toAdd(String s, int last, int lastSwitch, int endIndex) {

        final String toAdd = s.substring(
                lastSwitch == last && last != -1
                    ? lastSwitch
                    : last + 1,
                endIndex);
        
        return toAdd;
    }

    private MavenVersion(String version, Split split, Split trimmed) {
        
        Objects.requireNonNull(version);
        Objects.requireNonNull(split);
        Objects.requireNonNull(trimmed);
        
        this.version = version;
        this.split = split;
        this.trimmed = trimmed;
    }

    @Override
    public String toString() {
        return "MavenVersion [version=" + version + ", split=" + split + ", trimmed=" + trimmed + "]";
    }

    @Override
    public int compareTo(MavenVersion other) {
        
        final int result;
        
        if (this.trimmed.equals(other.trimmed)) {
            result = 0;
        }
        else {
            result = this.trimmed.compareTo(other.trimmed);
        }
        
        return result;
    }
}
