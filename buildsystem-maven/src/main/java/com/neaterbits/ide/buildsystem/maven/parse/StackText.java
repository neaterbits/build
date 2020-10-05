package com.neaterbits.ide.buildsystem.maven.parse;

import java.util.Objects;

import com.neaterbits.compiler.util.Context;

abstract class StackText extends StackBase {

	private String text;
	
	StackText(Context context) {
		super(context);
	}

	final String getText() {
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
