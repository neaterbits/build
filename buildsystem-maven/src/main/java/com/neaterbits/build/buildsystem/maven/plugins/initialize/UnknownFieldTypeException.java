package com.neaterbits.build.buildsystem.maven.plugins.initialize;

final class UnknownFieldTypeException extends MojoInitializeException {

    private static final long serialVersionUID = 1L;

    public UnknownFieldTypeException(MojoExecutionContext context, String fieldType, String fieldName) {
        super(context, "Unknown field type " + fieldType + " for " + fieldName);
    }
}
