package com.neaterbits.build.buildsystem.maven.parse;

import com.neaterbits.util.parse.context.Context;

final class StackExecution extends StackBase implements IdSetter {

    private String id;
    
    StackExecution(Context context) {
        super(context);
    }

    String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
