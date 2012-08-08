package org.jboss.arquillian.graphene.condition.api;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractBooleanConditionFactory<F extends BooleanConditionFactory> implements BooleanConditionFactory<F> {

    private boolean negation = false;

    @Override
    public F not() {
        AbstractBooleanConditionFactory copy = (AbstractBooleanConditionFactory) copy();
        copy.setNegation(!this.getNegation());
        return (F) copy;
    }

    protected final boolean getNegation() {
        return negation;
    }

    protected final void setNegation(boolean negation) {
        this.negation = negation;
    }

    abstract protected F copy();

}
