package org.jboss.arquillian.graphene.condition.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.jboss.logging.Logger;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class BooleanConditionWrapper implements ExpectedCondition<Boolean> {

    private final ExpectedCondition<?> wrapped;
    private final boolean negation;
    private final Set<Class<? extends RuntimeException>> ignoredExceptions = new HashSet<Class<? extends RuntimeException>>();

    protected static final Logger LOGGER = Logger.getLogger(BooleanConditionWrapper.class);

    public BooleanConditionWrapper(ExpectedCondition<?> wrapped, Class<? extends RuntimeException>... ignoredExceptions) {
        this(wrapped, false, ignoredExceptions);
    }

    public BooleanConditionWrapper(ExpectedCondition<?> wrapped, boolean negation, Class<? extends RuntimeException>... ignoredExceptions) {
        if (wrapped == null) {
            throw new IllegalArgumentException("The wrapped is null.");
        }
        this.wrapped = wrapped;
        this.negation = negation;
        this.ignoredExceptions.addAll(Arrays.asList(ignoredExceptions));
    }

    @Override
    public Boolean apply(WebDriver driver) {
        try {
            Object original = wrapped.apply(driver);
            if (original instanceof Boolean) {
                if (negation) {
                    return !((Boolean) original).booleanValue();
                } else {
                    return (Boolean) original;
                }
            } else {
                if (negation) {
                    return original == null;
                } else {
                    return original != null;
                }
            }
        } catch(StaleElementReferenceException ignored) {
            LOGGER.debug("The element is stale.", ignored);
            return false;
        } catch(RuntimeException e) {
            if (ignoredExceptions.contains(e.getClass())) {
                LOGGER.debug("Exception ignored, returning " + negation + ".", e);
                return negation;
            } else {
                throw e;
            }
        }
    }

}
