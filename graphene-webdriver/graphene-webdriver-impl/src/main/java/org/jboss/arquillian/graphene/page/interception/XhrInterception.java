package org.jboss.arquillian.graphene.page.interception;

import org.jboss.arquillian.graphene.javascript.Dependency;
import org.jboss.arquillian.graphene.javascript.InstallableJavaScript;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.graphene.page.extension.PageExtensionJavaScriptExecutionResolver;

@JavaScript(value="Document.Graphene.xhrInterception", methodResolver=PageExtensionJavaScriptExecutionResolver.class)
@Dependency(sources = "Graphene.xhrInterception.js")
public interface XhrInterception extends InstallableJavaScript {

    void inject();

    void reset();

    boolean isReplaced();
}
