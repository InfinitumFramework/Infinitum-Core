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

import java.io.IOException;
import java.lang.reflect.InvocationHandler;

import android.content.Context;

import com.clarionmedia.infinitum.exception.InfinitumRuntimeException;
import com.clarionmedia.infinitum.internal.Preconditions;
import com.clarionmedia.infinitum.internal.caching.DexCaching;
import com.google.dexmaker.stock.ProxyBuilder;

/**
 * <p>
 * Abstract implementation of {@link AopProxy} that relies on DexMaker in order
 * to proxy non-final classes in addition to interfaces.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/14/12
 * @since 1.0
 * @see AdvisedDexMakerProxy
 * @see LazyLoadDexMakerProxy
 */
public abstract class DexMakerProxy extends AopProxy {

	protected Context mContext;

	/**
	 * Creates a new {@code DexMakerProxy}.
	 * 
	 * @param context
	 *            the {@link Context} used to retrieve the DEX bytecode cache
	 * @param target
	 *            the proxied {@link Object}
	 */
	public DexMakerProxy(Context context, Object target) {
		super(target);
		Preconditions.checkNotNull(context);
		mContext = context;
	}

	/**
	 * Retrieves a {@code DexMakerProxy} instance for the given proxy.
	 * 
	 * @param object
	 *            the {@link Object} to retrieve a proxy instance for
	 * @return {@code DexMakerProxy} or {@code null} if {@code object} is not a
	 *         proxy
	 */
	public static DexMakerProxy getProxy(Object object) {
		if (!ProxyBuilder.isProxyClass(object.getClass()))
			return null;
		return (DexMakerProxy) ProxyBuilder.getInvocationHandler(object);
	}

	/**
	 * Indicates if the given {@link Object} is an {@link AopProxy}.
	 * 
	 * @param object
	 *            the {@code Object} to check
	 * @return {@code true} if it is a proxy, {@code false} if not
	 */
	public static boolean isAopProxy(Object object) {
		return ProxyBuilder.isProxyClass(object.getClass());
	}

	@Override
	public Object getProxy() {
		try {
			return ProxyBuilder.forClass(mTarget.getClass()).handler(this)
					.dexCache(DexCaching.getDexCache(mContext)).build();
		} catch (IOException e) {
			throw new InfinitumRuntimeException("DEX cache was not writeable.");
		}
	}

	@Override
	public final boolean isProxy(Object object) {
		return ProxyBuilder.isProxyClass(object.getClass());
	}

	@Override
	public final InvocationHandler getInvocationHandler(Object proxy) {
		if (!isProxy(proxy))
			return null;
		return ProxyBuilder.getInvocationHandler(proxy);
	}

	@Override
	public final ProxyType getProxyType() {
		return ProxyType.DexMaker;
	}

}
