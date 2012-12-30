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

package com.clarionmedia.infinitum.logging.impl;

import android.util.Log;

import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.logging.Logger;

/**
 * <p>
 * Implementation of {@link Logger} for configured logging to Logcat.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 04/10/12
 * @since 1.0
 */
public class LoggerImpl extends Logger {

	private String mTag;
	private InfinitumContext mContext;

	/**
	 * Constructs a new {@code LoggerImpl} with the given tag.
	 * 
	 * @param context
	 *            the {@link InfinitumContext} to use
	 * @param tag
	 *            the tag to assign to this {@code LoggerImpl}
	 */
	public LoggerImpl(InfinitumContext context, String tag) {
		mContext = context;
		mTag = tag;
	}

	@Override
	public void debug(String msg) {
		if (mContext.isDebug())
			Log.d(mTag, msg);
	}

	@Override
	public void debug(String msg, Throwable tr) {
		if (mContext.isDebug())
			Log.d(mTag, msg, tr);
	}

	@Override
	public void error(String msg) {
		if (mContext.isDebug())
			Log.e(mTag, msg);
	}

	@Override
	public void error(String msg, Throwable tr) {
		if (mContext.isDebug())
			Log.e(mTag, msg, tr);
	}

	@Override
	public void info(String msg) {
		if (mContext.isDebug())
			Log.i(mTag, msg);
	}

	@Override
	public void info(String msg, Throwable tr) {
		if (mContext.isDebug())
			Log.i(mTag, msg, tr);
	}

	@Override
	public void verbose(String msg) {
		if (mContext.isDebug())
			Log.v(mTag, msg);
	}

	@Override
	public void verbose(String msg, Throwable tr) {
		if (mContext.isDebug())
			Log.v(mTag, msg, tr);
	}

	@Override
	public void warn(String msg) {
		if (mContext.isDebug())
			Log.w(mTag, msg);
	}

	@Override
	public void warn(String msg, Throwable tr) {
		if (mContext.isDebug())
			Log.w(mTag, msg, tr);
	}

	@Override
	public String getTag() {
		return mTag;
	}

	@Override
	public void setTag(String tag) {
		mTag = tag;
	}

}
