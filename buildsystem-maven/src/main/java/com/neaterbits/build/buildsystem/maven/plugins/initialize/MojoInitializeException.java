package com.neaterbits.build.buildsystem.maven.plugins.initialize;

public abstract class MojoInitializeException extends Exception {

    private static final long serialVersionUID = 1L;

    private static String text(MojoExecutionContext context, String message) {
        return "Mojo " + context.getDebugName() + ' ' + message;
    }
    
    MojoInitializeException(MojoExecutionContext context, String message) {
        super(text(context, message));
    }

    public MojoInitializeException(MojoExecutionContext context, String message, Throwable cause) {
        super(text(context, message), cause);
    }
}
