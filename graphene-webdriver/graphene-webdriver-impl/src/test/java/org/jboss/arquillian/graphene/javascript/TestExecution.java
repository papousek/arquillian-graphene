package org.jboss.arquillian.graphene.javascript;

import org.jboss.arquillian.graphene.DefaultGrapheneContext;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneContext;
import org.jboss.arquillian.graphene.configuration.GrapheneConfiguration;
import org.jboss.arquillian.graphene.TestingDriverStub;
import org.jboss.arquillian.graphene.page.extension.PageExtensionRegistryImpl;
import org.jboss.arquillian.graphene.page.extension.RemotePageExtensionInstallatorProvider;
import org.junit.Test;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestExecution extends AbstractJavaScriptTest {


    @JavaScript
    public static interface TestingInterface {
        public void method();

        @MethodName("anotherMethodName")
        public void namedMethod();
    }

    @Test
    public void test_execution() {

        // given
        TestingDriverStub executor = spy(new TestingDriverStub());
        when(executor.executeScript("return true;")).thenReturn(true);

        // when
        DefaultGrapheneContext.init(executor);
        TestingInterface instance = JSInterfaceFactory.create(Graphene.context(), TestingInterface.class);
        instance.method();

        // then
        verify(executor, times(1)).executeScript(invocation("TestingInterface", "method"));
    }

    @Test
    public void test_execution_with_named_method() {

        // given
        TestingDriverStub executor = spy(new TestingDriverStub());
        when(executor.executeScript("return true;")).thenReturn(true);

        // when
        DefaultGrapheneContext.init(executor);
        TestingInterface instance = JSInterfaceFactory.create(Graphene.context(), TestingInterface.class);
        instance.namedMethod();

        // then
        verify(executor, times(1)).executeScript(invocation("TestingInterface", "anotherMethodName"));
    }

    @JavaScript("base")
    public static interface TestingInterfaceWithBase {
        public void method();
    }

    @Test
    public void test_execution_with_base() {

        // given
        TestingDriverStub executor = spy(new TestingDriverStub());
        when(executor.executeScript("return true;")).thenReturn(true);

        // when
        DefaultGrapheneContext.init(executor);
        TestingInterfaceWithBase instance = JSInterfaceFactory.create(Graphene.context(), TestingInterfaceWithBase.class);
        instance.method();

        // then
        verify(executor, times(1)).executeScript(invocation("base", "method"));
    }
}
