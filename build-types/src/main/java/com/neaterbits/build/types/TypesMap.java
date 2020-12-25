package com.neaterbits.build.types;

public interface TypesMap<T> {

	T lookupByScopedName(ScopedName scopedName);
}
