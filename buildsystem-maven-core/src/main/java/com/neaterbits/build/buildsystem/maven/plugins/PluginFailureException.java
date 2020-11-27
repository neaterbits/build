package com.neaterbits.build.buildsystem.maven.plugins;

public final class PluginFailureException extends PluginException {

    private static final long serialVersionUID = 1L;

    public PluginFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
