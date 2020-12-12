package com.neaterbits.build.buildsystem.maven.effective;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.neaterbits.build.buildsystem.maven.MavenConstants;
import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.effective.POMMerger.MergeFilter;
import com.neaterbits.build.buildsystem.maven.effective.POMMerger.MergeMode;
import com.neaterbits.build.buildsystem.maven.project.model.MavenBuild;
import com.neaterbits.build.buildsystem.maven.project.model.MavenBuildPlugin;
import com.neaterbits.build.buildsystem.maven.project.model.MavenCommon;
import com.neaterbits.build.buildsystem.maven.project.model.MavenDependencyManagement;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPluginManagement;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPluginRepository;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProfile;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.project.model.MavenReleases;
import com.neaterbits.build.buildsystem.maven.project.model.MavenRepository;
import com.neaterbits.build.buildsystem.maven.project.model.MavenSnapshots;
import com.neaterbits.build.buildsystem.maven.project.model.xml.MavenXMLProject;
import com.neaterbits.build.buildsystem.maven.project.parse.PomTreeParser;
import com.neaterbits.build.buildsystem.maven.variables.MavenBuiltinVariables;
import com.neaterbits.build.buildsystem.maven.variables.VariableExpansion;
import com.neaterbits.build.buildsystem.maven.xml.XMLReader;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;
import com.neaterbits.build.buildsystem.maven.xml.model.DocumentModel;
import com.neaterbits.build.types.ModuleId;

public class EffectivePOMsHelper {

    private static final boolean DEBUG = Boolean.FALSE;
    
	private static class Effective<DOCUMENT> {
		
		private final DOCUMENT base;
		private final MavenProject effective;
		
		public Effective(DOCUMENT base, MavenProject effective) {
			
			Objects.requireNonNull(base);
			Objects.requireNonNull(effective);

			this.base = base;
			this.effective = effective;
		}
	}
	
	public static final MavenRepository CENTRAL_REPOSITORY
	            = new MavenRepository(
	                    new MavenReleases(null, "never", null),
	                    new MavenSnapshots(false, null, null),
	                    MavenConstants.CENTRAL_REPOSITORY_NAME,
                        MavenConstants.CENTRAL_REPOSITORY_ID,
                        MavenConstants.CENTRAL_REPOSITORY_URL,
	                    "default");

	public static final MavenPluginRepository CENTRAL_PLUGIN_REPOSITORY
       = new MavenPluginRepository(CENTRAL_REPOSITORY);

	private static final String SUPER_POM
	  = "<project>"

      + "  <repositories>"
      + "    <repository>"
      + "      <snapshots>"
      + "        <enabled>"
                 + CENTRAL_REPOSITORY.getSnapshots().getEnabled()
                 + "</enabled>"
      + "      </snapshots>"
      + "      <name>" + CENTRAL_REPOSITORY.getName() + "</name>"
      + "      <id>" + CENTRAL_REPOSITORY.getId() + "</id>"
      + "      <url>" + CENTRAL_REPOSITORY.getUrl() + "</url>"
      + "      <layout>"+ CENTRAL_REPOSITORY.getLayout() + "</layout>"
      + "    </repository>"
      + "  </repositories>"
	  
      + "  <pluginRepositories>"
      + "    <pluginRepository>"
      + "      <releases>"
      + "        <updatePolicy>" 
                  + CENTRAL_PLUGIN_REPOSITORY.getReleases().getUpdatePolicy()
                  + "</updatePolicy>"
      + "      </releases>"
      + "      <snapshots>"
      + "        <enabled>"
                 + CENTRAL_PLUGIN_REPOSITORY.getSnapshots().getEnabled()
                 + "</enabled>"
      + "      </snapshots>"
      + "      <name>" + CENTRAL_PLUGIN_REPOSITORY.getName() + "</name>"
      + "      <id>" + CENTRAL_PLUGIN_REPOSITORY.getId() + "</id>"
      + "      <url>" + CENTRAL_PLUGIN_REPOSITORY.getUrl() + "</url>"
      + "      <layout>" + CENTRAL_PLUGIN_REPOSITORY.getLayout() + "</layout>"
      + "    </pluginRepository>"
      + "  </pluginRepositories>"
      
