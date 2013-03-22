/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.arquillian.graphene;

import java.util.Collection;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author jpapouse
 */
public interface WebDriverProxyFactory {

    <T extends WebDriver> T getProxy(WebDriver webDriver, Class<T> type);

    Collection<Class<?>> getProxiedClasses();

}
