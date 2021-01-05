package com.neaterbits.build.strategies.compilemodules;

import java.util.List;

import com.neaterbits.util.coll.Coll;

public abstract class BaseResolvedModule<PARSED_FILE, RESOLVE_ERROR> {

    private final ParsedModule<PARSED_FILE, RESOLVE_ERROR> parsedModule;
    private final List<RESOLVE_ERROR> resolveErrors;

    public BaseResolvedModule(
            ParsedModule<PARSED_FILE, RESOLVE_ERROR> parsedModule,
            List<RESOLVE_ERROR> resolveErrors) {
        
        this.parsedModule = parsedModule;
        this.resolveErrors = resolveErrors != null
                    ? Coll.immutable(resolveErrors)
                    : null;
    }
    
    public final ParsedModule<PARSED_FILE, RESOLVE_ERROR> getParsedModule() {
        return parsedModule;
    }

    public final List<RESOLVE_ERROR> getResolveErrors() {
        return resolveErrors;
    }
}
