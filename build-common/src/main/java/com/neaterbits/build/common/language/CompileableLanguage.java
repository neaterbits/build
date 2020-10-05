package com.neaterbits.build.common.language;

import java.io.IOException;
import java.util.Set;

import com.neaterbits.build.types.ClassLibs;
import com.neaterbits.build.types.DependencyFile;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.build.types.resource.LibraryResourcePath;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledModuleFileResourcePath;

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
