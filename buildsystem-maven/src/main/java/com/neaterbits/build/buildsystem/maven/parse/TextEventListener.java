package com.neaterbits.build.buildsystem.maven.parse;

import com.neaterbits.util.parse.context.Context;

public interface TextEventListener {

    void onText(Context context, String text);

}
