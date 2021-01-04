package com.neaterbits.build.strategies.compilemodules;

import java.util.List;
import java.util.Objects;

import com.neaterbits.util.coll.Coll;

public class ParsedModule<PARSED_FILE, RESOLVE_ERROR> {

    private final List<ParsedWithCachedRefs<PARSED_FILE, RESOLVE_ERROR>> parsed;

    public ParsedModule(List<ParsedWithCachedRefs<PARSED_FILE, RESOLVE_ERROR>> parsed) {

        Objects.requireNonNull(parsed);

        this.parsed = Coll.immutable(parsed);
    }
    
    protected ParsedModule(ParsedModule<PARSED_FILE, RESOLVE_ERROR> other) {
        this(other.parsed);
    }

    public final List<ParsedWithCachedRefs<PARSED_FILE, RESOLVE_ERROR>> getParsed() {
        return parsed;
    }
}
