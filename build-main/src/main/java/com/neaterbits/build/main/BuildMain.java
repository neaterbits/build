package com.neaterbits.build.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

import javax.xml.bind.JAXBException;

import com.neaterbits.build.buildsystem.common.ArgumentException;
import com.neaterbits.build.buildsystem.common.BuildSystemMain;
import com.neaterbits.build.buildsystem.common.ScanException;
import com.neaterbits.structuredlog.binary.logging.LogContext;
import com.neaterbits.structuredlog.xml.model.Log;
import com.neaterbits.structuredlog.xml.model.LogIO;
import com.neaterbits.util.concurrency.dependencyresolution.executor.TargetBuildResult;
import com.neaterbits.util.concurrency.dependencyresolution.executor.logger.BinaryTargetExecutorLogger;
import com.neaterbits.util.concurrency.dependencyresolution.executor.logger.DelegatingTargetExecutorLogger;
import com.neaterbits.util.concurrency.dependencyresolution.executor.logger.StructuredTargetExecutorLogger;
import com.neaterbits.util.concurrency.dependencyresolution.executor.logger.TargetExecutorLogger;

public class BuildMain {

	private static void usage() {
		System.err.println("usage: build [... build system specific arguments ...]");
	}
	
	public static void main(String [] args) throws ScanException {

	    final AllBuildSystems buildSystems = new AllBuildSystems(); 
	    
	    boolean buildPerformed = false;

        final StructuredTargetExecutorLogger structuredLogger = new StructuredTargetExecutorLogger(target -> new ArrayList<>());

        final Function<LogContext, TargetExecutorLogger> getLogger = logContext -> {

	        // final TargetExecutorLogger logger = new PrintlnTargetExecutorLogger();
	        final BinaryTargetExecutorLogger binaryTargetExecutorLogger = new BinaryTargetExecutorLogger(logContext);
	        
	        final DelegatingTargetExecutorLogger delegatingLogger = new DelegatingTargetExecutorLogger(
	                structuredLogger,
	                binaryTargetExecutorLogger);

	        return delegatingLogger; // new PrintlnTargetExecutorLogger();
	    };
	    
	    try {
    		if (args.length >= 1) {
    		    
    		    // First argument may be project dir
    		    buildPerformed = BuildSystemMain.performBuild(
    		            buildSystems,
    		            args[0],
    		            Arrays.copyOfRange(args, 1, args.length),
                        getLogger,
                        (TargetBuildResult result, LogContext lc) -> processResult(result, structuredLogger, lc));
    		}
    		
    		if (!buildPerformed) {
    	        
    	        final String userDir = System.getProperty("user.dir");

    	        if (userDir != null) {
    
    	            buildPerformed = BuildSystemMain.performBuild(
    	                    buildSystems,
    	                    userDir,
    	                    args,
    	                    getLogger,
    	                    (TargetBuildResult result, LogContext lc) -> processResult(result, structuredLogger, lc));
    	        }
    		}
    		
    		if (!buildPerformed) {
    		    usage();
    		}
	    }
	    catch (ArgumentException ex) {
	        usage();
	    }
	}
	
    private static void processResult(
            TargetBuildResult result,
            StructuredTargetExecutorLogger structuredLogger,
            LogContext logContext) {

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
    }
}
