package com.neaterbits.build.strategies.compilemodules;

import java.util.Objects;

public final class ParsedWithCachedRefs<PARSED_FILE> {

    private final PARSED_FILE parsedFile;
    private final PossibleTypeRefs typeRefs;
    private final int codeMapFileNo;
    
    public ParsedWithCachedRefs(
            PARSED_FILE parsedFile,
            PossibleTypeRefs typeRefs,
            int codeMapFileNo) {

        Objects.requireNonNull(parsedFile);
        Objects.requireNonNull(typeRefs);
        
        this.parsedFile = parsedFile;
        this.typeRefs = typeRefs;
        this.codeMapFileNo = codeMapFileNo;
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
}
