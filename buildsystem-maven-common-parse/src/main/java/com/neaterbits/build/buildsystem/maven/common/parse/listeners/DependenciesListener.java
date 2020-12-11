package com.neaterbits.build.buildsystem.maven.common.parse.listeners;

import com.neaterbits.util.parse.context.Context;

public interface DependenciesListener {

    void onDependenciesStart(Context context);

    void onDependencyStart(Context context);

    void onClassifierStart(Context context);

    void onClassifierEnd(Context context);

    void onScopeStart(Context context);

    void onScopeEnd(Context context);

    void onOptionalStart(Context context);

    void onOptionalEnd(Context context);
    
    void onExclusionsStart(Context context);
    
    void onExclusionStart(Context context);

    void onExclusionEnd(Context context);

    void onExclusionsEnd(Context context);

    void onDependencyEnd(Context context);

    void onDependenciesEnd(Context context);

}
