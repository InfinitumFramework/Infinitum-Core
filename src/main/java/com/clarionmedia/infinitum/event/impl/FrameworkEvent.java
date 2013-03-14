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

package com.clarionmedia.infinitum.event.impl;

import com.clarionmedia.infinitum.event.AbstractEvent;

import java.util.Map;

/**
 * <p>
 * Concrete implementation of {@link AbstractEvent} signifying an event
 * triggered by the invocation of a method annotated with {@link com.clarionmedia.infinitum.event.annotation.Event}.
 * </p>
 *
 * @author Tyler
 * @version 1.0 01/13/13
 * @see com.clarionmedia.infinitum.event.annotation.Event
 * @since 1.0
 */
public class FrameworkEvent extends AbstractEvent {

    /**
     * Creates a new {@code FrameworkEvent} instance.
     *
     * @param name      the name of the event
     * @param publisher the publisher of the event
     * @param payload   the payload, if any, of the event
     */
    public FrameworkEvent(String name, Object publisher, Map<String, Object> payload) {
        super(name, publisher, payload);
    }

}
