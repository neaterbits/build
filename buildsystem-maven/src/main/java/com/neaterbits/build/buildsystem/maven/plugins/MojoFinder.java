package com.neaterbits.build.buildsystem.maven.plugins;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.maven.plugin.Mojo;

import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.parse.MavenPluginDescriptorParser;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;
import com.neaterbits.build.buildsystem.maven.xml.stream.JavaxXMLStreamReaderFactory;

public class MojoFinder {

    public static MavenDescriptorPluginInfo findMojos(File jarFile) throws IOException {
        
        final JarFile jar = new JarFile(jarFile);

        final Map<String, Class<? extends Mojo>> mojos = new HashMap<>();
        
        final MavenDescriptorPluginInfo pluginInfo;
        
        try {
            
            final JarEntry pluginXMLEntry = jar.getJarEntry("META-INF/maven/plugin.xml");
            
            if (pluginXMLEntry == null) {
                throw new IllegalArgumentException("No plugin.xml");
            }
            
            final MavenPluginDescriptor pluginDescriptor = parsePluginDescriptor(jar, pluginXMLEntry, new JavaxXMLStreamReaderFactory());
            
            if (pluginDescriptor == null) {
                throw new IllegalStateException();
            }
            
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
    
                        final MojoDescriptor mojoDescriptor = pluginDescriptor.getMojos().stream()
                                .filter(mojo -> className.equals(mojo.getImplementation()))
                                .findFirst()
                                .orElse(null);
                        
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
            }

            pluginInfo = new MavenDescriptorPluginInfo(pluginDescriptor, mojos);
        } catch (XMLReaderException ex) {
            throw new IllegalArgumentException("Failed to parse plugin.xml", ex);
        }
        finally {
            jar.close();
        }

        return pluginInfo;
    }

    private static <DOCUMENT> MavenPluginDescriptor parsePluginDescriptor(
            JarFile jar,
            JarEntry jarEntry,
            XMLReaderFactory<DOCUMENT> xmlReaderFactory) throws IOException, XMLReaderException {
        
        final MavenPluginDescriptor mavenPluginDescriptor;
        
        try (InputStream inputStream = jar.getInputStream(jarEntry)) {
            
            mavenPluginDescriptor = MavenPluginDescriptorParser.read(inputStream, xmlReaderFactory, jarEntry.getName());
        }
        
        return mavenPluginDescriptor;
    }
}
