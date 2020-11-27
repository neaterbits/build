package com.neaterbits.build.buildsystem.maven.effective;

import java.time.ZonedDateTime;
import java.util.Objects;

public final class MavenResolveContext {

    private final ZonedDateTime buildStartTime;

    public static MavenResolveContext now() {
        return new MavenResolveContext(ZonedDateTime.now());
    }
    
    public MavenResolveContext(ZonedDateTime buildStartTime) {

        Objects.requireNonNull(buildStartTime);

        this.buildStartTime = buildStartTime;
    }

    ZonedDateTime getBuildStartTime() {
        return buildStartTime;
    }
}
