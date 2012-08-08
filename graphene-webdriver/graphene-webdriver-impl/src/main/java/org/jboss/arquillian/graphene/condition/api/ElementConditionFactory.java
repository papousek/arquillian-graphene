package org.jboss.arquillian.graphene.condition.api;

import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public interface ElementConditionFactory extends BasicConditionFactory<ElementConditionFactory> {

    /**
     * Returns a condition holding if and only if the element is selected.
     *
     * @return
     */
    public ExpectedCondition<Boolean> isSelected();

    /**
     * Returns a condition holding if and only if the element is visible (present and displayed).
     *
     * @return
     */
    public ExpectedCondition<Boolean> isVisible();

    /**
     * Returns a condition holding if and only if the element contains the given text.
     *
     * @param expected
     * @return
     */
    public ExpectedCondition<Boolean> textContains(String expected);

    /**
     * Returns a condition holding if and only if the text inside the element
     * equals to the given one.
     *
     * @return
     */
    public ExpectedCondition<Boolean> textEquals(String expected);

}
