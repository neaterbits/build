package com.neaterbits.build.buildsystem.maven.effective;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.effective.POMMerger.MergeFilter;
import com.neaterbits.build.buildsystem.maven.effective.POMMerger.MergeMode;
import com.neaterbits.build.buildsystem.maven.elements.MavenProject;
import com.neaterbits.build.buildsystem.maven.parse.PomTreeParser;
import com.neaterbits.build.buildsystem.maven.xml.MavenXMLProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReader;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;
import com.neaterbits.build.types.ModuleId;

public class EffectivePOMsHelper {

    private static final boolean DEBUG = Boolean.FALSE;
    
	private static class Effective<DOCUMENT> {
		
		private final DOCUMENT base;
		private final MavenXMLProject<DOCUMENT> effective;
		
		public Effective(DOCUMENT base, MavenXMLProject<DOCUMENT> effective) {
			
			Objects.requireNonNull(base);
			Objects.requireNonNull(effective);

			this.base = base;
			this.effective = effective;
		}
	}

	private static final String SUPER_POM = "<project></project>";

    private static <DOCUMENT> DOCUMENT makeSuperPOM(XMLReaderFactory<DOCUMENT> xmlReaderFactory, String superPomString) {
        
        final String pom = superPomString != null
                ? superPomString
                : SUPER_POM;
        
	    final InputStream inputStream = new ByteArrayInputStream(pom.getBytes());

	    try {
            final XMLReader<DOCUMENT> xmlReader = xmlReaderFactory.createReader(inputStream, "superpom.xml");
	    
            return xmlReader.readXML(null, null);
        } catch (XMLReaderException | IOException ex) {
            throw new IllegalStateException(ex);
        }
	}

	public static <NODE, ELEMENT extends NODE, DOCUMENT extends NODE>
		List<MavenProject> computeEffectiveProjects(
			List<MavenXMLProject<DOCUMENT>> projects,
			POMModel<NODE, ELEMENT, DOCUMENT> model,
			XMLReaderFactory<DOCUMENT> xmlReaderFactory,
			String superPomString) {

		final Map<MavenModuleId, Effective<DOCUMENT>> computed
			= new HashMap<>(projects.size() + 1);
		
		final POMMerger<NODE, ELEMENT, DOCUMENT> pomMerger = new POMMerger<>(model);
		
		final DOCUMENT superPom = makeSuperPOM(xmlReaderFactory, superPomString);
		
		final List<MavenProject> result = new ArrayList<>(projects.size());
		
		for (MavenXMLProject<DOCUMENT> project : projects) {

			final Effective<DOCUMENT> resolved = resolve(project, projects, computed, pomMerger, superPom);

			result.add(resolved.effective.getProject());
		}
		
		return result;
	}
	
	private static class MergePath {
		
		private final MergeMode mode;
		private final String [] path;
		
		MergePath(MergeMode mode, String ... path) {
			
			Objects.requireNonNull(mode);
			
			if (path.length == 0) {
				throw new IllegalArgumentException();
			}
			
			this.mode = mode;
			this.path = Arrays.copyOf(path, path.length);
		}
	}
	
	
	private static MergeMode getMergeMode(List<String> path, MergePath [] mergePaths, MergeMode defaultMergeMode) {
	    
        MergeMode mergeMode = defaultMergeMode;

        if (DEBUG) {
            System.out.println("-- merge filter " + path);
        }
    
        // Find the first path that matches, if any
        for (MergePath mergePath : mergePaths) {

            if (mergePath.path.length > path.size()) {
                
                // Can not match whole merge path but does all of path
                // match the merge path?
                
                boolean mergePathStartsWithPath = true;
                
                for (int i = 0; i < path.size(); ++ i) {

                    if (!path.get(i).equals(mergePath.path[i])) {
                        mergePathStartsWithPath = false;
                        break;
                    }
                }
    
                if (DEBUG) {
                    System.out.println("## starts with path " + mergePathStartsWithPath);
                }
        
                if (mergePathStartsWithPath) {
                    // Might match since matches part of path so make sure is moved over
                    mergeMode = MergeMode.ADD;
                }
                continue; 
            }

            int i;
            
            boolean matches = true;

            for (i = 0; i < mergePath.path.length; ++ i) {

                if (path.size() <= i) {
                    // Try next
                    matches = false;
                    break;
                }
                
                if (!path.get(i).equals(mergePath.path[i])) {
                    // Try next
                    matches = false;
                    break;
                }
            }
            
            // Matched all of merge path?
            if (matches && i == mergePath.path.length && i == path.size()) {
                
                if (DEBUG) {
                    System.out.println("## matched all for " + Arrays.toString(mergePath.path));
                }

                // Matched complete path, see how to merge

                mergeMode = findMergeModeFromMatchedPath(mergePath, path);
                break;
            }
        }
        
        return mergeMode;
	}

