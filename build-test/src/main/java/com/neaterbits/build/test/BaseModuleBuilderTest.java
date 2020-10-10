package com.neaterbits.build.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;

import com.neaterbits.build.buildsystem.common.ScanException;
import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.build.types.ModuleId;
import com.neaterbits.build.types.dependencies.ProjectDependency;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledModuleFileResource;
import com.neaterbits.build.types.resource.compile.CompiledModuleFileResourcePath;

public abstract class BaseModuleBuilderTest extends BaseBuildTest {

	protected abstract String getJarFileName(String moduleName, ModuleId rootModuleId);
	
	private static ProjectModuleResourcePath findOneModule(BuildRoot buildRoot, String match) {
		
		return findOne(buildRoot.getModules(), ProjectModuleResourcePath::getName, match);
	}

	private static <T> T findOne(Collection<T> collection, Function<T, String> getName, String match) {
		
		 final List<T> matches = collection.stream()
					.filter(item -> getName.apply(item).contains(match))
					.collect(Collectors.toList());
			
		assertThat(matches.size()).isEqualTo(1);
		 
		return matches.get(0);
	}

	@Test
	public void testTransitiveProjectDependencies() throws ScanException {

		final BuildRoot buildRoot = getBuildRoot();
	
		final ProjectModuleResourcePath root = findOneModule(buildRoot, "root");
		assertThat(root.getModuleId().getId().contains("root")).isTrue();
		
		final ProjectModuleResourcePath ideCommon = findOneModule(buildRoot, "build-common");

		assertThat(ideCommon.getModuleId().getId().contains("build-common")).isTrue();

		final List<ProjectDependency> directDependencies = buildRoot.getProjectDependenciesForProjectModule(ideCommon);
		
		assertThat(directDependencies).isNotNull();
		assertThat(directDependencies.isEmpty()).isFalse();
		
		for (ProjectDependency dependency : directDependencies) {

			assertThat(dependency.getModulePath()).isNotNull();
			assertThat(dependency.getCompiledModuleFilePath()).isNotNull();

			// System.out.println("## dependency " + dependency + "/" + resourcePath.getClass());

		}
		
		// Should depend on util
		final ProjectDependency utilDependency = findOne(
				directDependencies,
				dependency -> dependency.getModulePath().length() <= 1
							? "" : dependency.getModulePath().get(1).getName(),
				"buildsystem-common");
		
		assertThat(utilDependency.getCompiledModuleFilePath() instanceof CompiledModuleFileResourcePath).isTrue();
		assertThat(utilDependency.getCompiledModuleFilePath().get(2) instanceof CompiledModuleFileResource).isTrue();
		assertThat(utilDependency.getCompiledModuleFilePath().get(2).getName())
			.isEqualTo(getJarFileName("buildsystem-common", root.getModuleId()));
	
		/*
		
		final List<Dependency> transitiveDependencies = ModuleBuilderUtil.transitiveProjectDependencies(buildRoot, ideCommon);

		assertThat(transitiveDependencies.size()).isGreaterThan(0);

		for (Dependency dependency : transitiveDependencies) {
			System.out.println("## dependency " + dependency);
		}
		*/

	}
}
