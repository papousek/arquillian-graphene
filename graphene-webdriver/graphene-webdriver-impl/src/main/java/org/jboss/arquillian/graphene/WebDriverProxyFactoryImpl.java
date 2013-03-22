/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.arquillian.graphene;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.jboss.arquillian.graphene.enricher.SearchContextInterceptor;
import org.jboss.arquillian.graphene.enricher.StaleElementInterceptor;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author jpapouse
 */
public class WebDriverProxyFactoryImpl implements WebDriverProxyFactory{

    private final Set<Class<?>> classes = Collections.synchronizedSet(new HashSet<Class<?>>());

    @Override
    public <T extends WebDriver> T getProxy(WebDriver webDriver, Class<T> type) {
        T result;
        if (type.isInterface()) {
            result = GrapheneProxy.getProxyForFutureTarget(
                    new GrapheneProxy.ConstantFutureTarget(webDriver), null, type);
        } else {
            result = GrapheneProxy.getProxyForFutureTarget(
                    new GrapheneProxy.ConstantFutureTarget(webDriver),type);
        }
        GrapheneProxyInstance proxy = (GrapheneProxyInstance) result;
        proxy.registerInterceptor(new SearchContextInterceptor());
        proxy.registerInterceptor(new StaleElementInterceptor());
        classes.add(type);
        return result;
    }

    @Override
    public Collection<Class<?>> getProxiedClasses() {
        return Collections.unmodifiableCollection(classes);
    }

}
