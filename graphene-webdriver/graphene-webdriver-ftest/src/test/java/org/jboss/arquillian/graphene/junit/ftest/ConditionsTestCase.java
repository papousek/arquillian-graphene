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
package org.jboss.arquillian.graphene.junit.ftest;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.condition.api.ElementConditionFactory;
import org.jboss.arquillian.graphene.configuration.GrapheneConfiguration;
import org.jboss.arquillian.graphene.context.GrapheneConfigurationContext;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assume;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import static org.jboss.arquillian.graphene.Graphene.*;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(Arquillian.class)
public class ConditionsTestCase {

    @Drone
    private WebDriver browser;

    private static final By BY_INPUT = By.name("q");
    private static final By BY_SEARCH = By.id("search");
    private static final By BY_TOP_STUFF = By.id("topstuff");
    private static final By BY_TRY_LUCK = By.name("btnI");

    @FindBy(name="q")
    private WebElement input;
    @FindBy(id="search")
    private WebElement search;
    @FindBy(id="topstuff")
    private WebElement topStuff;
    @FindBy(name="btnI")
    private WebElement tryLuck;

    @Test
    public void testElementIsDisplayed() {
        init(browser);
        waitModel(browser).until(element(tryLuck).isVisible());
    }

    @Test
    public void testElementIsDisplayedWithBy() {
        init(browser);
        waitModel(browser).until(element(BY_TRY_LUCK).isVisible());
    }

    @Test
    public void testElementIsPresent() {
        init(browser);
        checkElementIsPresent(element(topStuff));
    }

    @Test
    public void testElementIsPresentWithBy() {
        init(browser);
        checkElementIsPresent(element(BY_TOP_STUFF));
    }

    @Test
    public void testElementTextContains() throws InterruptedException {
        init(browser);
        checkElementTextContains(element(search));
    }

    @Test
    public void testElementTextContainsWithBy() throws InterruptedException {
        init(browser);
        checkElementTextContains(element(BY_SEARCH));
    }

    @Test
    public void testAttributeIsPresent() {
        init(browser);
        waitModel(browser).until(attribute(input, "value").isPresent());
    }

    @Test
    public void testAttributeValueContains() {
        init(browser);
        input.sendKeys("florence and the machine");
        waitModel(browser).until(attribute(input, "value").valueContains("florence"));
    }

    @Test
    public void testAttributeValueEquals() {
        init(browser);
        input.sendKeys("florence and the machine");
        waitModel(browser).until(attribute(input, "value").valueEquals("florence and the machine"));
    }

    @Test
    public void testNotElementIsDisplayed() {
        init(browser);
        checkNotElementIsDisplayed(element(tryLuck));
    }

    @Test
    public void testNotElementIsDisplayedWithBy() {
        init(browser);
        checkNotElementIsDisplayed(element(BY_TRY_LUCK));
    }

    @Test
    public void testNotElementIsPresent() {
        init(browser);
        waitModel(browser).until(element(search).not().isPresent());
    }

    @Test
    public void testNotElementIsPresentWithBy() {
        init(browser);
        waitModel(browser).until(element(BY_SEARCH).not().isPresent());
    }

    @Test
    public void testNotElementTextContains() {
        init(browser);
        checkNotElementTextContains(element(search));
    }

    @Test
    public void testNotElementTextContainsWithBy() {
        init(browser);
        checkNotElementTextContains(element(BY_SEARCH));
    }

    @Test
    public void testNotElementTextEquals() {
        init(browser);
        checkNotElementTextEquals(element(search));
    }

     @Test
    public void testNotElementTextEqualsWithBy() {
        init(browser);
        checkNotElementTextEquals(element(BY_SEARCH));
    }

    @Test
    public void testNotAttributeIsPresent() {
        init(browser);
        waitModel(browser).until(attribute(input, "laryfary").not().isPresent());
    }

    @Test
    public void testNotAttributeValueContains() {
        init(browser);
        input.sendKeys("florence and the machine");
        waitModel(browser).until(attribute(input, "value").not().valueContains("rolling stones"));
    }

    @Test
    public void testNotAttributeValueEquals() {
        init(browser);
        input.sendKeys("florence and the machine");
        waitModel(browser).until(attribute(input, "value").not().valueEquals("florence"));
    }

    protected void init(WebDriver driver) {
        // TODO replace with automatic instantiation using ARQGRA-40
        GrapheneConfigurationContext.set(new GrapheneConfiguration());
        driver.get("http://google.com");
        PageFactory.initElements(new DefaultElementLocatorFactory(driver), this);
        driver.get("http://google.com"); // HACK
    }

    protected void checkElementIsPresent(ElementConditionFactory topStuffElementConditionFactory) {
        Assume.assumeTrue(!(browser instanceof HtmlUnitDriver));
        input.sendKeys("florence and the machine");
        input.submit();
        waitModel(browser).until(element(topStuff).isPresent());
    }

    protected void checkElementTextContains(ElementConditionFactory searchElementConditionFactory) throws InterruptedException {
        Assume.assumeTrue(!(browser instanceof HtmlUnitDriver));
        input.sendKeys("florence");
        waitModel(browser).until(element(search).isPresent());
        input.sendKeys("and the machine");
        waitModel(browser).until(element(search).textContains("florence and the machine"));
    }

    protected void checkNotElementIsDisplayed(ElementConditionFactory tryLuckElementConditionFactory) {
        Assume.assumeTrue(!(browser instanceof HtmlUnitDriver));
        input.sendKeys("florence and the machine");
        input.submit();
        waitModel(browser).until(tryLuckElementConditionFactory.not().isVisible());
    }

    protected void checkNotElementTextContains(ElementConditionFactory searchElementConditionFactory) {
        Assume.assumeTrue(!(browser instanceof HtmlUnitDriver));
        input.sendKeys("florence and the machine");
        input.clear();
        input.sendKeys("rolling stones");
        waitModel(browser).until(searchElementConditionFactory.not().textContains("florence and the machine"));
    }

    protected void checkNotElementTextEquals(ElementConditionFactory searchElementConditionFactory) {
        Assume.assumeTrue(!(browser instanceof HtmlUnitDriver));
        input.sendKeys("florence and the machine");
        input.clear();
        input.sendKeys("rolling stones");
        waitModel(browser).until(searchElementConditionFactory.not().textEquals("lary fary"));
    }

}
