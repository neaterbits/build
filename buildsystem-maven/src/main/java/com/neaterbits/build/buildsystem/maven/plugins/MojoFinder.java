package com.neaterbits.build.buildsystem.maven.plugins;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.maven.plugin.Mojo;

import com.neaterbits.build.buildsystem.maven.model.MavenFileDependency;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.parse.MavenPluginDescriptorParser;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;
import com.neaterbits.build.buildsystem.maven.xml.stream.JavaxXMLStreamReaderFactory;

public class MojoFinder {

    public static MavenPluginDescriptor getPluginDescriptor(File jarFile) throws IOException {
        
        final MavenPluginDescriptor pluginDescriptor;
        
        final JarFile jar = new JarFile(jarFile);

        try {
            final JarEntry pluginXMLEntry = jar.getJarEntry("META-INF/maven/plugin.xml");
            
            if (pluginXMLEntry == null) {
                throw new IllegalArgumentException("No plugin.xml");
            }
            
            pluginDescriptor = parsePluginDescriptor(jar, pluginXMLEntry, new JavaxXMLStreamReaderFactory());
            
            if (pluginDescriptor == null) {
                throw new IllegalStateException();
            }
        } catch (XMLReaderException ex) {
            throw new IllegalArgumentException("Failed to parse plugin.xml", ex);
        }
        finally {
            jar.close();
        }

        return pluginDescriptor;
    }
    
    static Map<String, Class<? extends Mojo>> loadClasses(MavenPluginInfo pluginInfo) {

        final Map<String, Class<? extends Mojo>> mojos = new HashMap<>();

        final List<URL> urls = new ArrayList<>(pluginInfo.getAllDependencies().size() + 1);
        
        urls.add(makeJarUrl(pluginInfo.getPluginJarFile()));
        
        for (MavenFileDependency fileDependency : pluginInfo.getAllDependencies()) {
            urls.add(makeJarUrl(fileDependency.getJarFile()));
        }
        
        final URLClassLoader classLoader = URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]));

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

        return mojos;
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
