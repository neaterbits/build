package com.neaterbits.build.buildsystem.maven.plexus.components.parse.common;

import com.neaterbits.util.parse.context.Context;

public interface PlexusRequirementsListener {

    void onRequirementsStart(Context context);
    
    void onRequirementStart(Context context);

    void onRoleStart(Context context);
    
    void onRoleEnd(Context context);
    
    void onRoleHintStart(Context context);
    
    void onRoleHintEnd(Context context);
    
    void onFieldNameStart(Context context);
    
    void onFieldNameEnd(Context context);

    void onFieldStart(Context context);
    
    void onFieldEnd(Context context);

    void onRequirementEnd(Context context);
    
    void onRequirementsEnd(Context context);

}
