package com.neaterbits.build.strategies.compilemodules;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ParsedWithCachedRefs<PARSED_FILE, RESOLVE_ERROR> {

    private final PARSED_FILE parsedFile;
    private final PossibleTypeRefs typeRefs;
    private final int codeMapFileNo;
    private final List<RESOLVE_ERROR> resolveErrors;
    
    public ParsedWithCachedRefs(
            PARSED_FILE parsedFile,
            PossibleTypeRefs typeRefs,
            int codeMapFileNo) {

        Objects.requireNonNull(parsedFile);
        Objects.requireNonNull(typeRefs);
        
        this.parsedFile = parsedFile;
        this.typeRefs = typeRefs;
        this.codeMapFileNo = codeMapFileNo;
        this.resolveErrors = new ArrayList<>();
    }

    public PARSED_FILE getParsedFile() {
        return parsedFile;
    }

    public PossibleTypeRefs getTypeRefs() {
        return typeRefs;
    }

    public int getCodeMapFileNo() {
        return codeMapFileNo;
    }

    public List<RESOLVE_ERROR> getResolveErrorsList() {
        return resolveErrors;
    }
}
