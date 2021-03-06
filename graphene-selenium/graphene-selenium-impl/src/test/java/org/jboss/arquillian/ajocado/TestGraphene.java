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
package org.jboss.arquillian.ajocado;

import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.ajocado.framework.GrapheneConfiguration;
import org.jboss.arquillian.ajocado.framework.GrapheneConfiguration.TimeoutType;
import org.jboss.arquillian.ajocado.framework.GrapheneConfigurationContext;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class TestGraphene {

    private static final long TIMEOUT = 500L;
    private static final long TOLERANCE = 1000L;
    private static final int LOOPS = 8;

    @Mock
    GrapheneConfiguration configuration;

    @BeforeMethod
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        GrapheneConfigurationContext.set(configuration);
    }

    @Test
    public void testAjaxWaitTimeoutConfiguration() {
        when(configuration.getTimeout(any(TimeoutType.class))).thenReturn(TIMEOUT);

        long runtime = System.currentTimeMillis();
        for (int i = 0; i < LOOPS; i++) {
            waitGui.waitForTimeout();
        }
        runtime = System.currentTimeMillis() - runtime;
        runtime = runtime / LOOPS;

        assertTrue(runtime > TIMEOUT, "runtime (" + runtime + ") is less than TIMEOUT=" + (TIMEOUT));
        assertTrue(runtime < TIMEOUT + TOLERANCE, "runtime (" + runtime + ") is greater than TIMEOUT + TOLERANCE = "
            + TIMEOUT + " + " + TOLERANCE + " = " + (TIMEOUT + TOLERANCE));
    }
}
