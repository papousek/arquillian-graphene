/**
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.arquillian.ajocado.drone.factory;

import java.util.List;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jboss.arquillian.core.api.Injector;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.spi.Configurator;
import org.jboss.arquillian.drone.spi.Destructor;
import org.jboss.arquillian.drone.spi.Instantiator;
import org.jboss.arquillian.drone.webdriver.configuration.TypedWebDriverConfiguration;
import org.jboss.arquillian.drone.webdriver.configuration.WebDriverConfiguration;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneRegistrar;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.TestingDriver;
import org.jboss.arquillian.graphene.configuration.GrapheneConfigurator;
import org.jboss.arquillian.graphene.drone.factory.GrapheneWebDriverFactory;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebDriver;

/**
 * @author Lukas Fryc
 */
@RunWith(MockitoJUnitRunner.class)
public class GrapheneWebDriverFactoryTestCase extends AbstractDroneTestCase {

    @Inject
    private Instance<Injector> injector;

    @Mock
    private TypedWebDriverConfiguration<WebDriverConfiguration> configuration;

    GrapheneWebDriverFactory factory = new GrapheneWebDriverFactory();

    TestingFactory testingFactory = new TestingFactory();

    @Mock
    TestingDriver realDriver;

    @Mock(extraInterfaces = GrapheneProxyInstance.class)
    TestingDriver proxyDriver;

    @Override
    protected void addExtensions(List<Class<?>> extensions) {
        super.addExtensions(extensions);
        extensions.add(GrapheneRegistrar.class);
        extensions.add(GrapheneConfigurator.class);
    }

    @Before
    public void init() {
        addAllServices(Instantiator.class, factory, testingFactory);
        getManager().fire(new BeforeSuite());
        getManager().fire(new BeforeClass(GrapheneWebDriverFactoryTestCase.class));
        injector.get().inject(factory);
    }

    @Test
    public void when_instantiator_creates_driver_instance_then_proxy_instance_is_returned_by_factory() {
        // given
        given_instantiator_creates(realDriver);
        // when
        WebDriver createdDriver = factory.createInstance(configuration);
        // then
        assertTrue(createdDriver instanceof GrapheneProxyInstance);
        assertNotSame(createdDriver, realDriver);
    }

    @Test
    public void when_instantiator_creates_driver_instance_then_factory_set_it_to_context() {
        // given
        given_instantiator_creates(realDriver);
        // when
        WebDriver createdDriver = factory.createInstance(configuration);
        createdDriver.getCurrentUrl();
        // then
        try {
            Graphene.context();
        } catch(IllegalStateException e) {
            Assert.fail("The static context is not inititilizied: " + e.getMessage());
        }
        verify(realDriver, only()).getCurrentUrl();
    }

    @Test
    public void when_instantiator_creates_proxy_then_factory_simply_return_it() {
        // given
        given_instantiator_creates(proxyDriver);
        // when
        WebDriver createdDriver = factory.createInstance(configuration);
        // then
        assertTrue(createdDriver instanceof GrapheneProxyInstance);
        assertSame(createdDriver, proxyDriver);
    }

    private void given_instantiator_creates(TestingDriver driver) {
        when(configuration.getImplementationClass()).thenReturn(TestingDriver.class.getName());
        testingFactory.setInstance(driver);
    }

    public class TestingFactory extends TestingDriverFactory implements
            Configurator<TestingDriver, TypedWebDriverConfiguration<TestingDriverConfiguration>>,
            Instantiator<TestingDriver, TypedWebDriverConfiguration<TestingDriverConfiguration>>, Destructor<TestingDriver> {

        private TestingDriver instance;

        public void setInstance(TestingDriver instance) {
            this.instance = instance;
        }

        @Override
        public TestingDriver createInstance(TypedWebDriverConfiguration<TestingDriverConfiguration> configuration) {
            return instance;
        }
    }
}