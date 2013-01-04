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

package com.clarionmedia.infinitum.context.impl;

import java.lang.reflect.Method;

import com.clarionmedia.infinitum.context.ContextFactory;
import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.di.AbstractProxy;
import com.clarionmedia.infinitum.di.JdkDynamicProxy;

/**
 * <p>
 * {@link JdkDynamicProxy} used to proxy {@link InfinitumContext} such that the
 * context is lazily loaded. This is used by the framework as a context
 * placeholder to avoid "Infinitum context not configured" issues during
 * framework initialization.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 12/28/12
 * @since 1.0
 */
public class InfinitumContextProxy extends JdkDynamicProxy {

	private InfinitumContext mProxiedContext;
	private Class<? extends InfinitumContext> mContextType;

	/**
	 * Creates a new {@code InfinitumContextProxy} instance.
	 * 
	 * @param contextType
	 *            the {@link InfinitumContext} type to proxy
	 */
	public InfinitumContextProxy(Class<? extends InfinitumContext> contextType) {
		super(null, new Class<?>[] { contextType });
		mContextType = contextType;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (mProxiedContext == null)
			mProxiedContext = ContextFactory.newInstance().getContext(mContextType);
		return method.invoke(mProxiedContext, args);
	}

	@Override
	public AbstractProxy clone() {
		throw new UnsupportedOperationException("Clone is not supported for InfinitumContextProxy!");
	}

}
