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

package com.clarionmedia.infinitum.aop;

import android.content.Context;

/**
 * <p>
 * Factory for creating {@link AopProxy} instances.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/14/12
 * @since 1.0
 */
public interface AdvisedProxyFactory {

	/**
	 * Creates a new {@link AopProxy} for the given {@link Object}.
	 * 
	 * @param context
	 *            the {@link Context} used to retrieve the DEX bytecode cache if
	 *            it's needed
	 * @param object
	 *            the {@code Object} to proxy
	 * @param pointcut
	 *            the {@link Pointcut} containing advice
	 * @return {@code AopProxy}
	 */
	AopProxy createProxy(Context context, Object object, Pointcut pointcut);

}
