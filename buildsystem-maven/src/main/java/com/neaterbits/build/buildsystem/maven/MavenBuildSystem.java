package com.neaterbits.build.buildsystem.maven;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.w3c.dom.Document;

import com.neaterbits.build.buildsystem.common.BuildSpecifier;
import com.neaterbits.build.buildsystem.common.BuildSystem;
import com.neaterbits.build.buildsystem.common.BuildSystemRoot;
import com.neaterbits.build.buildsystem.common.ScanException;
import com.neaterbits.build.buildsystem.maven.effective.EffectivePOMsHelper;
import com.neaterbits.build.buildsystem.maven.effective.MavenResolveContext;
import com.neaterbits.build.buildsystem.maven.elements.MavenProject;
import com.neaterbits.build.buildsystem.maven.targets.MavenBuildSpecifier;
import com.neaterbits.build.buildsystem.maven.xml.MavenXMLProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;
import com.neaterbits.build.buildsystem.maven.xml.dom.DOMModel;
import com.neaterbits.build.buildsystem.maven.xml.dom.DOMReaderFactory;
import com.neaterbits.build.types.ModuleId;
import com.neaterbits.util.concurrency.scheduling.task.TaskContext;

public final class MavenBuildSystem implements BuildSystem {

    private final MavenRepositoryAccess repositoryAccess; 
    
    public MavenBuildSystem() {
        this(new URLMavenRepositoryAccess());
    }
    
	MavenBuildSystem(MavenRepositoryAccess repositoryAccess) {
    
	    Objects.requireNonNull(repositoryAccess);
	    
	    this.repositoryAccess = repositoryAccess;
    }

    @Override
	public boolean isBuildSystemFor(File rootDirectory) {

		final File pomFile = new File(rootDirectory, "pom.xml");

		return pomFile.exists() && pomFile.isFile() && pomFile.canRead();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY>
	BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY> scan(File rootDirectory) throws ScanException {

		final XMLReaderFactory<Document> xmlReaderFactory = new DOMReaderFactory();
		
		final List<MavenXMLProject<Document>> mavenXMLProjects;

		try {
			mavenXMLProjects = MavenModulesReader.readModules(rootDirectory, xmlReaderFactory);
		} catch (XMLReaderException | IOException ex) {
			throw new ScanException("Failed to scan project", ex);
		}
		
		final MavenResolveContext resolveContext = MavenResolveContext.now();
	
		final List<MavenProject> mavenProjects
			= EffectivePOMsHelper.computeEffectiveProjects(
					mavenXMLProjects,
					DOMModel.INSTANCE,
					xmlReaderFactory,
					null,
					resolveContext);

		return (BuildSystemRoot)new MavenBuildRoot(mavenProjects, xmlReaderFactory, repositoryAccess);
	}

    @SuppressWarnings("unchecked")
    @Override
    public <CONTEXT extends TaskContext> BuildSpecifier<CONTEXT> getBuildSpecifier() {
        
        return (BuildSpecifier<CONTEXT>)new MavenBuildSpecifier();
    }
}
