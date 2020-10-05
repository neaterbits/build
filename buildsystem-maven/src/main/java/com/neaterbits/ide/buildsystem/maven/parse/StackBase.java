package com.neaterbits.ide.buildsystem.maven.parse;

import java.util.Objects;

import com.neaterbits.compiler.util.Context;

public abstract class StackBase {

	private final Context context;

	StackBase(Context context) {
		
		Objects.requireNonNull(context);

		this.context = context;
	}

	public final Context getContext() {
		return context;
	}
}
