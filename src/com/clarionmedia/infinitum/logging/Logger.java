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

package com.clarionmedia.infinitum.logging;

import android.app.Activity;

import com.clarionmedia.infinitum.context.InfinitumContext;
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
	 * @param context
	 *            the {@link InfinitumContext} to use for this {@code Logger}
	 * @param tag
	 *            the tag to assign to the {@code Logger}
	 * @return {@code Logger} instance
	 */
	public static Logger getInstance(InfinitumContext context, String tag) {
		return new LoggerImpl(context, tag);
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