	private static final MergePath [] PATHS = new MergePath [] {
            
            new MergePath(MergeMode.MERGE, "project"),
            new MergePath(MergeMode.MERGE, "project", "dependencies"),
            new MergePath(MergeMode.ADD,   "project", "dependencies", "dependency"),
            new MergePath(MergeMode.ADD,   "project", "dependencies", "dependency", "groupId"),
            new MergePath(MergeMode.ADD,   "project", "dependencies", "dependency", "artifactId"),
            new MergePath(MergeMode.ADD,   "project", "dependencies", "dependency", "version"),
            new MergePath(MergeMode.ADD,   "project", "dependencies", "dependency", "scope"),
            
            new MergePath(MergeMode.REPLACE_SUB, "project", "properties")
        };
	
	private static final MergeFilter MERGE_BASE_FILTER = path -> getMergeMode(path, PATHS, MergeMode.NONE);

	private static final MergeFilter MERGE_FILTER = path -> getMergeMode(path, PATHS, MergeMode.ADD);
	
	private static MergeMode findMergeModeFromMatchedPath(MergePath mergePath, List<String> path) {
		
		final MergeMode mergeMode;
		
		switch (mergePath.mode) {
		case ADD:
			mergeMode = MergeMode.ADD;
			break;
			
		case MERGE:
		    mergeMode = MergeMode.MERGE;
		    break;
			
		case REPLACE:
			if (mergePath.path.length == path.size()) {
				// At node to be replaced
				mergeMode = MergeMode.REPLACE;
			}
			else if (mergePath.path.length < path.size()) {
				// Within merge path so just add to replaced node
				mergeMode = MergeMode.ADD;
			}
			else {
				throw new IllegalStateException();
			}
			break;
		
		case REPLACE_SUB:
			if (mergePath.path.length == path.size()) {
			    // add nodes further up in path
				mergeMode = MergeMode.ADD;
			}
			else if (mergePath.path.length + 1 == path.size()) {
				// At node to be replaced since direct sub entry
				mergeMode = MergeMode.REPLACE;
			}
			else if (mergePath.path.length + 1 < path.size()) {
				// Sub of merge path so just add to replaced node
				mergeMode = MergeMode.ADD;
			}
			else {
				throw new IllegalStateException();
			}
			break;

		default:
			throw new IllegalStateException();
		}
	
		return mergeMode;
	}
	
