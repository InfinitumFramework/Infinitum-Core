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
     * @param contextType the {@link InfinitumContext} type to proxy
     */
    public InfinitumContextProxy(Class<? extends InfinitumContext> contextType) {
        super(null, new Class<?>[]{contextType});
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
