package com.neaterbits.build.buildsystem.maven.project.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProfile;
import com.neaterbits.util.parse.context.Context;

final class StackProfiles extends StackBase {

    private final List<MavenProfile> profiles;
    
    StackProfiles(Context context) {
        super(context);
    
        this.profiles = new ArrayList<>();
    }

    void add(MavenProfile profile) {
        
        Objects.requireNonNull(profile);
        
        profiles.add(profile);
    }
    
    List<MavenProfile> getProfiles() {
        return profiles;
    }
}
