package com.neaterbits.build.buildsystem.maven.plugins.instantiate;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.plugin.Mojo;

import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInfo;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoDescriptor;

public class MojoFinder {
    
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
}
