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

/**
 * <p>
 * Provides support for around advice by exposing the
 * {@link ProceedingJoinPoint#proceed()} method.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/14/12
 * @since 1.0
 */
public interface ProceedingJoinPoint extends JoinPoint {

	/**
	 * Proceed with the next advice or target method invocation.
	 * 
	 * @return the value returned by the target method, if any
	 * @throws Exception
	 *             if the next advice or target method threw an exception
	 */
	Object proceed() throws Exception;

	/**
	 * Sets the subsequent {@link ProceedingJoinPoint} to invoke after this one.
	 * 
	 * @param next
	 *            the next {@code ProceedingJoinPoint}
	 */
	void setNext(ProceedingJoinPoint next);

	/**
	 * Returns the subsequent {@link PRoceedingJoinPoint} to be invoked after
	 * this one.
	 * 
	 * @return the next {@code ProceedingJoinPoint}
	 */
	ProceedingJoinPoint next();

}
