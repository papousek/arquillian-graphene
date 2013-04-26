package org.jboss.arquillian.graphene.ftest.enhanced;

import junit.framework.Assert;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.wait.enhaced.Event;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class WebElementEnhancedElement extends AbstractEnhancedElementTest {

    @FindBy
    private WebElement onclick;

    @FindBy
    private WebElement doesntExist;

    @FindBy
    private WebElement output;

    @Test
    public void testFire() {
        Graphene.element(onclick).fire(Event.CLICK);
        Assert.assertEquals("The event hasn't been fired!", "CLICK", output.getText().trim());
    }

    @Test
    public void testIsPresent() {
        Assert.assertTrue(Graphene.element(output).isPresent());
        Assert.assertFalse(Graphene.element(doesntExist).isPresent());
    }

}
