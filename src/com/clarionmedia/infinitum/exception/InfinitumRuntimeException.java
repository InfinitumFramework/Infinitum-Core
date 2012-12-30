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

package com.clarionmedia.infinitum.exception;

/**
 * <p>
 * Indicates an unchecked exception occurred within the Infinitum runtime.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 02/15/12
 * @since 1.0
 */
public class InfinitumRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -2477796900040902595L;

	/**
	 * Constructs a new {@code InfinitumRuntimeException} with the given error
	 * message.
	 * 
	 * @param error
	 *            the error message for the {@link RuntimeException}
	 */
	public InfinitumRuntimeException(String error) {
		super(error);
	}

	/**
	 * Constructs a new {@code InfinitumRuntimeException} with the given error
	 * message.
	 * 
	 * @param error
	 *            the error message for the {@link RuntimeException}
	 * @param throwable
	 *            the cause of this exception
	 */
	public InfinitumRuntimeException(String error, Throwable throwable) {
		super(error, throwable);
	}

}
