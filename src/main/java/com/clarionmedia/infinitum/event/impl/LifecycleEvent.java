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

import com.clarionmedia.infinitum.event.EventPublisher;
import com.clarionmedia.infinitum.event.AbstractEvent;

/**
 * <p>
 * Concrete implementation of {@link AbstractEvent} signifying an event
 * triggered during an {@link Activity} or {@link Fragment} lifecycle hook.
 * </p>
 * 
 * @author Tyler
 * @version 1.0 01/13/13
 * @since 1.0
 */
public class LifecycleEvent extends AbstractEvent {

	public static enum LifecycleHook {
		ON_CREATE, ON_START, ON_RESUME, ON_PAUSE, ON_STOP, ON_DESTROY, ON_RESTART, ON_ATTACH, ON_CREATE_VIEW, ON_ACTIVITY_CREATED, ON_VIEW_STATE_RESTORED, ON_DESTROY_VIEW, ON_DETACH
	}

	private LifecycleHook mLifecycleHook;
	private EventPublisher mEventPublisher;

	/**
	 * Creates a new {@code LifecycleEvent} instance.
	 * 
	 * @param publisher
	 *            the {@link EventPublisher} publishing the event
	 * @param hook
	 *            the {@code LifecycleHook} the event is being published from
	 */
	public LifecycleEvent(EventPublisher publisher, LifecycleHook hook) {
		super(hook.name(), hook, null);
		mLifecycleHook = hook;
		mEventPublisher = publisher;
	}

	@Override
	public EventPublisher getPublisher() {
		return mEventPublisher;
	}

	/**
	 * Returns the {@code LifecycleHook} associated with this event.
	 * 
	 * @return {@code LifecycleHook}
	 */
	public LifecycleHook getLifecycleHook() {
		return mLifecycleHook;
	}

}
