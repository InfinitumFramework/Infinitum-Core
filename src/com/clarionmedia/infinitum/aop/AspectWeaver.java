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

import java.util.Set;

import android.content.Context;

/**
 * <p>
 * Responsible for generating advice-woven bytecode.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/12/12
 * @since 1.0
 */
public interface AspectWeaver {

	/**
	 * Weaves the advice from the given aspects into the framework-managed
	 * components and generates bytecode for them.
	 * 
	 * @param context
	 *            the {@link Context} used to retrieve the bytecode cache from
	 * @param aspects
	 *            the aspects to weave
	 */
	void weave(Context context, Set<Class<?>> aspects);

}
