package com.neaterbits.build.buildsystem.maven.common.parse.configuration;

import java.util.List;

import com.neaterbits.build.buildsystem.common.parse.BaseStackEventListener;
import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.build.buildsystem.maven.common.parse.listeners.UnknownElementListener;
import com.neaterbits.build.buildsystem.maven.xml.XMLAttribute;
import com.neaterbits.util.parse.context.Context;

public abstract class BasePlexusConfigurationStackEventListener
        extends BaseStackEventListener
        implements UnknownElementListener, PlexusConfigurationListener {

    @Override
    public void onConfigurationStart(Context context) {
        push(new StackPlexusConfigurationMap(context));
    }

    @Override
    public void onConfigurationEnd(Context context) {
        
        final StackBase cur = pop();
        
        if (cur instanceof StackPlexusConfigurationMap) {
            
            final StackPlexusConfigurationMap stackPluginConfiguration = (StackPlexusConfigurationMap)cur;
            
            final ConfigurationSetter configurationSetter = get();
            
            configurationSetter.setConfiguration(stackPluginConfiguration.getConfiguration());
        }
        else {
            throw new IllegalStateException();
        }
    }

    @Override
    public void onUnknownTagStart(Context context, String name, List<XMLAttribute> attributes) {

        final Object cur = get();
        
        if (cur instanceof StackPlexusConfigurationMap || cur instanceof StackConfigurationLevel) {
            push(new StackConfigurationLevel(context, name));
        }
    }

    @Override
    public void onUnknownTagEnd(Context context, String name) {
        
        final Object cur = get();
        
        if (cur instanceof StackConfigurationLevel) {
         
            final StackConfigurationLevel stackConfigurationLevel = pop();
            
            final StackBase last = get();
            
            if (last instanceof StackConfigurationLevel) {
                
                final StackConfigurationLevel lastConfigurationLevel = (StackConfigurationLevel)last;
                
                if (stackConfigurationLevel.getText() != null) {
                    lastConfigurationLevel.add(stackConfigurationLevel.getTagName(), stackConfigurationLevel.getText());
                }
                else {
                    lastConfigurationLevel.add(stackConfigurationLevel.getTagName(), stackConfigurationLevel.getObject());
                }
            }
            else {
                throw new IllegalStateException();
            }
        }
    }
}
