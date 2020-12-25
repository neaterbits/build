package com.neaterbits.build.buildsystem.common;

import java.io.File;
import java.util.List;
import java.util.function.Function;

import com.neaterbits.build.types.Build;
import com.neaterbits.build.types.ModuleId;
import com.neaterbits.structuredlog.binary.logging.LogContext;
import com.neaterbits.util.concurrency.dependencyresolution.executor.TargetBuildResult;
import com.neaterbits.util.concurrency.dependencyresolution.executor.logger.TargetExecutorLogger;
import com.neaterbits.util.concurrency.dependencyresolution.spec.TargetBuilderSpec;
import com.neaterbits.util.concurrency.scheduling.QueueAsyncExecutor;
import com.neaterbits.util.concurrency.scheduling.task.TaskContext;

public final class BuildSystemMain {
    
    @FunctionalInterface
    public interface OnBuildResult {
        
        void onResult(TargetBuildResult result, LogContext logContext);
    }
    
    public static boolean performBuild(
            BuildSystems buildSystems,
            String projectDirString,
            String [] args,
            Function<LogContext, TargetExecutorLogger> targetExecutorLogger,
            OnBuildResult onBuildResult) throws ScanException, ArgumentException {

        final File projectDir = new File(projectDirString);

        boolean buildPerformed = false;
        
        if (projectDir.isDirectory() && projectDir.canRead()) {

            final BuildSystem buildSystem = buildSystems.findBuildSystem(projectDir);

            if (buildSystem != null) {
                
                build(buildSystem, projectDir, args, targetExecutorLogger, onBuildResult);

                buildPerformed = true;
            }
        }

        return buildPerformed;
    }

    public static <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY, REPOSITORY, CONTEXT extends TaskContext>
    void build(
            BuildSystem buildSystem,
            File projectDir,
            String [] buildSystemArgs,
            Function<LogContext, TargetExecutorLogger> targetExecutorLogger,
            OnBuildResult onBuildResult) throws ScanException, ArgumentException {
        
        final BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY, REPOSITORY> buildSystemRoot
            = buildSystem.scan(projectDir);
        
        final BuildSpecifier<CONTEXT> buildSpecifier = buildSystem.getBuildSpecifier();

        final List<Build<CONTEXT>> builds = buildSpecifier.specifyBuild(buildSystemArgs);
        
        final CONTEXT context = buildSpecifier.createTaskContext(buildSystemRoot);
        
        for (Build<CONTEXT> build : builds) {
            build(build.getTargetBuilder(), context, build.getTargetName(), targetExecutorLogger, onBuildResult);
        }
    }
    
    private static <CONTEXT extends TaskContext>
    void build(
            TargetBuilderSpec<CONTEXT> targetBuilderSpec,
            CONTEXT context,
            String targetName,
            Function<LogContext, TargetExecutorLogger> targetExecutorLogger,
            OnBuildResult onBuildResult) {
        
        final LogContext logContext = new LogContext();

        final QueueAsyncExecutor asyncExecutor = new QueueAsyncExecutor(false);

        targetBuilderSpec.execute(
                logContext,
                context,
                targetName,
                targetExecutorLogger.apply(logContext),
                asyncExecutor,
                result -> {
                    
                    if (onBuildResult != null) {
                        onBuildResult.onResult(result, logContext);
                    }
                });
    }
}
