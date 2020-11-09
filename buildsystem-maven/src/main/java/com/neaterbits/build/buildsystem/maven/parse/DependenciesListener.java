package com.neaterbits.build.buildsystem.maven.parse;

import com.neaterbits.util.parse.context.Context;

public interface DependenciesListener {

    void onDependenciesStart(Context context);

    void onDependencyStart(Context context);

    void onScopeStart(Context context);

    void onScopeEnd(Context context);

    void onOptionalStart(Context context);

    void onOptionalEnd(Context context);

    void onDependencyEnd(Context context);

    void onDependenciesEnd(Context context);

}
