package com.neaterbits.build.strategies.compilemodules;

import com.neaterbits.util.IntList;

/**
 * Types and ref information extracted from files when parsed.
 */
public final class PossibleTypeRefs {

    private final IntList list;

    public PossibleTypeRefs() {
        this.list = new IntList();
    }
    
    public void addPossibleTypeReference(int parseTreeRef) {
        list.add(parseTreeRef);
    }
    
    public int [] getTypeReferences() {
        
        return list.toArray();
    }
}
