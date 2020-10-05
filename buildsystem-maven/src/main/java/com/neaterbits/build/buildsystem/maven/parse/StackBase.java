package com.neaterbits.build.buildsystem.maven.parse;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

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
