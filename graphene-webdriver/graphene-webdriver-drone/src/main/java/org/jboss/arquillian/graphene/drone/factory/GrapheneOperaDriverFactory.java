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

import com.opera.core.systems.OperaDriver;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.spi.Configurator;
import org.jboss.arquillian.drone.spi.Destructor;
import org.jboss.arquillian.drone.spi.Instantiator;
import org.jboss.arquillian.drone.webdriver.configuration.OperaDriverConfiguration;
import org.jboss.arquillian.drone.webdriver.configuration.TypedWebDriverConfiguration;
import org.jboss.arquillian.drone.webdriver.factory.OperaDriverFactory;
import org.jboss.arquillian.graphene.GrapheneContext;
import org.jboss.arquillian.graphene.WebDriverProxyFactory;

/**
 * Extends the {@link OperaDriverFactory} and provides the created instance to the {@link GrapheneContext}.
 *
 * @author <a href="jpapouse@redhat.com">Jan Papousek</a>
 *
 */
public class GrapheneOperaDriverFactory extends OperaDriverFactory implements
        Configurator<OperaDriver, TypedWebDriverConfiguration<OperaDriverConfiguration>>,
        Instantiator<OperaDriver, TypedWebDriverConfiguration<OperaDriverConfiguration>>, Destructor<OperaDriver> {

    @Inject
    private Instance<WebDriverProxyFactory> webDriverProxyFactory;

    @Override
    public int getPrecedence() {
        return 20;
    }

    @Override
    public OperaDriver createInstance(TypedWebDriverConfiguration<OperaDriverConfiguration> configuration) {
        OperaDriver driver = super.createInstance(configuration);
        return webDriverProxyFactory.get().getProxy(driver, OperaDriver.class);
    }

}
