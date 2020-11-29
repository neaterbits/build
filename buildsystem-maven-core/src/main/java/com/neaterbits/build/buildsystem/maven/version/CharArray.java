package com.neaterbits.build.buildsystem.maven.version;

import java.util.Arrays;

final class CharArray {

    private char [] chars;
    private int num;
    
    CharArray() {
        this.chars = new char[5];
        this.num = 0;
    }
    
    void add(char c) {
        if (num == chars.length) {
            chars = Arrays.copyOf(chars, chars.length * 3);
        }
        
        chars[num ++] = c;
    }
    
    char [] get() {
        return Arrays.copyOf(chars, num);
    }
}