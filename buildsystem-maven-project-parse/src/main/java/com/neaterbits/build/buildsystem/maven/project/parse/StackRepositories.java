package com.neaterbits.build.buildsystem.maven.project.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.build.buildsystem.maven.project.model.BaseMavenRepository;
import com.neaterbits.util.parse.context.Context;

final class StackRepositories<T extends BaseMavenRepository> extends StackBase {

    private final List<T> repositories;
    
    StackRepositories(Context context) {
        super(context);

        this.repositories = new ArrayList<>();
    }
    
    void add(T baseMavenRepository) {
        
        Objects.requireNonNull(baseMavenRepository);
        
        repositories.add(baseMavenRepository);
    }

    List<T> getRepositories() {
        return repositories;
    }
}
