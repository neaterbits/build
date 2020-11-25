package com.neaterbits.build.buildsystem.maven.container;

import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.util.di.Components;

public class PlexusContainerImplTest {

    private static interface TestRole {
        
    }
    
    private static interface OtherRole {
        
    }

    private static class TestComponent implements TestRole {
        
    }
    
    @Test
    public void testContainer() throws ComponentLookupException {
        
        final PlexusContainer container = new PlexusContainerImpl(Components.makeInstance());
        
        final TestComponent comp1 = new TestComponent();
        final TestComponent comp2 = new TestComponent();
        
        container.addComponent(comp1, TestRole.class, "testhint");
        
        assertThat(container.hasComponent(TestRole.class, "testhint")).isTrue();
        assertThat(container.hasComponent(OtherRole.class, "testhint")).isFalse();

        assertThat(container.hasComponent(TestRole.class, "testhint")).isTrue();
        assertThat(container.hasComponent(TestRole.class, "otherhint")).isFalse();
 
        assertThat(container.lookup(TestRole.class, "testhint")).isSameAs(comp1);
        
        assertThat(container.lookupList(TestRole.class).size()).isEqualTo(1);
        assertThat(container.lookupList(TestRole.class).get(0)).isSameAs(comp1);
        
        container.addComponent(comp2, TestRole.class, "otherhint");

        assertThat(container.lookup(TestRole.class, "testhint")).isSameAs(comp1);
        assertThat(container.lookup(TestRole.class, "otherhint")).isSameAs(comp2);

        assertThat(container.lookupList(TestRole.class).size()).isEqualTo(2);
        assertThat(container.lookupList(TestRole.class)).containsExactly(comp1, comp2);
    }
}
