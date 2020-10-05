package com.neaterbits.ide.common.language;

import java.io.IOException;
import java.util.Set;

import com.neaterbits.compiler.bytecode.common.ClassLibs;
import com.neaterbits.compiler.bytecode.common.DependencyFile;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.ide.common.resource.LibraryResourcePath;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResourcePath;

public interface CompileableLanguage extends BuildableLanguage {
	
	TypeName getTypeName(SourceFileResourcePath sourceFile);
	
	ClassLibs getSystemLibraries();
	
	TypeName getTypeName(String namespace, String name);

	String getNamespaceString(TypeName typeName);
	
	String getCompleteNameString(TypeName typeName);
	
	String getBinaryName(TypeName typeName);
	
	Set<TypeName> getTypesFromCompiledModuleFile(CompiledModuleFileResourcePath compiledModuleFileResourcePath) throws IOException;

	Set<TypeName> getTypesFromLibraryFile(LibraryResourcePath libraryResourcePath) throws IOException;

	Set<TypeName> getTypesFromSystemLibraryFile(DependencyFile systemLibraryPath) throws IOException;
	
	boolean canReadCodeMapFromCompiledCode();
	
}
