package com.neaterbits.build.buildsystem.maven.plugins;

public final class PluginExecutionException extends PluginException {

    private static final long serialVersionUID = 1L;

    public PluginExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
