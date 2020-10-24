package com.neaterbits.build.buildsystem.maven.plugins.model;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import org.apache.maven.plugin.Mojo;

public final class MojoModel {

    private final Class<? extends Mojo> implementationClass;

    public MojoModel(Class<? extends Mojo> implementationClass) {
        
        Objects.requireNonNull(implementationClass);
        
        this.implementationClass = implementationClass;
    }

    public Mojo instantiate() {
        
        try {
            return implementationClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException ex) {

            throw new IllegalStateException(ex);
        }
    }

    @Override
    public String toString() {
        return "MojoModel [implementationClass=" + implementationClass + "]";
    }
}
