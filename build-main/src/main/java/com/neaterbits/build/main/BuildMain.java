package com.neaterbits.build.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.xml.bind.JAXBException;

import com.neaterbits.build.buildsystem.common.BuildSystem;
import com.neaterbits.build.buildsystem.common.ScanException;
import com.neaterbits.build.common.tasks.ModulesBuildContext;
import com.neaterbits.build.language.java.jdk.JavaBuildableLanguage;
import com.neaterbits.build.language.java.jdk.JavaCompiler;
import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.build.model.BuildRootImpl;
import com.neaterbits.build.types.ModuleId;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.structuredlog.binary.logging.LogContext;
import com.neaterbits.structuredlog.xml.model.Log;
import com.neaterbits.structuredlog.xml.model.LogIO;
import com.neaterbits.util.concurrency.dependencyresolution.executor.logger.BinaryTargetExecutorLogger;
import com.neaterbits.util.concurrency.dependencyresolution.executor.logger.DelegatingTargetExecutorLogger;
import com.neaterbits.util.concurrency.dependencyresolution.executor.logger.StructuredTargetExecutorLogger;
import com.neaterbits.util.concurrency.dependencyresolution.spec.TargetBuilderSpec;
import com.neaterbits.util.concurrency.scheduling.QueueAsyncExecutor;

public class BuildMain {

	private static void usage() {
		System.err.println("usage: build [... build system specific arguments ...]");
	}
	
	public static void main(String [] args) throws ScanException {

	    final BuildSystems buildSystems = new BuildSystems(); 
	    
	    boolean buildPerformed = false;
	    
		if (args.length >= 1) {

		    // First argument may be project dir
		    buildPerformed = performBuild(buildSystems, args[0], args, 1);
		}
		
		if (!buildPerformed) {
	        
	        final String userDir = System.getProperty("user.dir");
	        if (userDir != null) {

	            buildPerformed = performBuild(buildSystems, userDir, args, 0);
	        }
		}
		
		if (!buildPerformed) {
		    usage();
		}
	}
	
	private static boolean performBuild(BuildSystems buildSystems, String projectDirString, String [] args, int usedArgs) throws ScanException {

        final File projectDir = new File(projectDirString);

        boolean buildPerformed = false;
        
        if (projectDir.isDirectory() && projectDir.canRead()) {

            final BuildSystem buildSystem = buildSystems.findBuildSystem(projectDir);

            if (buildSystem != null) {
                
                final String [] buildSystemArgs = usedArgs > 0
                        ? Arrays.copyOfRange(args, usedArgs, args.length)
                        : args;
                
                build(buildSystem, projectDir, buildSystemArgs);
            }
        }

        return buildPerformed;
	}

	private static <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY> void build(
	        BuildSystem buildSystem,
	        File projectDir,
	        String [] buildSystemArgs) throws ScanException {
	    
        final BuildRoot buildRoot = new BuildRootImpl<>(projectDir, buildSystem.scan(projectDir));
        
        System.out.println("Modules to build:");
        
        for (ProjectModuleResourcePath module : buildRoot.getModules()) {
            System.out.println(module.getName());
        }

        final TargetBuilderSpec<ModulesBuildContext> targetBuilderModules = buildSystem.specifyBuild(buildSystemArgs);
        
        final ModulesBuildContext context = new ModulesBuildContext(
                buildRoot,
                new JavaBuildableLanguage(),
                new JavaCompiler(),
                null);

        // final TargetExecutorLogger logger = new PrintlnTargetExecutorLogger();
        final StructuredTargetExecutorLogger structuredLogger = new StructuredTargetExecutorLogger();
        final LogContext logContext = new LogContext();
        final BinaryTargetExecutorLogger binaryTargetExecutorLogger = new BinaryTargetExecutorLogger(logContext);
        final DelegatingTargetExecutorLogger delegatingLogger = new DelegatingTargetExecutorLogger(
                structuredLogger,
                binaryTargetExecutorLogger);
        
        final QueueAsyncExecutor asyncExecutor = new QueueAsyncExecutor(false);

        targetBuilderModules.execute(logContext, context, delegatingLogger, asyncExecutor, result -> {

            final Log log = structuredLogger.makeLog();
            
            final LogIO logIO = new LogIO();
            
            try (FileOutputStream outputStream = new FileOutputStream(new File("buildlog.xml"))) {
                logIO.writeLog(log, outputStream);
            } catch (IOException | JAXBException ex) {
                throw new IllegalStateException(ex);
            }

            try (FileOutputStream outputStream = new FileOutputStream(new File("binarylog"))) {
                logContext.writeLogBufferToOutput(outputStream);
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        });
	}
}
