package com.neaterbits.build.buildsystem.maven.plugins.initialize;

import org.apache.maven.plugin.Mojo;

final class MojoExecutionContext {

    private final Mojo mojo;

    MojoExecutionContext(Mojo mojo) {
        this.mojo = mojo;
    }
    
    Mojo getMojo() {
        return mojo;
    }

    String getDebugName() {
        
        return mojo.getClass().getName();
    }
}

