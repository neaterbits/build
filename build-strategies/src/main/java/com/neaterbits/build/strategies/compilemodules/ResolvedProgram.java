package com.neaterbits.build.strategies.compilemodules;

import java.util.List;

import com.neaterbits.util.coll.Coll;

public class ResolvedProgram<PARSED_FILE, RESOLVE_ERROR> {

    private final List<ParsedModule<PARSED_FILE, RESOLVE_ERROR>> modules;

    ResolvedProgram(List<ParsedModule<PARSED_FILE, RESOLVE_ERROR>> modules) {
        
        this.modules = Coll.immutable(modules);
    }

    public List<ParsedModule<PARSED_FILE, RESOLVE_ERROR>> getModules() {
        return modules;
    }
}
