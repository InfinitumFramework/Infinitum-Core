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
import java.lang.reflect.Proxy;

import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.internal.Preconditions;
import com.clarionmedia.infinitum.reflection.ClassReflector;
import com.clarionmedia.infinitum.reflection.impl.DefaultClassReflector;

/**
 * <p>
 * Abstract implementation of {@link AbstractProxy} that relies on the JDK-provided
 * {@link Proxy} in order to proxy interfaces.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/14/12
 * @since 1.0
 * @see AdvisedJdkDynamicProxy
 */
public abstract class JdkDynamicProxy extends AbstractProxy {

	protected final Class<?>[] mInterfaces;

	/**
	 * Creates a new {@code JdkDynamicProxy}.
	 * 
	 * @param target
	 *            the proxied {@link Object}
	 * @param interfaces
	 *            the interfaces the proxy will implement
	 */
	public JdkDynamicProxy(Object target, Class<?>[] interfaces) {
		super(target);
		Preconditions.checkNotNull(interfaces);
		ClassReflector reflector = new DefaultClassReflector();
		Class<?>[] realInterfaces = new Class<?>[interfaces.length];
		for (int i = 0; i < interfaces.length; i++) {
			Class<?> realInterface;
			Class<?> clazz = interfaces[i];
			if (!clazz.isInterface()) {
				if (InfinitumContext.class.isAssignableFrom(clazz)) {
					realInterface = reflector.getSuperInterface(clazz, InfinitumContext.class);
					if (realInterface == null)
						throw new IllegalArgumentException("'" + clazz.getName() + "' is not an interface.");
				} else {
					realInterface = reflector.getSuperInterface(clazz);
					if (realInterface == null)
						throw new IllegalArgumentException("'" + clazz.getName()
								+ "' is not an interface and does not implement an interface.");
				}
			} else {
				realInterface = clazz;
			}
			realInterfaces[i] = realInterface;
		}
		mInterfaces = realInterfaces;
	}

	/**
	 * Retrieves a {@code JdkDynamicProxy} instance for the given proxy.
	 * 
	 * @param object
	 *            the {@link Object} to retrieve a proxy instance for
	 * @return {@code JdkDynamicProxy} or {@code null} if {@code object} is not
	 *         a proxy
	 */
	public static JdkDynamicProxy getProxy(Object object) {
		if (!Proxy.isProxyClass(object.getClass()))
			return null;
		return (JdkDynamicProxy) Proxy.getInvocationHandler(object);
	}

	/**
	 * Indicates if the given {@link Object} is an {@link AbstractProxy}.
	 * 
	 * @param object
	 *            the {@code Object} to check
	 * @return {@code true} if it is a proxy, {@code false} if not
	 */
	public static boolean isAopProxy(Object object) {
		return Proxy.isProxyClass(object.getClass());
	}

	@Override
	public final boolean isProxy(Object object) {
		return Proxy.isProxyClass(object.getClass());
	}

	@Override
	public final Object getProxy() {
		return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), mInterfaces, this);
	}

	@Override
	public final InvocationHandler getInvocationHandler(Object proxy) {
		if (!isProxy(proxy))
			return null;
		return Proxy.getInvocationHandler(proxy);
	}

	@Override
	public final ProxyType getProxyType() {
		return ProxyType.JdkDynamic;
	}

}
