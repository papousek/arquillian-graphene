/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.guard;

import org.jboss.arquillian.graphene.context.GrapheneProxy;
import org.jboss.arquillian.graphene.context.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.context.Interceptor;
import org.jboss.arquillian.graphene.context.InvocationContext;
import org.jboss.arquillian.graphene.javascript.JSInterfaceFactory;
import org.jboss.arquillian.graphene.page.RequestType;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class RequestGuardFactory {

    public static <T> T guard(T target, final RequestType requestExpected) {
        if (requestExpected == null) {
            throw new IllegalArgumentException("The paremeter [requestExpected] is null.");
        }
        if (target == null) {
            throw new IllegalArgumentException("The paremeter [target] is null.");
        }
        GrapheneProxyInstance proxy;
        if (!GrapheneProxy.isProxyInstance(target)) {
            proxy = (GrapheneProxyInstance) GrapheneProxy.getProxyForTarget(target);
        } else {
            proxy = (GrapheneProxyInstance) ((GrapheneProxyInstance) target).copy();
        }
        proxy.registerInterceptor(new Interceptor() {
            @Override
            public Object intercept(InvocationContext context) throws Throwable {
                RequestGuard guard = JSInterfaceFactory.create(RequestGuard.class);
                guard.clearRequestDone();
                Object result =  context.invoke();
                if (!guard.getRequestDone().equals(requestExpected)) {
                    throw new RequestGuardExpcetion(requestExpected, guard.getRequestDone());
                }
                return result;
            }
        });
        return (T) proxy;
    }

}
