package com.neaterbits.build.buildsystem.maven.common.parse.listeners;

import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.xml.XMLAttribute;
import com.neaterbits.build.buildsystem.maven.xml.XMLEventListener;
import com.neaterbits.util.parse.context.Context;

public abstract class BaseXMLEventListener<PARAM>
                            implements XMLEventListener<PARAM> {

    private final BaseEventListener delegate;

    protected abstract boolean allowTextForUnknownTag();

    protected abstract boolean withinUserUnknownTag();

    protected BaseXMLEventListener(BaseEventListener delegate) {
        
        Objects.requireNonNull(delegate);

        this.delegate = delegate;
    }

    private int unknownTag;
    
    protected final boolean withinUnknownTag() {
        return unknownTag > 0;
    }

    @Override
    public final void onStartDocument(PARAM param) {

        this.unknownTag = 0;
    }
    
    @Override
    public void onStartElement(Context context, String localPart, List<XMLAttribute> attributes, PARAM param) {
        
        delegate.onUnknownTagStart(context, localPart, attributes);
        
        ++ unknownTag;
    }

    @Override
    public void onEndElement(Context context, String localPart, PARAM param) {

        if (unknownTag == 0 && !withinUserUnknownTag()) {
            throw new IllegalStateException();
        }
    
        delegate.onUnknownTagEnd(context, localPart);
        
        -- unknownTag;
    }
    
    @Override
    public final void onText(Context context, String data, PARAM param) {

        if (unknownTag == 0 || allowTextForUnknownTag()) {
            delegate.onText(context, data);
        }
    }

    @Override
    public final void onEndDocument(PARAM param) {

        if (unknownTag != 0) {
            throw new IllegalStateException();
        }
    }
}
