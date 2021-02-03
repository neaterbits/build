package com.neaterbits.build.model.runtimeenvironment;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.neaterbits.build.types.ClassLibs;
import com.neaterbits.build.types.DependencyFile;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.build.types.resource.LibraryResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.compiler.bytecode.common.BytecodeFormat;

public interface RuntimeEnvironment {

    ClassLibs getSystemLibraries();
    
    BytecodeFormat getBytecodeFormat();

    Set<TypeName> getTypesFromCompiledModuleFile(CompiledModuleFileResourcePath compiledModuleFileResourcePath) throws IOException;

    Set<TypeName> getTypesFromLibraryFile(LibraryResourcePath libraryResourcePath) throws IOException;

    Set<TypeName> getTypesFromSystemLibraryFile(DependencyFile systemLibraryPath) throws IOException;

    boolean canReadCodeMapFromCompiledCode();
    
    String[] getCommandLineForRunning(
            List<CompiledModuleFileResourcePath> projects,
            List<LibraryResourcePath> libraries,
            TypeName entryPointType,
            String [] programArguments,
            String [] vmArguments);

}
