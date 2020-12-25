package com.neaterbits.build.strategies.compilemodules;

import java.util.List;

import com.neaterbits.util.coll.Coll;

public class ResolvedProgram<PARSED_FILE> {

    private final List<ParsedModule<PARSED_FILE>> modules;

    ResolvedProgram(List<ParsedModule<PARSED_FILE>> modules) {
        
        this.modules = Coll.immutable(modules);
    }

    public List<ParsedModule<PARSED_FILE>> getModules() {
        return modules;
    }
}
