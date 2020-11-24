package com.neaterbits.build.buildsystem.common.parse;

import com.neaterbits.util.parse.context.Context;

public interface TextEventListener {

    void onText(Context context, String text);

}
