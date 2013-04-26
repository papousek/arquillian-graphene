/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.arquillian.graphene.ftest.performance;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 *
 * @author jpapouse
 */
@RunWith(Arquillian.class)
public class Cache {

    @FindBy
    private WebElement gbqfq;

    @Drone
    private WebDriver browser;

    private static final long SIZE = 1000;

    @Before
    public void prepare() {
        browser.get("http://google.com");
    }

    @Test
    public void testInjected() {
        long before = System.currentTimeMillis();
        for (int i=0; i<SIZE; i++) {
            gbqfq.click();
        }
        System.out.println("INJECTED: " + (System.currentTimeMillis() - before));
    }

    @Test
    public void testFound() {
        WebElement unwrapped = (WebElement) ((GrapheneProxyInstance) gbqfq).unwrap();
        long before = System.currentTimeMillis();
        for (int i=0; i<SIZE; i++) {
            unwrapped.click();
        }
        System.out.println("UNWRAPPED: " + (System.currentTimeMillis() - before));
    }

}
