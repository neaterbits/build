package com.neaterbits.build.strategies.compilemodules;

import java.util.List;
import java.util.Objects;

public final class ResolvedModule<PARSED_FILE, RESOLVE_ERROR> 
    extends BaseResolvedModule<PARSED_FILE, RESOLVE_ERROR> {

    private final CompileModule compileModule;
    
    ResolvedModule(
            CompileModule compileModule,
            ParsedModule<PARSED_FILE, RESOLVE_ERROR> parsedModule,
            List<RESOLVE_ERROR> resolveErrors) {
        
        super(parsedModule, resolveErrors);
        
        Objects.requireNonNull(compileModule);
        
        this.compileModule = compileModule;
    }

    public CompileModule getCompileModule() {
        return compileModule;
    }
}