      + "</project>";

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
			List<DocumentModule<DOCUMENT>> modules,
			DocumentModel<NODE, ELEMENT, DOCUMENT> model,
			XMLReaderFactory<DOCUMENT> xmlReaderFactory,
			String superPomString,
			MavenResolveContext resolveContext) {

		final Map<MavenModuleId, Effective<DOCUMENT>> computed
			= new HashMap<>(modules.size() + 1);
		
		final POMMerger<NODE, ELEMENT, DOCUMENT> pomMerger = new POMMerger<>(model);
		
		final DOCUMENT superPom = makeSuperPOM(xmlReaderFactory, superPomString);
		
		for (DocumentModule<DOCUMENT> module : modules) {
		    
		    // resolve() may compute recursively up hierarchy, so must verify not already added e.g. in case
		    // a sub project is before the root project in the list
		    if (!computed.containsKey(module.getModuleId())) {

		        resolve(module, modules, computed, pomMerger, superPom, resolveContext);
		    }
		}
		
		return modules.stream()
		        .map(p -> computed.get(p.getModuleId()).effective)
		        .collect(Collectors.toUnmodifiableList());
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

            if (DEBUG) {
                System.out.println("## matches " + matches);
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
            else if (    matches
                      && i == mergePath.path.length
                      && i == path.size() - 1
                      && mergePath.mode == MergeMode.REPLACE_SUB) {
                
                if (DEBUG) {
                    System.out.println("## matched REPLACE_SUB for " + Arrays.toString(mergePath.path));
                }

                // Matched complete path, see how to merge
                mergeMode = findMergeModeFromMatchedPath(mergePath, path);
                break;
            }
        }

        if (DEBUG) {
            System.out.println("-- merge filter " + path + " gives mode " + mergeMode);
        }
        
        return mergeMode;
	}

	private static final MergePath [] PATHS = new MergePath [] {
            
            new MergePath(MergeMode.MERGE, "project"),
            
            new MergePath(MergeMode.REPLACE, "project", "groupId"),
            new MergePath(MergeMode.REPLACE, "project", "version"),
            
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
		
	    Objects.requireNonNull(mergePath);
	    Objects.requireNonNull(path);
	    
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
				mergeMode = MergeMode.MERGE;
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
			DocumentModule<DOCUMENT> module,
			List<DocumentModule<DOCUMENT>> modules,
			Map<MavenModuleId, Effective<DOCUMENT>> computed,
			POMMerger<NODE, ELEMENT, DOCUMENT> pomMerger,
			DOCUMENT superPom,
			MavenResolveContext resolveContext) {

	    if (DEBUG) {
	        System.out.println("## resolve " + module.getModuleId());
	    }

		Objects.requireNonNull(module);
		Objects.requireNonNull(modules);
		Objects.requireNonNull(computed);
		Objects.requireNonNull(pomMerger);
		Objects.requireNonNull(superPom);
	
		final MavenModuleId moduleId = module.getModuleId();
		
		if (computed.containsKey(moduleId)) {
			throw new IllegalStateException();
		}

		if (DEBUG) {
		    System.out.println("##----------------------------- initial document");
		    pomMerger.getModel().printDocument(module.getXMLProject().getDocument(), System.out);
		}

		final DOCUMENT parentEffectiveBase = findEffectiveParentBasePom(
		                                            module,
		                                            modules,
		                                            computed,
		                                            pomMerger,
		                                            superPom,
		                                            resolveContext);
		
		if (DEBUG) {
		    System.out.println("##----------------------------- parent effective");
		    pomMerger.getModel().printDocument(parentEffectiveBase, System.out);
		}
		
		// Current base document where only merging relevant fields
		final DOCUMENT mergedBase = pomMerger.merge(parentEffectiveBase, module.getXMLProject().getDocument(), MERGE_BASE_FILTER);

		if (DEBUG) {
		    System.out.println("##----------------------------- merged base");
		    pomMerger.getModel().printDocument(mergedBase, System.out);
		
		    System.out.println("##----------------------------- document to merge");
		    pomMerger.getModel().printDocument(module.getXMLProject().getDocument(), System.out);
		}
		
		// Effective document where adding all fields from sub pom
		final DOCUMENT mergedEffective = pomMerger.merge(parentEffectiveBase, module.getXMLProject().getDocument(), MERGE_FILTER);

		if (DEBUG) {
		    System.out.println("##----------------------------- merged effective");
		    pomMerger.getModel().printDocument(mergedEffective, System.out);
		}
	
		final File rootDirectory = module.getXMLProject().getProject().getRootDirectory();

		// Parse into effective
		final MavenXMLProject<DOCUMENT> mavenXMLProject = parse(mergedEffective, pomMerger.getModel(), rootDirectory);
		
		final MavenXMLProject<DOCUMENT> mavenXMLProjectWithVarReplace
		    = replaceVariables(mavenXMLProject, pomMerger.getModel(), resolveContext);

		final MavenProject mavenProjectWithResolveDependencyManagement
          = replaceDependencyManagement(mavenXMLProjectWithVarReplace.getProject(), computed);

		final Effective<DOCUMENT> effective = new Effective<DOCUMENT>(mergedBase, mavenProjectWithResolveDependencyManagement);

		if (computed.put(moduleId, effective) != null) {
			throw new IllegalStateException();
		}
		
		return effective;
	}

	private static <DOCUMENT>
	MavenProject replaceDependencyManagement(
	                            MavenProject project,
	                            Map<MavenModuleId, Effective<DOCUMENT>> computed) {
	    
	    return new MavenProject(
	            project,
	            resolveDependenciesInMavenCommon(project, project.getCommon(), computed),
	            project.getProfiles() != null
	                ? resolveDependenciesInProfiles(project, project.getProfiles(), computed)
                    : null);
	}

    private static <DOCUMENT>
    MavenCommon resolveDependenciesInMavenCommon(
                                MavenProject project,
                                MavenCommon common,
                                Map<MavenModuleId, Effective<DOCUMENT>> computed) {

        return new MavenCommon(
                common,
                common.getBuild() != null
                    ? resolveDependenciesInMavenBuild(project, common.getBuild(), computed)
                    : null,
                common.getDependencies() != null
                    ? resolveDependencies(project, common.getDependencies(), computed)
                    : null);
    }

    private static <DOCUMENT>
    MavenBuild resolveDependenciesInMavenBuild(
                                MavenProject project,
                                MavenBuild build,
                                Map<MavenModuleId, Effective<DOCUMENT>> computed) {
        
        return new MavenBuild(
                build,
                build.getPluginManagement() != null
                    ? resolveDependenciesInPluginManagement(project, build.getPluginManagement(), computed)
                    : null,
                build.getPlugins() != null
                    ? resolveDependenciesInPlugins(project, build.getPlugins(), computed)
                    : null);
    }

    private static <DOCUMENT>
    List<MavenBuildPlugin> resolveDependenciesInPlugins(
                                MavenProject project,
                                List<MavenBuildPlugin> plugins,
                                Map<MavenModuleId, Effective<DOCUMENT>> computed) {
        
        return plugins.stream()
                .map(p -> resolveDependenciesInPlugin(project, p, computed))
                .collect(Collectors.toList());
    }

    private static <DOCUMENT>
    MavenBuildPlugin resolveDependenciesInPlugin(
                                MavenProject project,
                                MavenBuildPlugin plugin,
                                Map<MavenModuleId, Effective<DOCUMENT>> computed) {

        return plugin.getDependencies() != null
                ? new MavenBuildPlugin(
                        plugin,
                        resolveDependencies(project, plugin.getDependencies(), computed))
                : plugin;
    }

    private static <DOCUMENT>
    MavenPluginManagement resolveDependenciesInPluginManagement(
                                MavenProject project,
                                MavenPluginManagement pluginManagement,
                                Map<MavenModuleId, Effective<DOCUMENT>> computed) {

        return pluginManagement.getPlugins() != null
                ? new MavenPluginManagement(
                        resolveDependenciesInPlugins(project, pluginManagement.getPlugins(), computed))
                : pluginManagement;
    }

    private static <DOCUMENT>
    List<MavenProfile> resolveDependenciesInProfiles(
                                MavenProject project,
                                List<MavenProfile> profiles,
                                Map<MavenModuleId, Effective<DOCUMENT>> computed) {
        
        return profiles.stream()
                .map(p -> resolveDependenciesInProfile(project, p, computed))
                .collect(Collectors.toList());
    }

    private static <DOCUMENT>
    MavenProfile resolveDependenciesInProfile(
                                MavenProject project,
                                MavenProfile profile,
                                Map<MavenModuleId, Effective<DOCUMENT>> computed) {

        return profile.getCommon() != null
                ? new MavenProfile(profile, resolveDependenciesInMavenCommon(project, profile.getCommon(), computed))
                : profile;
    }

    private static <DOCUMENT>
    List<MavenDependency> resolveDependencies(
                                MavenProject project,
                                List<MavenDependency> dependencies,
                                Map<MavenModuleId, Effective<DOCUMENT>> computed) {
        
        return dependencies.stream()
                .map(d -> {
                    final MavenDependency updated = resolveDependency(project, d, computed);
                    
                    final MavenDependency result;
                    
                    if (updated == null) {
                        result = d;
                    }
                    else {
                        final MavenModuleId moduleId = new MavenModuleId(
                                d.getModuleId().getGroupId(),
                                d.getModuleId().getArtifactId(),
                                updated.getModuleId().getVersion() != null
                                    ? updated.getModuleId().getVersion()
                                    : d.getModuleId().getVersion()); 
                        
                        result = new MavenDependency(
                                moduleId,
                                d.getType(),
                                d.getClassifier(),
                                updated.getScope() != null
                                    ? updated.getScope()
                                    : d.getScope(),
                                d.getOptional(),
                                d.getExclusions());
                    }
                    
                    return result;
                })
                .collect(Collectors.toList());
    }
	
	private static <DOCUMENT>
	MavenDependency resolveDependency(
	                            MavenProject project,
	                            MavenDependency dependency,
	                            Map<MavenModuleId, Effective<DOCUMENT>> computed) {
	    
	    MavenDependency updated = null;
	    
	    if (project.getParent() != null) {
	        
	        final Effective<DOCUMENT> parentEffective = computed.get(project.getParentModuleId());
	        
	        if (parentEffective != null) {
	            updated = resolveDependency(parentEffective.effective, dependency, computed);
	        }
            else {
                throw new IllegalStateException("parentEffective " + project.getParentModuleId() + " not computed");
            }
	    }
	    
	    if (updated == null) {
	        
	        final MavenDependencyManagement depManagement = project.getCommon().getDependencyManagement();

	        if (depManagement != null && depManagement.getDependencies() != null) {
	        
	            updated = depManagement.getDependencies().stream()
	                    .filter(d ->    Objects.equals(d.getModuleId().getGroupId(), dependency.getModuleId().getGroupId())
	                                 && Objects.equals(d.getModuleId().getArtifactId(), dependency.getModuleId().getArtifactId()))
	                    .findFirst()
	                    .orElse(null);
	        }
	    }
	    
	    return updated;
	}
	
	private static <NODE, ELEMENT extends NODE, DOCUMENT extends NODE>
	    MavenXMLProject<DOCUMENT> parse(
	            DOCUMENT document,
	            DocumentModel<NODE, ELEMENT, DOCUMENT> model,
	            File rootDirectory) {
	    
        final MavenProject parsed = PomTreeParser.parseToProject(
                document,
                model,
                rootDirectory);

        final MavenXMLProject<DOCUMENT> mavenXMLProject = new MavenXMLProject<DOCUMENT>(document, parsed);

        return mavenXMLProject;
	}
	
	private static <NODE, ELEMENT extends NODE, DOCUMENT extends NODE>
	    MavenXMLProject<DOCUMENT> replaceVariables(
	            MavenXMLProject<DOCUMENT> mavenXMLProject,
	            DocumentModel<NODE, ELEMENT, DOCUMENT> pomModel,
	            MavenResolveContext resolveContext) {
	    
        final Map<String, String> properties = mavenXMLProject.getProject().getProperties();

        final MavenBuiltinVariables builtinVariables
            = new MavenBuiltinVariables(
                    mavenXMLProject.getProject().getRootDirectory(),
                    resolveContext.getBuildStartTime());
        
        final Function<String, String> replaceVariables
            = text -> VariableExpansion.replaceVariable(
                    text,
                    builtinVariables,
                    properties,
                    pomModel,
                    mavenXMLProject.getDocument());
        
        final DOCUMENT updated = pomModel.copyDocument(
                mavenXMLProject.getDocument(),
                replaceVariables);

        return parse(updated, pomModel, builtinVariables.getProjectBaseDir());
	}
	
	// Find the parts from base POM that shall be merged with POM 
	private static <NODE, ELEMENT extends NODE, DOCUMENT extends NODE>
	    DOCUMENT findEffectiveParentBasePom(
	            DocumentModule<DOCUMENT> module,
	            List<DocumentModule<DOCUMENT>> modules,
	            Map<MavenModuleId, Effective<DOCUMENT>> computed,
	            POMMerger<NODE, ELEMENT, DOCUMENT> pomMerger,
	            DOCUMENT superPom,
	            MavenResolveContext resolveContext) {
	    
        final DocumentModule<DOCUMENT> parentModule = findParentModule(modules, module);
        
        final DOCUMENT parentEffectiveBase;
        
        if (parentModule != null) {
            
            if (DEBUG) {
                System.out.println("## found parent project " + parentModule.getModuleId());
            }
            
            final ModuleId parentModuleId = parentModule.getModuleId();
        
            Effective<DOCUMENT> parentEffective = computed.get(parentModuleId);
            
            if (parentEffective == null) {
             
                if (DEBUG) {
                    System.out.println("## resolve parent effective");
                }
                
                // resolve parent
                parentEffective = resolve(parentModule, modules, computed, pomMerger, superPom, resolveContext);
                
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
		DocumentModule<DOCUMENT> findParentModule(
				List<DocumentModule<DOCUMENT>> modules,
				DocumentModule<DOCUMENT> sub) {
		
	    final MavenModuleId parentModuleId = sub.getXMLProject().getProject().getParentModuleId();

	    final DocumentModule<DOCUMENT> parentModule;
	    
	    if (parentModuleId == null) {
	        parentModule = null;
	    }
	    else {
	        parentModule = modules.stream()
				.filter(m -> parentModuleId.equals(m.getModuleId()))
				.findFirst()
				.orElse(null);
	    }

	    return parentModule;
	}
}
