package org.jboss.arquillian.graphene;

import org.jboss.arquillian.graphene.configuration.GrapheneConfiguration;
import org.jboss.arquillian.graphene.page.extension.PageExtensionInstallatorProvider;
import org.jboss.arquillian.graphene.page.extension.PageExtensionRegistry;
import org.openqa.selenium.WebDriver;

public interface GrapheneContext {

    GrapheneConfiguration getConfiguration();

    PageExtensionInstallatorProvider getPageExtensionInstallatorProvider();

    PageExtensionRegistry getPageExtensionRegistry();

    <T extends WebDriver> T getWebDriver(Class<?>... types);

}
