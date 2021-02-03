package com.neaterbits.build.language.java.jdk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.neaterbits.build.model.runtimeenvironment.RuntimeEnvironment;
import com.neaterbits.build.types.ClassLibs;
import com.neaterbits.build.types.DependencyFile;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.build.types.resource.FileSystemResourcePath;
import com.neaterbits.build.types.resource.LibraryResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.compiler.bytecode.common.BytecodeFormat;
import com.neaterbits.compiler.java.bytecode.JavaBytecodeFormat;
import com.neaterbits.compiler.java.bytecode.JavaClassLibs;

public class JavaRuntimeEnvironment implements RuntimeEnvironment {
    
    private static final JavaBytecodeFormat JAVA_BYTECODE_FORMAT = new JavaBytecodeFormat();

    private final Path jreRootPath;
    
    public JavaRuntimeEnvironment() {
        this(Path.of(System.getProperty("java.home")));
    }
    
    public JavaRuntimeEnvironment(Path jreRootPath) {
        this.jreRootPath = jreRootPath;
    }

    @Override
    public ClassLibs getSystemLibraries() {
        
        final List<String> list;
        
        if (Files.exists(jreRootPath.resolve("jmods"))) {
            
            final List<String> fileNames = Arrays.asList("java.base.jmod");

            list = fileNames.stream()
                    .map(fileName -> jreRootPath.resolve("jmods").resolve(fileName).toString())
                    .collect(Collectors.toList());
        }
        else {
        
            final List<String> fileNames = Arrays.asList("rt.jar", "charsets.jar");
        
            list = fileNames.stream()
                    .map(fileName -> jreRootPath.resolve("lib").resolve(fileName).toString())
                    .collect(Collectors.toList());
        }
        
        try {
            return new JavaClassLibs(list);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public String[] getCommandLineForRunning(
            List<CompiledModuleFileResourcePath> projects,
            List<LibraryResourcePath> libraries,
            TypeName entryPointType,
            String [] programArguments,
            String [] vmArguments) {
        
        Objects.requireNonNull(projects);
        Objects.requireNonNull(entryPointType);

        final List<String> arguments = new ArrayList<>();

        arguments.add("java");

        final List<File> files = new ArrayList<>(projects.size() + (libraries != null ? libraries.size() : 0));
        
        for (CompiledModuleFileResourcePath project : projects) {
            files.add(project.getFile());
        }
        
        if (libraries != null) {
            for (LibraryResourcePath library : libraries) {
                files.add(library.getFile());
            }
        }
        
        if (vmArguments != null) {
            for (String vmArgument : vmArguments) {
                arguments.add(vmArgument);
            }
        }

        ClassPathHelper.addClassPathOption(arguments, files);
        
        arguments.add(entryPointType.join('.'));
        
        if (programArguments != null) {
            for (String programArgument : programArguments) {
                arguments.add(programArgument);
            }
        }
        
        return arguments.toArray(new String[arguments.size()]);
    }


    @Override
    public final BytecodeFormat getBytecodeFormat() {
        return JAVA_BYTECODE_FORMAT;
    }

    @Override
    public final Set<TypeName> getTypesFromCompiledModuleFile(CompiledModuleFileResourcePath compiledModuleFileResourcePath) throws IOException {
        return getTypesFromJarFile(compiledModuleFileResourcePath);
    }
    
    @Override
    public final Set<TypeName> getTypesFromLibraryFile(LibraryResourcePath libraryResourcePath) throws IOException {
        return getTypesFromJarFile(libraryResourcePath);
    }
    
    @Override
    public final Set<TypeName> getTypesFromSystemLibraryFile(DependencyFile systemLibraryPath) throws IOException {

        return getTypesFromJarFile(systemLibraryPath.getFile());
    }

    private final Set<TypeName> getTypesFromJarFile(FileSystemResourcePath jarFileResourcePath) throws IOException {
        return getTypesFromJarFile(jarFileResourcePath.getFile());
    }

    @Override
    public final boolean canReadCodeMapFromCompiledCode() {
        return true;
    }

    private Set<TypeName> getTypesFromJarFile(File file) throws IOException {

        return JAVA_BYTECODE_FORMAT.getTypesFromLibraryFile(file);
    }
}
