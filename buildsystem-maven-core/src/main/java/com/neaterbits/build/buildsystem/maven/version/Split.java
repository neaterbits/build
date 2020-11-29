package com.neaterbits.build.buildsystem.maven.version;

import java.util.Arrays;
import java.util.Objects;

class Split implements Comparable<Split> {
    
    private final String[] strings;
    private final char [] separators;

    Split(String[] strings, char[] separators) {
        
        Objects.requireNonNull(strings);
        Objects.requireNonNull(separators);

        if (strings.length < 1) {
            throw new IllegalArgumentException();
        }

        if (separators.length != strings.length - 1) {
            throw new IllegalArgumentException();
        }

        this.strings = strings;
        this.separators = separators;
    }
    
    // Trim according to version rules
    Split trim() {

        final String [] scratchStrings = new String[strings.length];
        
        trim(0, scratchStrings);
        
        int numNonNull = 0;
        
        for (int i = 0; i < scratchStrings.length; ++ i) {
            if (scratchStrings[i] != null) {
                ++ numNonNull;
            }
        }
        
        final String [] strings = new String[numNonNull];
        final char [] separators = new char[numNonNull - 1];
        
        int dstIdx = 0;
        int separatorIdx = 0;
        
        for (int i = 0; i < scratchStrings.length; ++ i) {
            
            if (scratchStrings[i] != null) {
                
                strings[dstIdx] = scratchStrings[i];

                // Set to separator prior to next match 
                if (dstIdx > 0 && i <= this.separators.length) {
                    separators[separatorIdx ++] = this.separators[i - 1];
                }

                ++ dstIdx;
            }
        }

        return new Split(strings, separators);
    }
    
    private static boolean isTrimmable(String string) {
        
        final boolean isTrimmable;
        
        switch (string) {
        case "0":
        case "":
        case "final":
        case "ga":
            isTrimmable = true;
            break;
            
        default:
            isTrimmable = false;
            break;
        }

        return isTrimmable;
    }
    
    private boolean trim(int index, String [] dstStrings) {
        
        final boolean isTrimmable = isTrimmable(this.strings[index]);;

        if (index == dstStrings.length - 1) {
        
            if (!isTrimmable) {
                dstStrings[index] = this.strings[index];
            }
        }
        else {
            final boolean lastWasTrimmed = trim(index + 1, dstStrings);

            if (!isTrimmable || (!lastWasTrimmed && separators[index] != '-')) {
                dstStrings[index] = this.strings[index];
            }
        }

        return isTrimmable;
    }

    String[] getStrings() {
        return Arrays.copyOf(strings, strings.length);
    }

    char[] getSeparators() {
        return Arrays.copyOf(separators, separators.length);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Split other = (Split) obj;
        if (!Arrays.equals(separators, other.separators))
            return false;
        if (!Arrays.equals(strings, other.strings))
            return false;
        return true;
    }
    
    private static int builtinStringToOrder(String s) {
        
        int value;
        
        switch (s) {
        case "alpha":
            value = 1;
            break;
            
        case "beta":
            value = 2;
            break;
            
        case "milestone":
            value = 3;
            break;
            
        case "rc":
        case "cr":
            value = 4;
            break;
            
        case "snapshot":
            value = 5;
            break;
            
        case "":
        case "final":
            value = 6;
            break;
            
        case "ga":
            value = 7;
            break;
            
        case "sp":
            value = 8;
            break;

        default:
            value = -1;
            break;
        }

        return value;
    }
    
    private static String nullValue(char separator) {

        final String nullValue;
        
        switch (separator) {
        case '.':
            nullValue = "0";
            break;
            
        case '-':
            nullValue = "";
            break;
            
        default:
            throw new UnsupportedOperationException();
        }

        return nullValue;
    }

    @Override
    public int compareTo(Split other) {

        final int len = Math.max(this.strings.length, other.strings.length);
        
        int result = 0;
        
        for (int i = 0; i < len; ++ i) {
        
            final String s;
            final char sep;
            
            if (i < this.strings.length) {
                s = this.strings[i];
                sep = i > 0 ? this.separators[i - 1] : 0;
            }
            else {
                sep = other.separators[i - 1];
                s = nullValue(sep);
            }
                    
            final String o;
            char osep;
            
            if (i < other.strings.length) {
                o = other.strings[i];
                osep = i > 0 ? other.separators[i - 1] : 0;
            }
            else {
                osep = this.separators[i - 1];
                o = nullValue(osep);
            }

            if (s.isEmpty() || o.isEmpty()) {
                result = compareStrings(s, o);
            }
            else {
                final char cs = s.charAt(0);
                final char co = o.charAt(0);
            
                if (Character.isDigit(cs) && Character.isDigit(co)) {

                    result = Integer.compare(Integer.parseInt(s), Integer.parseInt(o));

                    if (result == 0 && sep != osep) {
    
                        if (sep == '.' && osep == '-') {
                            result = 1;
                        }
                        else if (sep == '-' && osep == '.') {
                            result = -1;
                        }
                        else {
                            throw new IllegalStateException();
                        }
                    }
                }
                else if (Character.isDigit(cs) && !Character.isDigit(co)) {
                    result = 1;
                }
                else if (!Character.isDigit(cs) && Character.isDigit(co)) {
                    result = -1;
                }
                else {
                    // 'a', 'b', 'm' with hyphen
                    if (   nextIsHyphenInteger(i)
                        && other.nextIsHyphenInteger(i)) {
                        
                        result = compareStrings(unabbreviate(s), unabbreviate(o));
                    }
                    else {
                        result = compareStrings(s, o);
                    }
                    
                    if (result == 0 && sep != osep) {

                        if (sep == '.' && osep == '-') {
                            result = -1;
                        }
                        else if (sep == '-' && osep == '.') {
                            result = 1;
                        }
                        else {
                            throw new IllegalStateException();
                        }
                    }
                }
            }

            if (result != 0) {
                break;
            }
        }
        
        return result;
    }
    
    private static String unabbreviate(String s) {
        
        final String result;
        
        switch (s) {
        case "a":
            result = "alpha";
            break;
            
        case "b":
            result = "beta";
            break;
            
        case "m":
            result = "milestone";
            break;
            
        default:
            result = s;    
            break;
        }

        return result;
    }
    
    private boolean nextIsHyphenInteger(int index) {
        
        final boolean nextIsHyphenInteger;
        
        if (index + 1 >= strings.length) {
            nextIsHyphenInteger = false;
        }
        else {
            
            boolean isInteger = false;
            
            try {
                Integer.parseInt(strings[index + 1]);

                isInteger = true;
            }
            catch (NumberFormatException ex) {
            }
            
            nextIsHyphenInteger = isInteger && separators[index] == '-';
        }

        return nextIsHyphenInteger;
    }
    
    private static int compareStrings(String s, String o) {
        
        final int builtin1 = builtinStringToOrder(s);
        final int builtin2 = builtinStringToOrder(o);
        
        final int result;
        
        if (builtin1 != -1 && builtin2 != -1) {
            result = Integer.compare(builtin1, builtin2);
        }
        else if (builtin1 != -1) {
            result = -1;
        }
        else if (builtin2 != -1) {
            result = 1;
        }
        else {
            final int stringCompareResult = s.compareTo(o);
            
            result = stringCompareResult == 0
                    ? 0
                    : stringCompareResult > 0 ? 1 : -1;
        }

        return result;
    }
}