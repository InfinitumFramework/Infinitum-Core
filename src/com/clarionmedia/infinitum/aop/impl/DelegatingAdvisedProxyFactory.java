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

package com.clarionmedia.infinitum.aop.impl;

import android.content.Context;
import com.clarionmedia.infinitum.aop.AopProxy;
import com.clarionmedia.infinitum.aop.Pointcut;
import com.clarionmedia.infinitum.aop.AdvisedProxyFactory;

/**
 * <p>
 * {@link AdvisedProxyFactory} which creates {@link AopProxy} instances by
 * determining the best implementation to use.
 * {@code DelegatingAdvisedProxyFactory} will use {@link AdvisedDexMakerProxy}
 * to proxy non-final classes and {@link AdvisedJdkDynamicProxy} to proxy
 * interfaces.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/14/12
 * @since 1.0
 */
public class DelegatingAdvisedProxyFactory implements AdvisedProxyFactory {

	@Override
	public AopProxy createProxy(Context context, Object object,
			Pointcut pointcut) {
		Class<?> clazz = object.getClass();
		Class<?>[] interfaces = clazz.getInterfaces();
		if (interfaces.length > 0)
			return new AdvisedJdkDynamicProxy(object, pointcut, interfaces);
		return new AdvisedDexMakerProxy(context, object, pointcut);
	}

}
