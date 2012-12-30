/*
 * Copyright (c) 2012 Tyler Treat
 * 
 * This file is part of Infinitum Framework.
 *
 * Infinitum Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Infinitum Framework is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Infinitum Framework.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.clarionmedia.infinitum.di;

import java.lang.reflect.InvocationHandler;

/**
 * <p>
 * Provides proxy support for the AOP framework. Infinitum offers two
 * implementations for JDK dynamic proxies and DexMaker proxies.
 * </p>
 *
 * @author Tyler Treat
 * @version 1.0 07/13/12
 * @see JdkDynamicProxy
 * @see DexMakerProxy
 * @since 1.0
 */
public abstract class AbstractProxy implements InvocationHandler {

    /**
     * Indicates the backing implementation of a proxy.
     */
    public static enum ProxyType {
        JdkDynamic, DexMaker
    }

    ;

    protected Object mTarget;

    /**
     * Creates a new {@code AbstractProxy}.
     *
     * @param target the proxied {@link Object}
     */
    public AbstractProxy(Object target) {
        mTarget = target;
    }

    /**
     * Retrieves a handle for the given proxy {@link Object}, if it is one.
     *
     * @param object the {code Object} to retrieve a proxy instance for
     * @return {@code AbstractProxy} or {@code null} if {@code object} is not a proxy
     */
    public static AbstractProxy getProxy(Object object) {
        AbstractProxy proxy = DexMakerProxy.getProxy(object);
        if (proxy != null)
            return proxy;
        proxy = JdkDynamicProxy.getProxy(object);
        return proxy;
    }

    /**
     * Indicates if the given {@link Object} is an {@link AbstractProxy}.
     *
     * @param object the {@code Object} to check
     * @return {@code true} if it is a proxy, {@code false} if not
     */
    public static boolean isAopProxy(Object object) {
        boolean isProxy = DexMakerProxy.isAopProxy(object);
        if (!isProxy)
            isProxy = JdkDynamicProxy.isAopProxy(object);
        return isProxy;
    }

    /**
     * Retrieves the proxied {@link Object}.
     *
     * @param object the proxy to retrieve the target {@code Object} for
     * @return target {@code Object} or {@code object} if it is not a proxy
     */
    public static Object getTarget(Object object) {
        if (!isAopProxy(object))
            return object;
        return getProxy(object).getTarget();
    }

    /**
     * Indicates if the given {@link Object} is a proxy or not
     *
     * @param object the {@code Object} to check
     * @return {@code true} if it is a proxy, {@code false} if not
     */
    public abstract boolean isProxy(Object object);

    /**
     * Creates a new proxy
     *
     * @return proxy {@link Object}
     */
    public abstract Object getProxy();

    /**
     * Returns the {@link InvocationHandler} for the given proxy.
     *
     * @param proxy the proxy to retrieve the {@code InvocationHandler} for
     * @return {@code InvocationHandler} or {@code null} if the given
     *         {@code Object} is not a proxy
     */
    public abstract InvocationHandler getInvocationHandler(Object proxy);

    /**
     * Returns the {@link ProxyType} for this {@code AbstractProxy}.
     *
     * @return {@code ProxyType}
     */
    public abstract ProxyType getProxyType();

    /**
     * Creates a copy of the {@code AbstractProxy}.
     *
     * @return copied {@code AbstractProxy}
     */
    public abstract AbstractProxy clone();

    /**
     * Returns the proxied {@link Object}.
     *
     * @return target {@code Object}
     */
    public Object getTarget() {
        return mTarget;
    }

    /**
     * Sets the proxied {@link Object}.
     *
     * @param target {@code Object}
     */
    public void setTarget(Object target) {
        mTarget = target;
    }

}
