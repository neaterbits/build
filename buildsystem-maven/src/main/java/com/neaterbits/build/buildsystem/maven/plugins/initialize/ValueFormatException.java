package com.neaterbits.build.buildsystem.maven.plugins.initialize;

public final class ValueFormatException extends ConfigureException {

    private static final long serialVersionUID = 1L;

    ValueFormatException(MojoExecutionContext context, String fieldName) {
        super(context, "Value format issue for field " + fieldName);
    }

    public ValueFormatException(MojoExecutionContext context, String message, Throwable cause) {
        super(context, message, cause);
    }
}
