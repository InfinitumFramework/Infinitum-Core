/*
 * Copyright (C) 2012 Clarion Media, LLC
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

package com.clarionmedia.infinitum.logging;

import android.app.Activity;

import com.clarionmedia.infinitum.logging.impl.LoggerImpl;

/**
 * <p>
 * Prints log messages to Logcat but adheres to environment configuration.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 04/10/12
 * @since 1.0
 */
public abstract class Logger {

	/**
	 * Retrieves a new {@code Logger} instance.
	 * 
	 * @param tag
	 *            the tag to assign to the {@code Logger}
	 * @return {@code Logger} instance
	 */
	public static Logger getInstance(String tag) {
		return new LoggerImpl(tag);
	}

	/**
	 * Prints a log message at the DEBUG level.
	 * 
	 * @param msg
	 *            the message to log
	 */
	public abstract void debug(String msg);

	/**
	 * Prints a log message at the DEBUG level.
	 * 
	 * @param msg
	 *            the message to log
	 * @param tr
	 *            the {@link Throwable} to log
	 */
	public abstract void debug(String msg, Throwable tr);

	/**
	 * Prints a log message at the ERROR level.
	 * 
	 * @param msg
	 *            the message to log
	 */
	public abstract void error(String msg);

	/**
	 * Prints a log message at the ERROR level.
	 * 
	 * @param msg
	 *            the message to log
	 * @param tr
	 *            the {@link Throwable} to log
	 */
	public abstract void error(String msg, Throwable tr);

	/**
	 * Prints a log message at the INFO level.
	 * 
	 * @param msg
	 *            the message to log
	 */
	public abstract void info(String msg);

	/**
	 * Prints a log message at the INFO level.
	 * 
	 * @param msg
	 *            the message to log
	 * @param tr
	 *            the {@link Throwable} to log
	 */
	public abstract void info(String msg, Throwable tr);

	/**
	 * Prints a log message at the VERBOSE level.
	 * 
	 * @param msg
	 *            the message to log
	 */
	public abstract void verbose(String msg);

	/**
	 * Prints a log message at the VERBOSE level.
	 * 
	 * @param msg
	 *            the message to log
	 * @param tr
	 *            the {@link Throwable} to log
	 */
	public abstract void verbose(String msg, Throwable tr);

	/**
	 * Prints a log message at the WARN level.
	 * 
	 * @param msg
	 *            the message to log
	 */
	public abstract void warn(String msg);

	/**
	 * Prints a log message at the WARN level.
	 * 
	 * @param msg
	 *            the message to log
	 * @param tr
	 *            the {@link Throwable} to log
	 */
	public abstract void warn(String msg, Throwable tr);

	/**
	 * Returns the tag assigned to this {@code Logger}. The tag is used to
	 * identify where a log message originated from. Typically, it is the name
	 * of the {@link Activity} or class in which the {@code Logger} was called.
	 * 
	 * @return {@code Logger} tag
	 */
	public abstract String getTag();

	/**
	 * Sets the tag assigned to this {@code Logger}. The tag is used to identify
	 * where a log message originated from. Typically, it is the name of the
	 * {@link Activity} or class in which the {@code Logger} was called.
	 * 
	 * @param tag
	 *            the tag to assign
	 */
	public abstract void setTag(String tag);

}
