package com.neaterbits.build.buildsystem.maven.project.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.build.buildsystem.maven.project.model.MavenNotifier;
import com.neaterbits.util.parse.context.Context;

final class StackNotifiers extends StackBase {

    private final List<MavenNotifier> notifiers;
    
    StackNotifiers(Context context) {
        super(context);
        
        this.notifiers = new ArrayList<>();
    }

    void add(MavenNotifier notifier) {
        
        Objects.requireNonNull(notifier);
        
        notifiers.add(notifier);
    }
    
    List<MavenNotifier> getNotifiers() {
        return notifiers;
    }
}
