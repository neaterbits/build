package com.neaterbits.build.strategies.compilemodules;

import java.util.Objects;

// Separate class for ResolveModule to distinguish target from CompileModule
public final class ResolveModule {

    private final CompileModule compileModule;

    ResolveModule(CompileModule compileModule) {
        
        Objects.requireNonNull(compileModule);

        this.compileModule = compileModule;
    }

    CompileModule getCompileModule() {
        return compileModule;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((compileModule == null) ? 0 : compileModule.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ResolveModule other = (ResolveModule) obj;
        if (compileModule == null) {
            if (other.compileModule != null)
                return false;
        } else if (!compileModule.equals(other.compileModule))
            return false;
        return true;
    }
}
