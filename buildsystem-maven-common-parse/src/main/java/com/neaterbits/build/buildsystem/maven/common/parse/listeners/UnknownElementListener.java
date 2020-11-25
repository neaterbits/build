package com.neaterbits.build.buildsystem.maven.common.parse.listeners;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.xml.XMLAttribute;
import com.neaterbits.util.parse.context.Context;

public interface UnknownElementListener {

    void onUnknownTagStart(Context context, String name, List<XMLAttribute> attributes);
    
    void onUnknownTagEnd(Context context, String name);

}
