package org.jboss.arquillian.graphene.condition.api;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public interface BooleanConditionFactory<F extends BooleanConditionFactory> {

    /**
     * Returns a new condition factory negating all created conditions. Validity
     * of negated conditions is reverse to the conditions created by the original
     * condition factory.
     *
     * @return
     */
    F not();

}
