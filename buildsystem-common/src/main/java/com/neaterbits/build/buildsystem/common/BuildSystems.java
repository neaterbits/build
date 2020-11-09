package com.neaterbits.build.buildsystem.common;

import java.io.File;

public interface BuildSystems {
    
    BuildSystem findBuildSystem(File projectDir);

}
