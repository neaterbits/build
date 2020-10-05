package com.neaterbits.build.types.resource;

public class SourceLineResourcePath extends SourcePath {

	private final int lineNo;
	
	public SourceLineResourcePath(SourceFileResourcePath sourceFileResourcePath, int lineNo) {
		super(sourceFileResourcePath);

		this.lineNo = lineNo;
	}

	public int getLineNo() {
		return lineNo;
	}

	@Override
	public ResourcePath getParentPath() {
		return new SourceFileResourcePath(makeSourceFileHolderResourcePath(), (SourceFileResource)getLast());
	}
}
