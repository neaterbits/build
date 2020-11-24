package com.neaterbits.build.buildsystem.common.parse;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

public abstract class StackText extends StackBase {

	private String text;

	protected StackText(Context context) {
		super(context);
	}

	public final String getText() {
		return text;
	}

	final void setText(String text) {
		Objects.requireNonNull(text);

		if (this.text != null) {
			throw new IllegalStateException();
		}

		this.text = text;
	}
}
