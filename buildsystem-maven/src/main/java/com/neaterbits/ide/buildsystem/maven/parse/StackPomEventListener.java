package com.neaterbits.ide.buildsystem.maven.parse;

import java.io.File;
import java.util.Objects;

import com.neaterbits.compiler.util.ArrayStack;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.Stack;
import com.neaterbits.ide.buildsystem.maven.elements.MavenBuild;
import com.neaterbits.ide.buildsystem.maven.elements.MavenDependency;
import com.neaterbits.ide.buildsystem.maven.elements.MavenExtension;
import com.neaterbits.ide.buildsystem.maven.elements.MavenPlugin;
import com.neaterbits.ide.buildsystem.maven.elements.MavenProject;
import com.neaterbits.ide.buildsystem.maven.elements.MavenReporting;

final class StackPomEventListener implements PomEventListener {

	private final File rootDirectory;
	
	private MavenProject mavenProject;
	
	private final Stack<StackBase> stack;

	StackPomEventListener(File rootDirectory) {
		
		Objects.requireNonNull(rootDirectory);
		
		this.rootDirectory = rootDirectory;
		this.stack = new ArrayStack<>();
	}

	private void push(StackBase frame) {
		Objects.requireNonNull(frame);

		// System.out.println("push: " + frame.getClass().getSimpleName());
		
		stack.push(frame);
	}
	
	@SuppressWarnings("unchecked")
	private <T> T pop() {
		final T frame = (T)stack.pop();
		
		// System.out.println("pop: " + frame.getClass().getSimpleName());

		return frame;
	}

	@SuppressWarnings("unchecked")
	private <T> T get() {
		return (T)stack.get();
	}
	
	public MavenProject getMavenProject() {
		return mavenProject;
	}

	@Override
	public void onProjectStart(Context context) {
		push(new StackProject(context));
	}

	@Override
	public void onText(Context context, String text) {

		final StackBase stackBase = get();
		
		if (stackBase instanceof StackText) {
			final StackText stackText = (StackText)stackBase;
			
			stackText.setText(text.trim());
		}
	}

	@Override
	public void onGroupIdStart(Context context) {

		push(new StackGroupId(context));

	}

	@Override
	public void onGroupIdEnd(Context context) {
		
		final StackGroupId stackGroupId = pop();
		
		final EntitySetter entitySetter = get();
		
		entitySetter.setGroupId(stackGroupId.getText());
	}

	
	@Override
	public void onArtifactIdStart(Context context) {

		push(new StackArtifactId(context));
	}

	@Override
	public void onArtifactIdEnd(Context context) {

		final StackArtifactId stackArtifactId = pop();
		
		final EntitySetter entitySetter = get();

		entitySetter.setArtifactId(stackArtifactId.getText());
	}

	@Override
	public void onVersionStart(Context context) {
		
		push(new StackVersion(context));
		
	}
	
	@Override
	public void onVersionEnd(Context context) {

		final StackVersion stackVersion = pop();
		
		final EntitySetter entitySetter = get();

		entitySetter.setVersion(stackVersion.getText());
	}
	
	@Override
	public void onParentStart(Context context) {
		
		push(new StackParent(context));
		
	}

	@Override
	public void onParentEnd(Context context) {

		final StackParent stackParent = pop();
		
		final StackProject stackProject = get();

		stackProject.setParentModuleId(stackParent.makeModuleId());
	}

	@Override
	public void onDependenciesStart(Context context) {
		push(new StackDependencies(context));
	}

	@Override
	public void onDependencyStart(Context context) {
		push(new StackDependency(context));
	}

	@Override
	public void onScopeStart(Context context) {
		push(new StackScope(context));
	}

	@Override
	public void onScopeEnd(Context context) {
		
		final StackScope stackScope = pop();
		
		final StackDependency stackDependency = get();
		
		stackDependency.setScope(stackScope.getText());
	}

	@Override
	public void onOptionalStart(Context context) {
		push(new StackOptional(context));
	}

