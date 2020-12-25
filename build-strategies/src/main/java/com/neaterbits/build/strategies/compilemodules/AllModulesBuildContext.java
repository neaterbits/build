package com.neaterbits.build.strategies.compilemodules;

import java.util.List;
import java.util.Objects;

import com.neaterbits.util.coll.Coll;
import com.neaterbits.util.concurrency.scheduling.task.TaskContext;

public final class AllModulesBuildContext<PARSED_FILE, CODE_MAP, RESOLVE_ERROR> extends TaskContext {
    
    private final AllModulesCompiler<PARSED_FILE, CODE_MAP, RESOLVE_ERROR> compiler;
    private final List<CompileModule> modules;
    private final CODE_MAP codeMap;

    public AllModulesBuildContext(
            AllModulesCompiler<PARSED_FILE, CODE_MAP, RESOLVE_ERROR> compiler,
            List<CompileModule> modules,
            CODE_MAP codeMap) {
        
        Objects.requireNonNull(compiler);
        Objects.requireNonNull(modules);
        Objects.requireNonNull(codeMap);

        this.compiler = compiler;
        this.modules = Coll.immutable(modules);
        this.codeMap = codeMap;
    }

    AllModulesCompiler<PARSED_FILE, CODE_MAP, RESOLVE_ERROR> getCompiler() {
        return compiler;
    }

    List<CompileModule> getModules() {
        return modules;
    }

    CODE_MAP getCodeMap() {
        return codeMap;
    }
}
