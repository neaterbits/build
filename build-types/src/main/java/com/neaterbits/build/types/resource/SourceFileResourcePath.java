package com.neaterbits.build.types.resource;

import java.io.File;

public final class SourceFileResourcePath extends SourcePath {

	public SourceFileResourcePath(SourceFileHolderResourcePath sourceHolderPath, SourceFileResource sourceFileResource) {
		super(sourceHolderPath, sourceFileResource);
	}
	
	@Override
	public ResourcePath getParentPath() {
		return makeSourceFileHolderResourcePath();
	}
	
	public NamespaceResourcePath getNamespacePath() {

		final Resource resource = getFromLast(1);
		
		final NamespaceResourcePath resourcePath;
		
		if (resource instanceof NamespaceResource) {

			final SourceFolderResourcePath sourceFolderResourcePath = makeSourceFolderResourcePath(2);

			resourcePath = new NamespaceResourcePath(sourceFolderResourcePath, (NamespaceResource)resource);
		}
		else {
			resourcePath = null;
		}
		
		return resourcePath;
	}
	
	public final SourceFolderResourcePath getSourceFolderPath() {
		
		final SourceFolderResourcePath resourcePath;
		
		if (getFromLast(1) instanceof SourceFolderResource) {
			resourcePath = makeSourceFolderResourcePath(1);
		}
		else if (getFromLast(2) instanceof SourceFolderResource) {
			resourcePath = makeSourceFolderResourcePath(2);
		}
		else {
			throw new UnsupportedOperationException();
		}
		
		return resourcePath;
	}

	public File getSourceFolder() {

		for (int i = 1; i < length(); ++ i) {
			final Resource resource = getFromLast(i);
			
			if (resource instanceof SourceFolderResource) {
				return ((SourceFolderResource)resource).getFile();
			}
		}

		return null;
	}

	public String getName() {
		return getLast().getName();
	}
}
