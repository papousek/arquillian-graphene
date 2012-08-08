package org.jboss.arquillian.graphene.condition.api;

import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public interface BasicConditionFactory<F extends BasicConditionFactory> extends BooleanConditionFactory<F> {

    /**
     * Returns a condition holding if and only if the object is present on the page.
     *
     * @return
     */
    public ExpectedCondition<Boolean> isPresent();

}
