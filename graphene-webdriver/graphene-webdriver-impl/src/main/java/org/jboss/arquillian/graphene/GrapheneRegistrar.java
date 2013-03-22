package org.jboss.arquillian.graphene;

import java.util.Collection;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.drone.impl.DroneContext;
import org.jboss.arquillian.graphene.configuration.GrapheneConfiguration;
import org.jboss.arquillian.graphene.page.extension.PageExtensionRegistry;
import org.jboss.arquillian.graphene.spi.page.PageExtensionProvider;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.test.spi.annotation.ClassScoped;
import org.jboss.arquillian.test.spi.annotation.SuiteScoped;
import org.jboss.arquillian.test.spi.event.suite.AfterClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;

public class GrapheneRegistrar {

    @ClassScoped
    @Inject
    private InstanceProducer<GrapheneContext> grapheneContext;
    @SuiteScoped
    @Inject
    private InstanceProducer<WebDriverProxyFactory> webDriverProxyFactory;

    @Inject
    private Instance<ServiceLoader> serviceLoader;
    @Inject
    private Instance<DroneContext> droneContext;

    public void registerWebDriverProxyFactory(@Observes(precedence = 50) BeforeClass event) {
        webDriverProxyFactory.set(new WebDriverProxyFactoryImpl());
    }

    public void registerContext(@Observes(precedence = 50) BeforeClass event, final GrapheneConfiguration configuration) {
        grapheneContext.set(new GrapheneContextWrapper(configuration, droneContext, Default.class));
        Graphene.setContext(grapheneContext.get());
    }

    public void unregisterContext(@Observes(precedence = -50) AfterClass event) {
        grapheneContext.get().getPageExtensionRegistry().flush();
        Graphene.reset();
    }

    protected void loadPageExtensions(PageExtensionRegistry registry, TestClass testClass) {
        Collection<PageExtensionProvider> providers = serviceLoader.get().all(PageExtensionProvider.class);
        for (PageExtensionProvider provider: providers) {
            registry.register(provider.getPageExtensions(testClass));
        }
    }

}
