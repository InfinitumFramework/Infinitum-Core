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
