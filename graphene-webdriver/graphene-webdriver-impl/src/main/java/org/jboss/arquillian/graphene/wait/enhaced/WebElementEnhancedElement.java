package org.jboss.arquillian.graphene.wait.enhaced;

import org.jboss.arquillian.graphene.GrapheneContext;
import org.jboss.arquillian.graphene.javascript.JSInterfaceFactory;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

public class WebElementEnhancedElement implements EnhancedElement {

    private final WebElement element;
    private final JQueryEventTrigger trigger;

    public WebElementEnhancedElement(WebElement element) {
        this.element = element;
        GrapheneContext context = ((GrapheneProxyInstance) element).getContext();
        this.trigger = JSInterfaceFactory.create(context, JQueryEventTrigger.class);
    }

    @Override
    public boolean isPresent() {
        try {
            element.isDisplayed();
            return true;
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }

    @Override
    public void fire(Event event) {
        trigger.triggerEvent(element, event.getEventName());
    }

}
