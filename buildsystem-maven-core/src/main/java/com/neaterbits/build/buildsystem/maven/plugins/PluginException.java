package com.neaterbits.build.buildsystem.maven.plugins;

abstract class PluginException extends Exception {

    private static final long serialVersionUID = 1L;

    PluginException(String message, Throwable cause) {
        super(message, cause);
    }
}
