package com.neaterbits.build.buildsystem.common.parse;

import com.neaterbits.util.parse.context.Context;

public abstract class StackBoolean extends StackText {

    protected StackBoolean(Context context) {
        super(context);
    }
    
    public final Boolean getValue() {
        
        final Boolean value;
        
        switch (getText().toLowerCase()) {
        
        case "true":
            value = Boolean.TRUE;
            break;
            
        case "false":
            value = Boolean.FALSE;
            break;
        
        default:
            value = null;
            break;
        }

        return value;
    }
}
