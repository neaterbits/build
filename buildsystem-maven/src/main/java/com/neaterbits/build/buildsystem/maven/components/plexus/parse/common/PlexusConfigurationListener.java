package com.neaterbits.build.buildsystem.maven.components.plexus.parse.common;

import com.neaterbits.util.parse.context.Context;

public interface PlexusConfigurationListener {

    void onConfigurationStart(Context context);
    
    void onConfigurationEnd(Context context);

}
