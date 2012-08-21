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
Document.Graphene = Document.Graphene || {};

/**
 * The XMLHttpRequest injection providing ability to intercept requests.
 */
Document.Graphene.xhrInterception = {
    /**
     * Flags for different implementations of XHR
     */
    isXHR: !!window.XMLHttpRequest,
    isActiveX: !!window.ActiveXObject,
    /**
     * The hash of arrays of functions (with method names to intercept like keys)
     */
    interceptors: {},

    /**
     * The prototype of injected XHR object.
     *
     * Delegates to intercepter chain.
     */
    wrapperPrototype: {
        abort : function() {
            return Document.Graphene.xhrInterception.invokeInterceptorChain(this, 'abort', arguments);
        },
        open : function() {
            return Document.Graphene.xhrInterception.invokeInterceptorChain(this, 'open', arguments);
        },
        getAllResponseHeaders : function() {
            return Document.Graphene.xhrInterception.invokeInterceptorChain(this, 'getAllResponseHeaders', arguments);
        },
        getResponseHeader : function() {
            return Document.Graphene.xhrInterception.invokeInterceptorChain(this, 'getResponseHeader', arguments);
        },
        send : function() {
            return Document.Graphene.xhrInterception.invokeInterceptorChain(this, 'send', arguments);
        },
        setRequestHeader : function() {
            return Document.Graphene.xhrInterception.invokeInterceptorChain(this, 'setRequestHeader', arguments);
        },
        onreadystatechange : undefined
    },

    /**
     * Injects XHR wrapper for Firefox/Chromium/WebKit and similar browsers
     */
    replaceXHR: function() {
        this.original = window.XMLHttpRequest;
        window.XMLHttpRequest = this.createReplacement();
    },

    /**
     * Reverts XHR wrapper for Firefox/Chromium/WebKit and similar browsers
     */
    revertXHR: function() {
        window.XMLHttpRequest = this.original;
        this.original = undefined;
    },

    /**
     * Creates XHR wrapper for replacement of original XHR object
     */
    createReplacement: function() {
        var Replacement = function() {
            this.xhr = new Document.Graphene.xhrInterception.original();
            this.xhr.onreadystatechange = Document.Graphene.xhrInterception.callback(this);
        };
        Replacement.prototype = this.wrapperPrototype;
        return Replacement;
    },

    /**
     * onreadystatechange callback which is registered on true XHR instance.
     *
     * Delegates to intercepter chain.
     */
    callback: function(wrapper) {
        return function() {
            wrapper.readyState = this.readyState;
            if (wrapper.readyState == 4) {
                wrapper.responseText = this.responseText;
                wrapper.responseXML = this.responseXML;
                wrapper.status = this.status;
                wrapper.statusText = this.statusText;
            }
            Document.Graphene.xhrInterception.invokeInterceptorChain(wrapper, 'onreadystatechange', [ wrapper ]);
        };
    },

    /**
     * Decides which injection is necessary for current browser
     */
    replace: function() {
        if (this.isXHR) {
            this.replaceXHR();
        }
    },

    /**
     * Decides which injection is necessary for current browser
     */
    revert: function() {
        if (this.isXHR) {
            this.revertXHR();
        }
    },

    /**
     * Registers intercepter in the chain of intercepters.
     */
    registerInterceptor: function(methodName, interceptor) {
        if (typeof this.interceptors[methodName] == 'undefined') {
            this.interceptors[methodName] =  [];
        }
        this.interceptors[methodName].push(interceptor);
    },

    /**
     * Starts the execution of interceptor chain.
     *
     * The method calls or the interceptors in the chain and once all of them are fired, calls original implementation.
     *
     * @param wrapper
     *            XHR wrapper instance
     * @param methodName
     *            the name of the method invoked
     * @param arguments
     *            of the invocation
     * @param i
     *            (optional) the number of interceptor to invoke (if there is no such interceptor, function delegates to real
     *            method)
     */
    invokeInterceptorChain: function(wrapper, methodName, args, i) {
        var i = i || 0;
        if (this.interceptors[methodName] && this.interceptors[methodName].length > i) {
            return this.invokeNextInterceptor(wrapper, methodName, args, i);
        } else {
            return this.invokeRealMethod(wrapper, methodName, args);
        }
    },

    /**
     * Invokes next intercepter in the chain
     */
    invokeNextInterceptor: function(wrapper, methodName, args, i) {
        var context = {
            xhrOriginal : wrapper.xhr,
            xhrWrapper : wrapper,
            proceed : function() {
                return Document.Graphene.xhrInterception.invokeInterceptorChain(wrapper, methodName, args, i + 1);
            }
        };
        var interceptor = this.interceptors[methodName][i];
        return interceptor.call(wrapper, context, args);
    },

    /**
     * Invokes original XHR implemention method.
     *
     * If onreadystatechange callback is processed, it is invoked on wrapper; otherwise method of the XHR instance is invoked.
     */
    invokeRealMethod: function(wrapper, methodName, args) {
        var xhr = (methodName === 'onreadystatechange') ? wrapper : wrapper.xhr;
        if (xhr[methodName]) {
            return xhr[methodName].apply(xhr, args);
        }
    },

    /**
     * Ensures the interceptor is installed properly
     */
    install : function() {
        if (!this.original) {
            this.replace();
        }
    },
    /**
     * Removes all registered interceptors.
     */
    uninstall : function() {
        this.interceptors = {};
        if (this.original) {
            this.revert();
        }
    },
    /**
     * Registers intercepter for abort method.
     *
     * Interceptor is function with two params: context and args.
     *
     * Sample: function(context, args) { context.proceed(args); }
     */
    onAbort : function(interceptor) {
        this.registerInterceptor('abort', interceptor);
    },
    /**
     * Registers intercepter for open method.
     *
     * Interceptor is function with two params: context and args.
     *
     * Sample: function(context, args) { context.proceed(args); }
     */
    onOpen : function(interceptor) {
        this.registerInterceptor('open', interceptor);
    },
    /**
     * Registers intercepter for getAllResponseHeaders method.
     *
     * Interceptor is function with two params: context and args.
     *
     * Sample: function(context, args) { context.proceed(args); }
     */
    onGetAllResponseHeaders : function(interceptor) {
        this.registerInterceptor('getAllResponseHeaders', interceptor);
    },
    /**
     * Registers intercepter for send method.
     *
     * Interceptor is function with two params: context and args.
     *
     * Sample: function(context, args) { context.proceed(args); }
     */
    onSend : function(interceptor) {
        this.registerInterceptor('send', interceptor);
    },
    /**
     * Registers intercepter for setRequestHeader method.
     *
     * Interceptor is function with two params: context and args.
     *
     * Sample: function(context, args) { context.proceed(args); }
     */
    onSetRequestHeader : function(interceptor) {
        this.registerInterceptor('setRequestHeader', interceptor);
    },
    /**
     * Registers intercepter for onreadystatechange callback method.
     *
     * Interceptor is function with two params: context and args.
     *
     * Sample: function(context, args) { context.proceed(args); }
     */
    onreadystatechange : function(interceptor) {
        this.registerInterceptor('onreadystatechange', interceptor);
    }
};