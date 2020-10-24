package com.neaterbits.build.buildsystem.maven.plugins;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.maven.plugin.Mojo;

import com.neaterbits.build.buildsystem.maven.plugins.model.MojoModel;

public class MojoFinder {

    public static List<MojoModel> findMojos(File jarFile) throws IOException {
        
        final JarFile jar = new JarFile(jarFile);
        final List<MojoModel> mojos = new ArrayList<>();

        try {
            
            final Enumeration<JarEntry> entries = jar.entries();
            
            final URL url;
    
            try {
                url = new URL("jar:file:" + jarFile.getAbsolutePath() + "!/");
            } catch (MalformedURLException ex) {
                throw new IllegalStateException(ex);
            }
            
            final URLClassLoader classLoader = URLClassLoader.newInstance(new URL [] { url });
            
            while (entries.hasMoreElements()) {
                
                final JarEntry jarEntry = entries.nextElement();
                
                final String classSuffix = ".class";
                
                if (jarEntry.isDirectory()) {
                    continue;
                }
                
                final String fileName = jarEntry.getName();
                
                if (fileName.endsWith(classSuffix)) {
                    
                    final String classPath = fileName.substring(0, fileName.length() - classSuffix.length());
                    
                    final String className = classPath.replace('/', '.');
                    
                    try {
                        final Class<?> cl = classLoader.loadClass(className);
    
                        if (    Mojo.class.isAssignableFrom(cl)
                             && !cl.isInterface()
                             && (cl.getModifiers() & Modifier.ABSTRACT) ==  0) {
    
                            @SuppressWarnings("unchecked")
                            final Class<? extends Mojo> mojoCl = (Class<? extends Mojo>) cl;
                            
                            mojos.add(new MojoModel(mojoCl));
                            
                        }
                    } catch (ClassNotFoundException ex) {
                    }
                }
            }
        }
        finally {
            jar.close();
        }

        return mojos;
    }
}
