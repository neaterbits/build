package com.neaterbits.build.buildsystem.maven.common.parse.listeners;

import com.neaterbits.util.parse.context.Context;

public interface EntityEventListener {

    void onGroupIdStart(Context context);

    void onGroupIdEnd(Context context);

    void onArtifactIdStart(Context context);

    void onArtifactIdEnd(Context context);

    void onVersionStart(Context context);

    void onVersionEnd(Context context);

    void onPackagingStart(Context context);

    void onPackagingEnd(Context context);
}
