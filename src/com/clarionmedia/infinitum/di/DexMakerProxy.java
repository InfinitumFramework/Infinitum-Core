/*
 * Copyright (c) 2012 Tyler Treat
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

import java.io.IOException;
import java.lang.reflect.InvocationHandler;

import android.content.Context;

import com.clarionmedia.infinitum.exception.InfinitumRuntimeException;
import com.clarionmedia.infinitum.internal.Preconditions;
import com.clarionmedia.infinitum.internal.caching.DexCaching;
import com.google.dexmaker.stock.ProxyBuilder;

/**
 * <p>
 * Abstract implementation of {@link AbstractProxy} that relies on DexMaker in order
 * to proxy non-final classes in addition to interfaces.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/14/12
 * @since 1.0
 * @see AdvisedDexMakerProxy
 * @see LazyLoadDexMakerProxy
 */
public abstract class DexMakerProxy extends AbstractProxy {

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
	 * Indicates if the given {@link Object} is an {@link AbstractProxy}.
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