	@Override
	public void onOptionalEnd(Context context) {

		final StackOptional stackOptional = pop();
		
		final StackDependency stackDependency = get();
		
		stackDependency.setOptional(stackOptional.getText());
	}

	@Override
	public void onDependencyEnd(Context context) {

		final StackDependency stackDependency = pop();
		
		final MavenDependency dependency = new MavenDependency(
				stackDependency.makeModuleId(),
				stackDependency.getPackaging(),
				stackDependency.getScope(),
				stackDependency.getOptional());
	
		final StackDependencies stackDependencies = get();
	
		stackDependencies.addDependency(dependency);
	}
	

	@Override
	public void onDependenciesEnd(Context context) {
		
		final StackDependencies stackDependencies = pop();
		
		final StackProject stackProject = get();
		
		stackProject.setDependencies(stackDependencies.getDependencies());
	}

	@Override
	public void onModulesStart(Context context) {

		push(new StackModules(context));
		
	}

	@Override
	public void onModuleStart(Context context) {

		push(new StackModule(context));
		
	}

	@Override
	public void onModuleEnd(Context context) {
		final StackModule stackModule = pop();
		
		final StackModules stackModules = get();
		
		stackModules.addModule(stackModule.getText());
	}

	@Override
	public void onModulesEnd(Context context) {

		final StackModules stackModules = pop();
		
		final StackProject stackProject = get();
		
		stackProject.setSubModules(stackModules.getModules());
	}
	
	@Override
	public void onReportingStart(Context context) {

		push(new StackReporting(context));
	}

	@Override
	public void onReportingEnd(Context context) {

		final StackReporting stackReporting = pop();
		
		final MavenReporting reporting = new MavenReporting(stackReporting.getPlugins());
		
		final StackProject stackProject = get();
		
		stackProject.setReporting(reporting);
	}

	@Override
	public void onBuildStart(Context context) {
		push(new StackBuild(context));
	}

	@Override
	public void onPluginsStart(Context context) {
		push(new StackPlugins(context));
	}

	@Override
	public void onPluginStart(Context context) {
		push(new StackPlugin(context));
	}

	@Override
	public void onPluginEnd(Context context) {

		final StackPlugin stackPlugin = pop();
		
		final MavenPlugin mavenPlugin = new MavenPlugin(stackPlugin.makeModuleId(), null);
		
		final StackPlugins stackPlugins = get();
		
		stackPlugins.addPlugin(mavenPlugin);
	}

	@Override
	public void onPluginsEnd(Context context) {

		final StackPlugins stackPlugins = pop();
		
		final PluginsSetter pluginsSetter = get();
		
		pluginsSetter.setPlugins(stackPlugins.getPlugins());
	}

	@Override
	public void onExtensionsStart(Context context) {
		
		if (get() instanceof StackBuild) {
			push(new StackExtensions(context));
		}
	}

	
	@Override
	public void onExtensionStart(Context context) {
		push(new StackExtension(context));
	}

	@Override
	public void onExtensionEnd(Context context) {
		
		final StackExtension stackExtension = pop();
		
		final StackExtensions stackExtensions = get();
		
		stackExtensions.addExtension(new MavenExtension(stackExtension.makeModuleId()));
	}

	@Override
	public void onExtensionsEnd(Context context) {
		
		if (get() instanceof StackExtensions) {
		
			final StackExtensions stackExtensions = pop();
			
			final StackBuild stackBuild = get();
			
			stackBuild.setExtensions(stackExtensions.getExtensions());
		}
	}

	@Override
	public void onBuildEnd(Context context) {

		final StackBuild stackBuild = pop();
		
		final MavenBuild build = new MavenBuild(stackBuild.getPlugins());
		
		final StackProject stackProject = get();
	
		stackProject.setBuild(build);
	}

	@Override
	public void onProjectEnd(Context context) {

		final StackProject project = pop();
		
		final MavenProject mavenProject = new MavenProject(
				rootDirectory,
				project.makeModuleId(),
				project.getParentModuleId(),
				project.getPackaging(),
				project.getSubModules(),
				project.getDependencies(),
				project.getBuild());
		
		this.mavenProject = mavenProject;
	}
}
