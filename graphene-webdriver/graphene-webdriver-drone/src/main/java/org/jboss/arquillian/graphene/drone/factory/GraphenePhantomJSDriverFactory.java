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
package org.jboss.arquillian.graphene.drone.factory;

import org.jboss.arquillian.drone.spi.Configurator;
import org.jboss.arquillian.drone.spi.Destructor;
import org.jboss.arquillian.drone.spi.Instantiator;
import org.jboss.arquillian.drone.webdriver.configuration.PhantomJSDriverConfiguration;
import org.jboss.arquillian.drone.webdriver.configuration.TypedWebDriverConfiguration;
import org.jboss.arquillian.drone.webdriver.factory.PhantomJSDriverFactory;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

/**
 * Extends the {@link PhantomJSDriverFactory} and provides the created instance to the {@link GrapheneContext}.
 *
 * @author <a href="jpapouse@redhat.com">Jan Papousek</a>
 *
 */
public class GraphenePhantomJSDriverFactory extends PhantomJSDriverFactory implements
        Configurator<PhantomJSDriver, TypedWebDriverConfiguration<PhantomJSDriverConfiguration>>,
        Instantiator<PhantomJSDriver, TypedWebDriverConfiguration<PhantomJSDriverConfiguration>>, Destructor<PhantomJSDriver> {

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.drone.spi.Sortable#getPrecedence()
     */
    @Override
    public int getPrecedence() {
        return 20;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.drone.spi.Destructor#destroyInstance(java.lang.Object)
     */
    @Override
    public void destroyInstance(PhantomJSDriver instance) {
        try {
            super.destroyInstance(instance);
        } finally {
            GrapheneContext.reset();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.drone.spi.Instantiator#createInstance(org.jboss.arquillian.drone.spi.DroneConfiguration)
     */
    @Override
    public PhantomJSDriver createInstance(TypedWebDriverConfiguration<PhantomJSDriverConfiguration> configuration) {
        PhantomJSDriver driver = super.createInstance(configuration);
        PhantomJSDriver proxy = GrapheneContext.getProxyForDriver(PhantomJSDriver.class);
        GrapheneContext.set(driver);
        return proxy;
    }
}
