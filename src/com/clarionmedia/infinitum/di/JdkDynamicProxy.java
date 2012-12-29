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
import java.lang.reflect.Proxy;

import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.internal.Preconditions;
import com.clarionmedia.infinitum.reflection.ClassReflector;
import com.clarionmedia.infinitum.reflection.impl.DefaultClassReflector;

/**
 * <p>
 * Abstract implementation of {@link AopProxy} that relies on the JDK-provided
 * {@link Proxy} in order to proxy interfaces.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/14/12
 * @since 1.0
 * @see AdvisedJdkDynamicProxy
 */
public abstract class JdkDynamicProxy extends AopProxy {

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
	 * Indicates if the given {@link Object} is an {@link AopProxy}.
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
