package org.jboss.arquillian.graphene;

import java.lang.annotation.Annotation;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.drone.impl.DroneContext;
import org.jboss.arquillian.graphene.configuration.GrapheneConfiguration;
import org.jboss.arquillian.graphene.enricher.SearchContextInterceptor;
import org.jboss.arquillian.graphene.enricher.StaleElementInterceptor;
import org.jboss.arquillian.graphene.page.extension.PageExtensionInstallatorProvider;
import org.jboss.arquillian.graphene.page.extension.PageExtensionRegistry;
import org.jboss.arquillian.graphene.page.extension.PageExtensionRegistryImpl;
import org.jboss.arquillian.graphene.page.extension.RemotePageExtensionInstallatorProvider;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.openqa.selenium.HasInputDevices;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class GrapheneContextWrapper implements GrapheneContext {

    private final Instance<DroneContext> droneContext;
    private final Class<? extends Annotation> qualifier;
    private final GrapheneConfiguration configuration;
    private final PageExtensionInstallatorProvider pageExtensionInstallatorProvider;
    private final PageExtensionRegistry pageExtensionRegistry;
    private final GrapheneProxy.FutureTarget target = new GrapheneProxy.FutureTarget() {
        @Override
        public Object getTarget() {
            WebDriver target = droneContext.get().get(WebDriver.class, qualifier);
            if (target instanceof GrapheneProxyInstance) {
                target = ((GrapheneProxyInstance) target).unwrap();
            }
            return target;
        }
    };

    public GrapheneContextWrapper(GrapheneConfiguration configuration, Instance<DroneContext> droneContext, Class<? extends Annotation> qualifier) {
        this.droneContext = droneContext;
        this.qualifier = qualifier;
        this.configuration = configuration;
        this.pageExtensionRegistry = new PageExtensionRegistryImpl();
        this.pageExtensionInstallatorProvider = new RemotePageExtensionInstallatorProvider(pageExtensionRegistry, getWebDriver(JavascriptExecutor.class));
    }

    @Override
    public GrapheneConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public PageExtensionInstallatorProvider getPageExtensionInstallatorProvider() {
        return pageExtensionInstallatorProvider;
    }

    @Override
    public PageExtensionRegistry getPageExtensionRegistry() {
        return pageExtensionRegistry;
    }

    @Override
    public final <T extends WebDriver> T getWebDriver(Class<?>... types) {
        Class<?> baseType = pickClass(types);
        T webdriver = GrapheneProxy.getProxyForFutureTarget(target, baseType == null ? null : baseType, WebDriver.class, JavascriptExecutor.class, HasInputDevices.class);
        GrapheneProxyInstance proxy = (GrapheneProxyInstance) webdriver;
        proxy.registerInterceptor(new SearchContextInterceptor());
        proxy.registerInterceptor(new StaleElementInterceptor());
        return webdriver;
    }

    private Class<?> pickClass(Class<?>... types) {
        for (Class<?> type: types) {
            if (!type.isInterface()) {
                return type;
            }
        }
        return null;
    }
}
