package com.neaterbits.build.buildsystem.maven.plugins.initialize;

public final class TypeMismatchException extends MojoInitializeException {

    private static final long serialVersionUID = 1L;

    public TypeMismatchException(MojoExecutionContext context, String configured, String fieldType, String fieldName) {
        super(context, "Configured type " + configured + ", got field type " + fieldType + " for " + fieldName);
    }
}
