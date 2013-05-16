/*
 * Copyright (C) 2013 Clarion Media, LLC
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

import com.clarionmedia.infinitum.context.ContextFactory;
import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.context.impl.InfinitumContextProxy;
import com.clarionmedia.infinitum.context.impl.XmlApplicationContext;
import com.clarionmedia.infinitum.logging.Logger;

/**
 * <p> Implementation of {@link Logger} for configured logging to Logcat. {@code SmartLogger} adheres to environment
 * configurations, meaning it will not log in "production" environments. </p>
 *
 * @author Tyler Treat
 * @version 1.1.0 04/25/13
 * @since 1.0
 */
public class SmartLogger extends Logger {

    private String mTag;
    private InfinitumContext mContext;

    /**
     * Constructs a new {@code SmartLogger} with the given tag.
     *
     * @param tag the tag to assign to this {@code SmartLogger}
     */
    public SmartLogger(String tag) {
        mTag = tag;
        mContext = (InfinitumContext) new InfinitumContextProxy(XmlApplicationContext.class,
                ContextFactory.getInstance()).getProxy();
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
