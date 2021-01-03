package com.neaterbits.build.strategies.compilemodules;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.util.parse.ParserException;

/**
 * Abstract interface for calling out to compiler
 * that can parse and parallelize across multiple modules
 * 
 * 
 */

public interface AllModulesCompiler<PARSED_FILE, CODE_MAP, RESOLVE_ERROR> {

    /**
     * Parse one source file and collect references about types and type references for that file.
     * 
     * @param sourceFile the source file to parse
     * @param charset expected charset of the file
     * @param codeMap code map to add types to
     * 
     * @return marker interface for parsed file and cached parse tree element refs to
     *         elements that would have to be swapped out in the resolve phase
     *         
     * @throws IOException in case of IO error
     */
    ParsedWithCachedRefs<PARSED_FILE, RESOLVE_ERROR>
    parseFile(SourceFileResourcePath sourceFile, Charset charset, CODE_MAP codeMap)
                throws IOException, ParserException;
    
    /**
     * Resolve all modules and all type references in all parsed files
     * on a single thread
     * 
     * @param module parsed module
     * @param codeMap code map with collected types and files
     * 
     * @return collection of all parsed modules and resulting codemap.
     */
    
    List<RESOLVE_ERROR> resolveParseTreeInPlaceFromCodeMap(
                                        ParsedModule<PARSED_FILE, RESOLVE_ERROR> modules,
                                        CODE_MAP codeMap);
    
}
