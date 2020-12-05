package com.neaterbits.build.buildsystem.maven.plexus.components.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.common.parse.listeners.BaseXMLEventListener;
import com.neaterbits.build.buildsystem.maven.xml.XMLAttribute;
import com.neaterbits.util.parse.context.Context;

final class PlexusComponentDescriptorXMLEventListener extends BaseXMLEventListener<Void> {
    
    private final PlexusComponentDescriptorEventListener delegate;
    
    public PlexusComponentDescriptorXMLEventListener(PlexusComponentDescriptorEventListener delegate) {
        super(delegate);
        
        this.delegate = delegate;
    }

    @Override
    protected boolean withinUserUnknownTag() {
        return false;
    }

    @Override
    protected boolean allowTextForUnknownTag() {
        return withinUserUnknownTag();
    }

    @Override
    public void onStartElement(Context context, String localPart, List<XMLAttribute> attributes, Void param) {

        switch (localPart) {
        
        case "component-set":
            delegate.onComponentSetStart(context);
            break;
            
        case "components":
            delegate.onComponentsStart(context);
            break;
            
        case "component":
            delegate.onComponentStart(context);
            break;
            
        case "role":
            delegate.onRoleStart(context);
            break;
            
        case "role-hint":
            delegate.onRoleHintStart(context);
            break;
            
        case "implementation":
            delegate.onImplementationStart(context);
            break;
        
        case "instantiation-strategy":
            delegate.onInstantiationStrategyStart(context);
            break;
            
        case "requirements":
            delegate.onRequirementsStart(context);
            break;
            
        case "requirement":
            delegate.onRequirementStart(context);
            break;
            
        case "field-name":
            delegate.onFieldNameStart(context);
            break;
        
        case "field":
            delegate.onFieldStart(context);
            break;

        default:
            super.onStartElement(context, localPart, attributes, param);
            break;
        }
    }

    @Override
    public void onEndElement(Context context, String localPart, Void param) {

        switch (localPart) {
        
        case "component-set":
            delegate.onComponentSetEnd(context);
            break;
            
        case "components":
            delegate.onComponentsEnd(context);
            break;
            
        case "component":
            delegate.onComponentEnd(context);
            break;
            
        case "role":
            delegate.onRoleEnd(context);
            break;
            
        case "role-hint":
            delegate.onRoleHintEnd(context);
            break;
            
        case "implementation":
            delegate.onImplementationEnd(context);
            break;
            
        case "instantiation-strategy":
            delegate.onInstantiationStrategyEnd(context);
            break;

        case "requirements":
            delegate.onRequirementsEnd(context);
            break;
            
        case "requirement":
            delegate.onRequirementEnd(context);
            break;
            
        case "field-name":
            delegate.onFieldNameEnd(context);
            break;

        case "field":
            delegate.onFieldEnd(context);
            break;

        default:
            super.onEndElement(context, localPart, param);
            break;
        }
    }
}
