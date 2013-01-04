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
