package org.jboss.arquillian.graphene.javascript;

import org.jboss.arquillian.graphene.DefaultGrapheneContext;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.TestingDriverStub;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mockito.Spy;

public class TestInstantiation {

    @Spy
    TestingDriverStub executor = new TestingDriverStub();

    @JavaScript
    public static interface TestingInterface {
    }

    @Test
    public void factory_should_create_valid_instance_of_given_interface() {
        DefaultGrapheneContext.init(executor);
        TestingInterface instance = JSInterfaceFactory.create(Graphene.context(), TestingInterface.class);
        assertTrue("instance should implement the provided interface", instance instanceof TestingInterface);
    }

    @JavaScript
    public static class InvalidClass {
    }

    @Test(expected = IllegalArgumentException.class)
    public void factory_should_fail_when_class_provided() {
        DefaultGrapheneContext.init(executor);
        InvalidClass instance = JSInterfaceFactory.create(Graphene.context(), InvalidClass.class);
    }
}
