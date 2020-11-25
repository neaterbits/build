package com.neaterbits.build.buildsystem.maven.project.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.build.buildsystem.maven.project.model.MavenExecution;
import com.neaterbits.util.parse.context.Context;

final class StackExecutions extends StackBase {

    private final List<MavenExecution> executions;
    
    StackExecutions(Context context) {
        super(context);
        
        this.executions = new ArrayList<>();
    }

    void add(MavenExecution execution) {
        
        Objects.requireNonNull(execution);

        executions.add(execution);
    }
    
    List<MavenExecution> getExecutions() {
        return executions;
    }
}