	private static <NODE, ELEMENT extends NODE, DOCUMENT extends NODE>
		Effective<DOCUMENT> resolve(
			MavenXMLProject<DOCUMENT> project,
			List<MavenXMLProject<DOCUMENT>> projects,
			Map<MavenModuleId, Effective<DOCUMENT>> computed,
			POMMerger<NODE, ELEMENT, DOCUMENT> pomMerger,
			DOCUMENT superPom) {

	    if (DEBUG) {
	        System.out.println("## resolve " + project.getProject().getModuleId().getArtifactId());
	    }

		Objects.requireNonNull(project);
		Objects.requireNonNull(projects);
		Objects.requireNonNull(computed);
		Objects.requireNonNull(pomMerger);
		Objects.requireNonNull(superPom);
	
		final MavenModuleId moduleId = project.getProject().getModuleId();
		
		if (computed.containsKey(moduleId)) {
			throw new IllegalStateException();
		}

		if (DEBUG) {
		    System.out.println("##----------------------------- initial document");
		    pomMerger.getModel().printDocument(project.getDocument(), System.out);
		}

		final DOCUMENT parentEffectiveBase = findEffectiveParentBasePom(
		                                            project,
		                                            projects,
		                                            computed,
		                                            pomMerger,
		                                            superPom);
		
		if (DEBUG) {
		    System.out.println("##----------------------------- parent effective");
		    pomMerger.getModel().printDocument(parentEffectiveBase, System.out);
		}
		
		// Current base document where only merging relevant fields
		final DOCUMENT mergedBase = pomMerger.merge(parentEffectiveBase, project.getDocument(), MERGE_BASE_FILTER);

		if (DEBUG) {
		    System.out.println("##----------------------------- merged base");
		    pomMerger.getModel().printDocument(mergedBase, System.out);
		
		    System.out.println("##----------------------------- document to merge");
		    pomMerger.getModel().printDocument(project.getDocument(), System.out);
		}
		
		// Effective document where adding all fields from sub pom
		final DOCUMENT mergedEffective = pomMerger.merge(parentEffectiveBase, project.getDocument(), MERGE_FILTER);

		if (DEBUG) {
		    System.out.println("##----------------------------- merged effective");
		    pomMerger.getModel().printDocument(mergedEffective, System.out);
		}
		
		final File rootDirectory = project.getProject().getRootDirectory();

		final MavenProject parsed = PomTreeParser.parseToProject(
                                        		        mergedEffective,
                                        		        pomMerger.getModel(),
                                        		        rootDirectory);

		final MavenXMLProject<DOCUMENT> mavenXMLProject = new MavenXMLProject<DOCUMENT>(mergedEffective, parsed);
		
		final Effective<DOCUMENT> effective = new Effective<DOCUMENT>(mergedBase, mavenXMLProject);

		if (computed.put(moduleId, effective) != null) {
			throw new IllegalStateException();
		}
		
		return effective;
	}
	
	// Find the parts from base POM that shall be merged with POM 
	private static <NODE, ELEMENT extends NODE, DOCUMENT extends NODE>
	    DOCUMENT findEffectiveParentBasePom(
	            MavenXMLProject<DOCUMENT> project,
	            List<MavenXMLProject<DOCUMENT>> projects,
	            Map<MavenModuleId, Effective<DOCUMENT>> computed,
	            POMMerger<NODE, ELEMENT, DOCUMENT> pomMerger,
	            DOCUMENT superPom) {
	    
        final MavenXMLProject<DOCUMENT> parentProject = findParentProject(projects, project);
        
        final DOCUMENT parentEffectiveBase;
        
        if (parentProject != null) {
            
            if (DEBUG) {
                System.out.println("## found parent project " + parentProject.getProject().getModuleId());
            }
            
            final ModuleId parentModuleId = parentProject.getProject().getModuleId();
        
            Effective<DOCUMENT> parentEffective = computed.get(parentModuleId);
            
            if (parentEffective == null) {
             
                if (DEBUG) {
                    System.out.println("## resolve parent effective");
                }
                
                // resolve parent
                parentEffective = resolve(parentProject, projects, computed, pomMerger, superPom);
                
                if (parentEffective == null) {
                    throw new IllegalStateException();
                }

                if (!computed.containsKey(parentModuleId)) {
                    throw new IllegalStateException();
                }
            }
            else {
                
                if (DEBUG) {
                    System.out.println("## found parent effective");
                }
            }

            parentEffectiveBase = parentEffective.base;
        }
        else {
            // No parent so merge from super POM
            parentEffectiveBase = superPom;
        }

        if (DEBUG) {
            System.out.println("## resolve end");
        }
        
        return parentEffectiveBase;
	}
	
	private static <DOCUMENT>
		MavenXMLProject<DOCUMENT> findParentProject(
				List<MavenXMLProject<DOCUMENT>> projects,
				MavenXMLProject<DOCUMENT> sub) {
		
	    final MavenModuleId parentModuleId = sub.getProject().getParentModuleId();

	    final MavenXMLProject<DOCUMENT> parentXMLProject;
	    
	    if (parentModuleId == null) {
	        parentXMLProject = null;
	    }
	    else {
	        parentXMLProject = projects.stream()
				.filter(p -> parentModuleId.equals(p.getProject().getModuleId()))
				.findFirst()
				.orElse(null);
	    }

	    return parentXMLProject;
	}
}
