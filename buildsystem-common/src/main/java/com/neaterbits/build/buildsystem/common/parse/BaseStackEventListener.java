package com.neaterbits.build.buildsystem.common.parse;

import java.util.Objects;

import com.neaterbits.util.ArrayStack;
import com.neaterbits.util.Stack;
import com.neaterbits.util.parse.context.Context;

public abstract class BaseStackEventListener implements TextEventListener {

    private final Stack<StackBase> stack;

    protected BaseStackEventListener() {

        this.stack = new ArrayStack<>();
    }

    protected final boolean hasEntries() {
        return !stack.isEmpty();
    }

    protected final void push(StackBase frame) {
        Objects.requireNonNull(frame);

        // System.out.println("push: " + frame.getClass().getSimpleName());

        stack.push(frame);
    }

    @SuppressWarnings("unchecked")
    protected final <T> T pop() {
        final T frame = (T)stack.pop();

        // System.out.println("pop: " + frame.getClass().getSimpleName());

        return frame;
    }

    @SuppressWarnings("unchecked")
    protected final <T> T get() {
        return (T)stack.get();
    }

    @Override
    public final void onText(Context context, String text) {

        final StackBase stackBase = get();

        if (stackBase instanceof StackText) {
            final StackText stackText = (StackText)stackBase;

            stackText.setText(text.trim());
        }
        else if (stackBase instanceof StackTexts) {
            final StackTexts stackTexts = (StackTexts)stackBase;
            
            stackTexts.addText(text);
        }
    }

    @Override
    public String toString() {
        return stack.toString(entry -> entry.getClass().getSimpleName());
    }
}
