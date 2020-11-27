package com.neaterbits.build.buildsystem.maven.phases;

import java.util.Objects;

import com.neaterbits.util.concurrency.scheduling.Constraint;

public final class Phase {

    public enum PrevPhase {
        ALL,
        DEPS
    }
    
    /*
    public enum Deps {
        MODULE_DEPENDENT,   // Can not be run in parallel with prior phases for dependent modules, but independent modules can
        MODULE_INDEPENDENT, // Can be run in parallel with prior phases for dependent modules
        PHASE               // prior phase must complete for whole build
    }
    
    public enum PrevPhase {
        
        NONE, // Can build irregardless of prior phase complete
        
        COMPLETE_DEPS, // Complete previous phases for dependencies of a module
        
        COMPLETE_MODULE, // Complete previous phases for this module (and dependencies)
        
        PHASE // Complete prior phase for all modules before building this phase 
        
    }
    
    public enum ThisPhase {
        NONE, // Does not need to take dependencies into consideration in same phase

        DEPENDENCIES // Must build dependencies first
    };
    */
    
    private final String name;
    private final PrevPhase prevPhase;
    private final Constraint constraint;
    private final String plugin;
    private final String goal;

    Phase(String name, PrevPhase prevPhase, Constraint constraint) {

        Objects.requireNonNull(name);
        Objects.requireNonNull(constraint);
        Objects.requireNonNull(prevPhase);
        
        this.name = name;
        this.prevPhase = prevPhase;
        this.constraint = constraint;
        this.plugin = null;
        this.goal = null;
    }

    Phase(String name, PrevPhase prevPhase, Constraint constraint, String plugin, String goal) {
    
        Objects.requireNonNull(name);
        Objects.requireNonNull(prevPhase);
        Objects.requireNonNull(constraint);
        Objects.requireNonNull(plugin);
        Objects.requireNonNull(goal);
        
        this.name = name;
        this.prevPhase = prevPhase;
        this.constraint = constraint;
        this.plugin = plugin;
        this.goal = goal;
    }

    public String getName() {
        return name;
    }

    public PrevPhase getPrevPhase() {
        return prevPhase;
    }

    public Constraint getConstraint() {
        return constraint;
    }

    public String getPlugin() {
        return plugin;
    }

    public String getGoal() {
        return goal;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Phase other = (Phase) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Phase [name=" + name + ", prevPhase=" + prevPhase + ", constraint=" + constraint + ", plugin=" + plugin
                + ", goal=" + goal + "]";
    }
}
