package com.neaterbits.build.buildsystem.maven.plugins.initialize;

public abstract class ConfigureException extends MojoInitializeException {

    private static final long serialVersionUID = 1L;

    ConfigureException(MojoExecutionContext context, String message) {
        super(context, message);
    }

    public ConfigureException(MojoExecutionContext context, String message, Throwable cause) {
        super(context, message, cause);
    }
}
