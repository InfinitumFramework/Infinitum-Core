/*
 * Copyright (C) 2012 Clarion Media, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * @since 1.0
 * @see JdkDynamicProxy
 * @see DexMakerProxy
 */
public abstract class AbstractProxy implements InvocationHandler {

	/**
	 * Indicates the backing implementation of a proxy.
	 */
	public static enum ProxyType {
		JdkDynamic, DexMaker
	};

	protected Object mTarget;

	/**
	 * Creates a new {@code AbstractProxy}.
	 * 
	 * @param target
	 *            the proxied {@link Object}
	 */
	public AbstractProxy(Object target) {
		mTarget = target;
	}

	/**
	 * Retrieves a handle for the given proxy {@link Object}, if it is one.
	 * 
	 * @param object
	 *            the {code Object} to retrieve a proxy instance for
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
	 * @param object
	 *            the {@code Object} to check
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
	 * @param object
	 *            the proxy to retrieve the target {@code Object} for
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
	 * @param object
	 *            the {@code Object} to check
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
	 * @param proxy
	 *            the proxy to retrieve the {@code InvocationHandler} for
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
	 * @param target
	 *            {@code Object}
	 */
	public void setTarget(Object target) {
		mTarget = target;
	}

}
