package com.neaterbits.build.strategies.compilemodules;

import java.util.List;
import java.util.Objects;

import com.neaterbits.util.coll.Coll;

public class ParsedModule<PARSED_FILE> {

    private final List<ParsedWithCachedRefs<PARSED_FILE>> parsed;

    protected ParsedModule(List<ParsedWithCachedRefs<PARSED_FILE>> parsed) {

        Objects.requireNonNull(parsed);

        this.parsed = Coll.immutable(parsed);
    }
    
    protected ParsedModule(ParsedModule<PARSED_FILE> other) {
        this(other.parsed);
    }

    public final List<ParsedWithCachedRefs<PARSED_FILE>> getParsed() {
        return parsed;
    }
}
