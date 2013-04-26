package org.jboss.arquillian.graphene.wait.enhaced;

public interface EnhancedElement {

    boolean isPresent();

    void fire(Event event);

}
