package com.neaterbits.build.buildsystem.maven.plexus.components.model;

import java.util.Collections;
import java.util.List;

public final class PlexusComponentSet {

    private final List<PlexusComponent> components;

    public PlexusComponentSet(List<PlexusComponent> components) {
        this.components = components != null
                ? Collections.unmodifiableList(components)
                : null;
    }

    public List<PlexusComponent> getComponents() {
        return components;
    }
}
