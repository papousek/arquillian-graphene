package org.jboss.arquillian.graphene.guard;

import org.jboss.arquillian.graphene.javascript.Dependency;
import org.jboss.arquillian.graphene.javascript.InstallableJavaScript;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.graphene.page.RequestType;
import org.jboss.arquillian.graphene.page.extension.PageExtensionJavaScriptExecutionResolver;
import org.jboss.arquillian.graphene.page.interception.XhrInterception;

@JavaScript(value = "Document.Graphene.Page.RequestGuard", methodResolver=PageExtensionJavaScriptExecutionResolver.class)
@Dependency(sources = "Graphene.Page.RequestGuard.js", interfaces=XhrInterception.class)
public interface RequestGuard extends InstallableJavaScript {

    RequestType getRequestDone();

    RequestType clearRequestDone();

}