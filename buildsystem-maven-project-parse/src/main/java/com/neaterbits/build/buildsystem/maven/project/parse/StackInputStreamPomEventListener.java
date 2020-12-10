package com.neaterbits.build.buildsystem.maven.project.parse;

import java.io.File;

import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.util.parse.context.Context;

final class StackInputStreamPomEventListener extends BaseStackPomEventListener {

    private StackProject project;
    
    @Override
    public void onProjectEnd(Context context) {
        
        this.project = pop();
    }
    
    MavenProject makeProject(File rootDirectory) {
        return super.makeProject(project, rootDirectory);
    }
}
