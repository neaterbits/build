package com.neaterbits.build.buildsystem.maven.plugins.initialize;

final class MissingRequiredValueException extends ConfigureException {

    private static final long serialVersionUID = 1L;

    public MissingRequiredValueException(MojoExecutionContext context, String fieldName) {
        super(context, "Missing value for field " + fieldName);
    }
}
