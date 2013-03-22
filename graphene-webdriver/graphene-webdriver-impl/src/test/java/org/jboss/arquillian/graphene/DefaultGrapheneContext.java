package org.jboss.arquillian.graphene;

import org.jboss.arquillian.graphene.configuration.GrapheneConfiguration;
import org.jboss.arquillian.graphene.page.extension.PageExtensionInstallatorProvider;
import org.jboss.arquillian.graphene.page.extension.PageExtensionRegistry;
import org.jboss.arquillian.graphene.page.extension.PageExtensionRegistryImpl;
import org.jboss.arquillian.graphene.page.extension.RemotePageExtensionInstallatorProvider;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class DefaultGrapheneContext implements GrapheneContext {

    private final WebDriver driver;
    private final GrapheneConfiguration configuration;
    private final PageExtensionInstallatorProvider pageExtensionInstallatorProvider;
    private final PageExtensionRegistry pageExtensionRegistry;

    public DefaultGrapheneContext(WebDriver driver) {
        this.driver = driver;
        this.configuration = new GrapheneConfiguration();
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
        return (T) driver;
    }

    public static void init(WebDriver driver) {
        Graphene.setContext(new DefaultGrapheneContext(driver));
    }

    public static void reset() {
        Graphene.reset();
    }

}
