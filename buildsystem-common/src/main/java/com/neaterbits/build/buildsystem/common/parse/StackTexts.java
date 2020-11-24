package com.neaterbits.build.buildsystem.common.parse;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

public abstract class StackTexts extends StackBase {

    private StringBuilder sb;
    
    protected StackTexts(Context context) {
        super(context);
    }

    public final String getText() {
        return sb != null ? sb.toString() : null;
    }

    final void addText(String text) {
        
        Objects.requireNonNull(text);
        
        if (sb == null) {
            this.sb = new StringBuilder(text);
        }
        else {
            sb.append(text);
        }
    }
}
