package com.neaterbits.build.buildsystem.common;

import java.util.List;
import java.util.Objects;

import com.neaterbits.util.concurrency.dependencyresolution.spec.TargetBuilderSpec;
import com.neaterbits.util.concurrency.scheduling.task.TaskContext;

public interface BuildSpecifier<CONTEXT extends TaskContext> {

    public static class Build<CTX extends TaskContext> {
        private final TargetBuilderSpec<CTX> targetBuilder;
        private final String targetName;
        
        public Build(TargetBuilderSpec<CTX> targetBuilder, String targetName) {

            Objects.requireNonNull(targetBuilder);
            Objects.requireNonNull(targetName);
            
            this.targetBuilder = targetBuilder;
            this.targetName = targetName;
        }

        public TargetBuilderSpec<CTX> getTargetBuilder() {
            return targetBuilder;
        }

        public String getTargetName() {
            return targetName;
        }
    }
    
    List<Build<CONTEXT>> specifyBuild(String [] args) throws ArgumentException;

    CONTEXT createTaskContext(BuildSystemRootScan buildSystemRoot);
}
