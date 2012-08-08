package org.jboss.arquillian.graphene.condition.api;

import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public interface AttributeConditionFactory extends BasicConditionFactory<AttributeConditionFactory> {

    /**
     * Returns a condition holding if and only if the attribute value contains
     * the given string.
     *
     * @param expected
     * @return
     */
    public ExpectedCondition<Boolean> valueContains(String expected);

    /**
     * Returns a condition holding if and only if the attribute value equals to
     * the given string.
     *
     * @param expected
     * @return
     */
    public ExpectedCondition<Boolean> valueEquals(String expected);

}
