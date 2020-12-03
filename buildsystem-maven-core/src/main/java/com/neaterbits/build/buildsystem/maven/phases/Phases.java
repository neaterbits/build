package com.neaterbits.build.buildsystem.maven.phases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.phases.Phase.PrevPhase;
import com.neaterbits.util.concurrency.scheduling.Constraint;

public final class Phases {

    public static final Phase VALIDATE = new Phase("validate", PrevPhase.ALL, Constraint.CPU);

    private static final Phases CLEAN_PHASES;
    
    private static final Phases BUILD_PHASES;
    
    private static final List<Phases> LIFECYCLES;
    
    static {
        
        final List<Phase> cleanPhases = new ArrayList<>();
        
        cleanPhases.add(new Phase("pre-clean",  PrevPhase.DEPS, Constraint.IO));
        cleanPhases.add(new Phase("clean",      PrevPhase.DEPS, Constraint.IO, "clean", "clean"));
        cleanPhases.add(new Phase("post-clean", PrevPhase.DEPS, Constraint.IO));
        
        CLEAN_PHASES = new Phases(cleanPhases);

        final List<Phase> buildPhases = new ArrayList<>();
        
        buildPhases.add(VALIDATE);
        buildPhases.add(new Phase("initialize", PrevPhase.ALL, Constraint.IO));
        
        buildPhases.add(new Phase("generate-sources",   PrevPhase.DEPS, Constraint.IO));
        buildPhases.add(new Phase("process-resources",  PrevPhase.DEPS, Constraint.IO, "resources", "resources"));
        buildPhases.add(new Phase("compile",            PrevPhase.DEPS, Constraint.CPU, "compiler", "compile"));
        buildPhases.add(new Phase("process-classes",    PrevPhase.DEPS, Constraint.CPU));
        
        buildPhases.add(new Phase("generate-test-sources",   PrevPhase.DEPS, Constraint.IO));
        buildPhases.add(new Phase("process-test-sources",    PrevPhase.DEPS, Constraint.IO));
        buildPhases.add(new Phase("generate-test-resources", PrevPhase.DEPS, Constraint.IO));
        buildPhases.add(new Phase("process-test-resources",  PrevPhase.DEPS, Constraint.IO, "resources", "testResources"));
        buildPhases.add(new Phase("test-compile",         PrevPhase.DEPS, Constraint.CPU, "compiler", "testCompile"));
        buildPhases.add(new Phase("process-test-classes", PrevPhase.DEPS, Constraint.IO));
        buildPhases.add(new Phase("test",                 PrevPhase.DEPS, Constraint.CPU, "surefire", "test"));
        
        buildPhases.add(new Phase("prepare-package",      PrevPhase.DEPS, Constraint.IO));
        buildPhases.add(new Phase("package",              PrevPhase.DEPS, Constraint.IO, "jar", "jar"));
        
        buildPhases.add(new Phase("pre-integration-test", PrevPhase.DEPS, Constraint.IO));
        buildPhases.add(new Phase("integration-test",     PrevPhase.DEPS, Constraint.CPU));
        buildPhases.add(new Phase("post-integration-test", PrevPhase.DEPS, Constraint.IO));
        
        buildPhases.add(new Phase("verify",     PrevPhase.DEPS, Constraint.IO));
        buildPhases.add(new Phase("install",    PrevPhase.DEPS, Constraint.IO, "install", "install"));
        buildPhases.add(new Phase("deploy",     PrevPhase.ALL,  Constraint.IO, "deploy", "deploy"));
        
        BUILD_PHASES = new Phases(buildPhases);
        
        LIFECYCLES = Collections.unmodifiableList(Arrays.asList(CLEAN_PHASES, BUILD_PHASES));
    }
    
    private final List<Phase> phases;
    
    private Phases(List<Phase> phases) {
        this.phases = Collections.unmodifiableList(phases);
    }

    private Phase findPhase(String phaseName) {
        
        Objects.requireNonNull(phaseName);
        
        final Phase phase = phases.stream()
            .filter(p -> p.getName().equals(phaseName))
            .findFirst()
            .orElse(null);
    
        return phase;
    }
    
    private boolean hasPhase(String phaseName) {

        return findPhase(phaseName) != null;
    }
    
    private static Phases findPhases(String phaseName) {
        
        return LIFECYCLES.stream()
                .filter(phases -> phases.hasPhase(phaseName))
                .findFirst()
                .orElse(null);
    }
    
    public List<Phase> getAll() {
        return phases;
    }

    public static Phases getApplicablePhases(String phaseName) throws UnknownPhaseException {
        
        final Phases phases = findPhases(phaseName);
        
        if (phases == null) {
            throw new UnknownPhaseException("No such phase '" + phaseName + "'", phaseName);
        }

        final List<Phase> applicablePhases = getApplicablePhases(phases.getAll(), phases.findPhase(phaseName));
        
        return new Phases(applicablePhases);
    }

    private static List<Phase> getApplicablePhases(List<Phase> allPhases, Phase phaseToBuild) {

        Objects.requireNonNull(allPhases);
        Objects.requireNonNull(phaseToBuild);

        final List<Phase> phases = new ArrayList<>(allPhases.size());

        int lastIndex;
        
        for (lastIndex = 0; lastIndex < allPhases.size(); ++ lastIndex) {

            final Phase phase = allPhases.get(lastIndex);

            phases.add(phase);
            
            if (allPhases.get(lastIndex).getName().equals(phaseToBuild.getName())) {
                break;
            }
        }

        return phases;
    }
}
