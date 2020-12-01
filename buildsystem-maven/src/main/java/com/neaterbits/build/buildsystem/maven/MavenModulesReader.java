package com.neaterbits.build.buildsystem.maven;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.project.model.xml.MavenXMLProject;
import com.neaterbits.build.buildsystem.maven.project.parse.PomTreeParser;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;

public class MavenModulesReader {

	static <DOCUMENT> List<MavenXMLProject<DOCUMENT>> readModules(
								File baseDirectory,
								XMLReaderFactory<DOCUMENT> xmlReaderFactory) throws XMLReaderException, IOException {

		final List<MavenXMLProject<DOCUMENT>> modules = new ArrayList<>();
		
		readModules(baseDirectory.toPath(), modules, xmlReaderFactory, true);

		return modules;
	}

	private static <DOCUMENT> void readModules(
			Path pomDirectory,
			List<MavenXMLProject<DOCUMENT>> modules,
			XMLReaderFactory<DOCUMENT> xmlReaderFactory,
			boolean readAnyParentModules) throws XMLReaderException, IOException {

		// System.out.println("## read file " + pomDirectory.getPath());
		
		final File pomFile = pomDirectory.resolve("pom.xml").toFile();
		
		final MavenXMLProject<DOCUMENT> mavenProject = PomTreeParser.readModule(pomFile, xmlReaderFactory);
		
		if (mavenProject == null) {
			throw new IllegalStateException();
		}
		
		if (!modules.stream().anyMatch(module -> module.getProject().getModuleId().equals(mavenProject.getProject().getModuleId()))) {
    		
    		modules.add(mavenProject);
    		
    		readSubModules(pomDirectory, mavenProject.getProject(), modules, xmlReaderFactory);
    		
    		// Parent modules
    		if (readAnyParentModules) {
    		    readParentModules(pomDirectory, mavenProject, modules, xmlReaderFactory);
    		}
		}
	}
	
	private static <DOCUMENT> void readParentModules(
                            	        Path pomDirectory,
                            	        MavenXMLProject<DOCUMENT> mavenProject,
                            	        List<MavenXMLProject<DOCUMENT>> modules,
                            	        XMLReaderFactory<DOCUMENT> xmlReaderFactory) throws XMLReaderException, IOException {

	    for (MavenXMLProject<DOCUMENT> cur = mavenProject; cur != null;) {
            
            final MavenModuleId parentModuleId = cur.getProject().getParentModuleId();
            
            if (parentModuleId == null) {
                cur = null;
            }
            else {
                final String relativePath = cur.getProject().getParent().getRelativePath();
    
                final Path parentPomPath;
                
                if (relativePath != null) {
                    parentPomPath = pomDirectory.resolve(relativePath);
                }
                else {
                    parentPomPath = pomDirectory.resolve("..");
                }
                
                final File parentPomFile = parentPomPath.resolve("pom.xml").toFile();
            
                final MavenXMLProject<DOCUMENT> parentPom
                        = PomTreeParser.readModule(parentPomFile, xmlReaderFactory);
    
                if (parentPom == null) {
                    throw new IllegalStateException();
                }
                
                modules.add(parentPom);
                
                // read any submodules of the parent pom
                readSubModules(
                        parentPomPath,
                        parentPom.getProject(),
                        modules,
                        xmlReaderFactory);
                
                cur = parentPom;
            }
        }
	}
	
	private static <DOCUMENT> void readSubModules(
	        Path pomDirectory,
	        MavenProject project,
	        List<MavenXMLProject<DOCUMENT>> modules,
	        XMLReaderFactory<DOCUMENT> xmlReaderFactory) throws XMLReaderException, IOException {
	
	    final List<String> subModules = project.getCommon().getModules();
	        
        if (subModules != null) {
            
            for (String subModule : subModules) {
                readModules(pomDirectory.resolve(subModule), modules, xmlReaderFactory, false);
            }
        }
	}
}
