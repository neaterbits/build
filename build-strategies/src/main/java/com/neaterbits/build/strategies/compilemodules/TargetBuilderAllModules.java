package com.neaterbits.build.strategies.compilemodules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.util.concurrency.dependencyresolution.spec.TargetBuilderSpec;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.ActionParameters;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.ActionResult;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.FunctionActionLog;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.TargetBuilder;
import com.neaterbits.util.concurrency.scheduling.Constraint;
import com.neaterbits.util.parse.ParserException;

public final class TargetBuilderAllModules<PARSED_FILE, CODE_MAP, RESOLVE_ERROR>
        extends TargetBuilderSpec<AllModulesBuildContext<PARSED_FILE, CODE_MAP, RESOLVE_ERROR>> {
            
    private static class BuildSourceFile {
        
        private final SourceFileResourcePath sourceFile;
        private final CompileModule compileModule;
        
        public BuildSourceFile(SourceFileResourcePath sourceFile, CompileModule compileModule) {

            Objects.requireNonNull(sourceFile);
            Objects.requireNonNull(compileModule);
            
            this.sourceFile = sourceFile;
            this.compileModule = compileModule;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((sourceFile == null) ? 0 : sourceFile.hashCode());
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
            BuildSourceFile other = (BuildSourceFile) obj;
            if (sourceFile == null) {
                if (other.sourceFile != null)
                    return false;
            } else if (!sourceFile.equals(other.sourceFile))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "BuildSourceFile [sourceFile=" + sourceFile.getName() + "]";
        }
    }
    
    private static final String PARSE_AND_CODEMAP_PREREQUISITE = "parse_and_codemap";
    
    public static final String RESOLVED_TARGET = "resolved";

    @Override
    protected void buildSpec(TargetBuilder<AllModulesBuildContext<PARSED_FILE, CODE_MAP, RESOLVE_ERROR>> targetBuilder) {

        targetBuilder.addTarget(
                PARSE_AND_CODEMAP_PREREQUISITE,
                "parsed_modules_and_built_codemap",
                "parse_and_resolve",
                "Parse all files in all modules and resolve against each other and external dependencies")

        .withPrerequisites("Parsed modules")
        .fromIterating(context -> context.getModules())
        .buildBy(b -> {
            b.addInfoSubTarget(
                    CompileModule.class,
                    "module_source_files",
                    "parse_and_added_module",
                    module -> module.getPath().getName(),
                    module -> "Source file module " + module.getPath())
            
            .withPrerequisites("Parse source files")
            .fromIterating(Constraint.CPU, (context, target) -> target.getSourceFiles().stream()
                    .map(sourceFile -> new BuildSourceFile(sourceFile, target))
                    .collect(Collectors.toList()))

            .buildBy(sourceBuilder -> {
                sourceBuilder.addInfoSubTarget(
                        BuildSourceFile.class,
                        "parsed_source_file",
                        "parse_source_file_add_to_codemap",
                        bsf -> bsf.sourceFile.getName(),
                        bsf -> "Parse source file " + bsf.sourceFile.getName())

                .actionWithResult(Constraint.CPU_IO_CACHED, this::parseAndCollectRefs);
            })
            .actionWithResult(Constraint.CPU, (context, target, parameters) -> {
                
                final List<ParsedWithCachedRefs<PARSED_FILE, RESOLVE_ERROR>> parsedFiles = new ArrayList<>();
                
                for (SourceFileResourcePath sourceFile : target.getSourceFiles()) {
                    
                    final BuildSourceFile bsf = new BuildSourceFile(sourceFile, target);
                    
                    @SuppressWarnings("unchecked")
                    final ParsedWithCachedRefs<PARSED_FILE, RESOLVE_ERROR> parsed
                        = parameters.getTargetActionResult(bsf, ParsedWithCachedRefs.class);
                    parsedFiles.add(parsed);
                }
                
                final ParsedModule<PARSED_FILE, RESOLVE_ERROR> parsedModule = new ParsedModule<>(parsedFiles);
              
                return new ActionResult<>(parsedModule, FunctionActionLog.OK);
            });
        });
        
        targetBuilder.addTarget(
                RESOLVED_TARGET,
                "resolved_program",
                "resolve_program_from_codemap",
                "Resolve whole program from mparsed files and types collected in codemap")
        
        .withNamedPrerequisite(PARSE_AND_CODEMAP_PREREQUISITE)
        
        .withPrerequisites("Parsed modules and codemap")
        .fromIterating(context -> makeResolveModuleList(context.getModules()))
        
        .buildBy(programResolve -> programResolve
                .addInfoSubTarget(
                    ResolveModule.class,
                    "parsed_module",
                    "resolve_module",
                    module -> module.getCompileModule().getPath().getName(),
                    module -> "Resolve module " + module.getCompileModule().getPath())
                
                .withNamedPrerequisite(PARSE_AND_CODEMAP_PREREQUISITE)

                .actionWithResult(Constraint.CPU, (context, target, actionParameters) -> {

                    System.out.println("## resolve module");

                    @SuppressWarnings("unchecked")
                    final ParsedModule<PARSED_FILE, RESOLVE_ERROR> parsedModule
                        = actionParameters.getTargetActionResult(target.getCompileModule(), ParsedModule.class);

                    return resolve(context, target.getCompileModule(), parsedModule);
                }));
    }
    
    private static List<ResolveModule> makeResolveModuleList(List<CompileModule> compileModules) {

        return compileModules.stream()
            .map(ResolveModule::new)
            .collect(Collectors.toUnmodifiableList());
    }
    
    public static ResolveModule makeResolveModule(CompileModule compileModule) {

        return new ResolveModule(compileModule);
    }

    private ActionResult<ParsedWithCachedRefs<PARSED_FILE, RESOLVE_ERROR>>
            parseAndCollectRefs(
                    AllModulesBuildContext<PARSED_FILE, CODE_MAP, RESOLVE_ERROR> context,
                    BuildSourceFile target,
                    ActionParameters<BuildSourceFile> parametes) {
        
        ActionResult<ParsedWithCachedRefs<PARSED_FILE, RESOLVE_ERROR>> result; 
        
        try {
            final ParsedWithCachedRefs<PARSED_FILE, RESOLVE_ERROR> parsed = context.getCompiler().parseFile(
                    target.sourceFile,
                    target.compileModule.getCharset(),
                    context.getCodeMap());
            
            result = new ActionResult<>(parsed, FunctionActionLog.OK);
            
        } catch (IOException | ParserException ex) {
            result = new ActionResult<>(null, new FunctionActionLog(ex));
        }
        
        return result;
    }
    
    private ActionResult<ResolvedModule<PARSED_FILE, RESOLVE_ERROR>> resolve(
            AllModulesBuildContext<PARSED_FILE, CODE_MAP, RESOLVE_ERROR> context,
            CompileModule compileModule,
            ParsedModule<PARSED_FILE, RESOLVE_ERROR> parsedModule) {
        
        Objects.requireNonNull(context);
        Objects.requireNonNull(compileModule);
        Objects.requireNonNull(parsedModule);
        
        final List<RESOLVE_ERROR> errors
            = context.getCompiler().resolveParseTreeInPlaceFromCodeMap(parsedModule, context.getCodeMap());

        final ResolvedModule<PARSED_FILE, RESOLVE_ERROR> resolvedModule
            = new ResolvedModule<>(compileModule, parsedModule, errors);
        
        final ActionResult<ResolvedModule<PARSED_FILE, RESOLVE_ERROR>> result
            = new ActionResult<>(resolvedModule, FunctionActionLog.OK);
    
        return result;
    }
}
