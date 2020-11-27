package com.neaterbits.build.buildsystem.maven.plugins.instantiate;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.Mojo;

import com.neaterbits.build.buildsystem.maven.model.MavenFileDependency;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInfo;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoDescriptor;

public class MojoFinder {
    
    public static ClassLoader makeClassLoader(MavenPluginInfo pluginInfo) {
        
        final List<URL> urls = new ArrayList<>(pluginInfo.getAllDependencies().size() + 1);
        
        if (pluginInfo.getPluginJarFile() != null) {
            urls.add(makeJarUrl(pluginInfo.getPluginJarFile()));
        }
        
        for (MavenFileDependency fileDependency : pluginInfo.getAllDependencies()) {
            urls.add(makeJarUrl(fileDependency.getJarFile()));
        }
        
        final URLClassLoader classLoader = URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]));
        
        return classLoader;
    }
    
    static class LoadedClasses {
        
        private final Map<String, Class<? extends Mojo>> mojoClasses;
        
        private LoadedClasses(Map<String, Class<? extends Mojo>> mojoClasses) {
            this.mojoClasses = Collections.unmodifiableMap(mojoClasses);
        }

        Map<String, Class<? extends Mojo>> getMojoClasses() {
            return mojoClasses;
        }
    }
    
    static LoadedClasses loadClasses(MavenPluginInfo pluginInfo, ClassLoader classLoader) {

        final Map<String, Class<? extends Mojo>> mojos = new HashMap<>();

        for (MojoDescriptor mojoDescriptor : pluginInfo.getPluginDescriptor().getMojos()) {

            final String className = mojoDescriptor.getImplementation();
            
            try {
                final Class<?> cl = classLoader.loadClass(className);

                if (    Mojo.class.isAssignableFrom(cl)
                        && !cl.isInterface()
                        && (cl.getModifiers() & Modifier.ABSTRACT) ==  0
                        && mojoDescriptor != null) {

                   @SuppressWarnings("unchecked")
                   final Class<? extends Mojo> mojoCl = (Class<? extends Mojo>) cl;
                   
                   mojos.put(className, mojoCl);
                }
                
            } catch (ClassNotFoundException ex) {
                
            }
        }

        return new LoadedClasses(mojos);
    }
    
    private static URL makeJarUrl(File jarFile) {

        final URL url;

        try {
            url = new URL("jar:file:" + jarFile.getAbsolutePath() + "!/");
        } catch (MalformedURLException ex) {
            throw new IllegalStateException(ex);
        }

        return url;
    }
}
