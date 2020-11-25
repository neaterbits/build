package com.neaterbits.build.buildsystem.maven.common.parse.configuration;

import com.neaterbits.util.parse.context.Context;

public interface PlexusConfigurationListener {

    void onConfigurationStart(Context context);
    
    void onConfigurationEnd(Context context);

}
