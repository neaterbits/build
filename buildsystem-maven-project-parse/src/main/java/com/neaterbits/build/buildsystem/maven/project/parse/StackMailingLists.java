package com.neaterbits.build.buildsystem.maven.project.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.build.buildsystem.maven.project.model.MavenMailingList;
import com.neaterbits.util.parse.context.Context;

final class StackMailingLists extends StackBase {

    private final List<MavenMailingList> mailingLists;

    StackMailingLists(Context context) {
        super(context);

        this.mailingLists = new ArrayList<>();
    }

    void add(MavenMailingList mailingList) {

        Objects.requireNonNull(mailingList);

        mailingLists.add(mailingList);
    }
    
    List<MavenMailingList> getMailingLists() {
        return mailingLists;
    }
}
