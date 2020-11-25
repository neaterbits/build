package com.neaterbits.build.buildsystem.maven.di.componentsource.mavenplugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.parse.MavenPluginDescriptorParser;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;
import com.neaterbits.build.buildsystem.maven.xml.stream.JavaxXMLStreamReaderFactory;
import com.neaterbits.util.di.componentsource.BaseComponentsSourceLoader;
import com.neaterbits.util.di.componentsource.ComponentsSource;
import com.neaterbits.util.di.componentsource.ComponentsSourceException;

public final class MavenPluginComponentsSourceLoader
        extends BaseComponentsSourceLoader<
                            MojoDescriptor,
                            MavenPluginDescriptor,
                            MavenPluginComponentsSource> {

    private static final XMLReaderFactory<?> XML_READER_FACTORY = new JavaxXMLStreamReaderFactory();

    @Override
    public List<? extends ComponentsSource<MojoDescriptor>> load(URLClassLoader classLoader)
            throws IOException, ComponentsSourceException {

        return processAll(
                classLoader,
                "META-INF/maven/plugin.xml",
                MavenPluginComponentsSourceLoader::readPluginDescriptor,
                MavenPluginComponentsSource::new);
    }

    private static MavenPluginDescriptor readPluginDescriptor(URL url, InputStream inputStream) throws IOException, ComponentsSourceException {
        
        final MavenPluginDescriptor pluginDescriptor;

        try {
            pluginDescriptor = MavenPluginDescriptorParser.read(
                                                    inputStream,
                                                    XML_READER_FACTORY,
                                                    url.getFile());
        } catch (XMLReaderException ex) {
            throw new ComponentsSourceException("Exception while parsing XML for " + url, ex);
        }

        return pluginDescriptor;
    }
}
