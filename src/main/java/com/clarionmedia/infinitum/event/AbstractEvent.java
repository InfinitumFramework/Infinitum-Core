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

package com.clarionmedia.infinitum.event;

import android.app.Activity;
import com.clarionmedia.infinitum.context.InfinitumContext;

import java.util.Map;

/**
 * <p>
 * Abstract representation of an event that has been triggered by the framework
 * or on behalf of the framework. A {@code FrameworkEvent} may be fired off from
 * an {@link Activity} lifecycle hook, such as {@code onCreate}, from the
 * invocation of a method annotated with {@link com.clarionmedia.infinitum.event.annotation.Event}, or manually by
 * calling {@link InfinitumContext#publishEvent(AbstractEvent)}.
 * </p>
 *
 * @author Tyler Treat
 * @version 1.0.4 03/13/13
 * @see com.clarionmedia.infinitum.event.annotation.Event
 * @since 1.0
 */
public abstract class AbstractEvent {

    protected Object mPublisher;
    protected String mName;
    protected Map<String, Object> mPayload;

    /**
     * Creates a new {@code FrameworkEvent} instance.
     *
     * @param name      the name of the event
     * @param publisher the publisher of the event
     * @param payload   the payload, if any, of the event
     */
    public AbstractEvent(String name, Object publisher, Map<String, Object> payload) {
        mName = name;
        mPublisher = publisher;
        mPayload = payload;
    }

    public Object getPublisher() {
        return mPublisher;
    }

    public String getName() {
        return mName;
    }

    public boolean containsPayloadValue(String name) {
        return mPayload.containsKey(name);
    }

    public Object getPayloadValue(String name) {
        return mPayload.get(name);
    }

}
