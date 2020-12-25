package com.neaterbits.build.strategies.compilemodules;

import java.util.List;
import java.util.Objects;

import com.neaterbits.util.coll.Coll;

public final class ResolvedModule<PARSED_FILE, RESOLVE_ERROR> {

    private final CompileModule compileModule;
    private final ParsedModule<PARSED_FILE> parsedModule;
    private final List<RESOLVE_ERROR> resolveErrors;
    
    ResolvedModule(
            CompileModule compileModule,
            ParsedModule<PARSED_FILE> parsedModule,
            List<RESOLVE_ERROR> resolveErrors) {
        
        Objects.requireNonNull(compileModule);
        Objects.requireNonNull(parsedModule);
        
        this.compileModule = compileModule;
        this.parsedModule = parsedModule;
        this.resolveErrors = resolveErrors != null
                    ? Coll.immutable(resolveErrors)
                    : null;
    }

    public CompileModule getCompileModule() {
        return compileModule;
    }

    public ParsedModule<PARSED_FILE> getParsedModule() {
        return parsedModule;
    }

    public List<RESOLVE_ERROR> getResolveErrors() {
        return resolveErrors;
    }
}
