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

package com.clarionmedia.infinitum.activity;

import com.clarionmedia.infinitum.event.EventPublisher;

public class LifecycleEvent {

	public static enum EventType {
		ON_CREATE, ON_START, ON_RESUME, ON_PAUSE, ON_STOP, ON_DESTROY, ON_RESTART, ON_ATTACH, ON_CREATE_VIEW, ON_ACTIVITY_CREATED, ON_VIEW_STATE_RESTORED, ON_DESTROY_VIEW, ON_DETACH
	}

	private EventPublisher mEventPublisher;
	private EventType mEventType;

	public LifecycleEvent(EventPublisher publisher, EventType type) {
		mEventPublisher = publisher;
		mEventType = type;
	}

	public EventPublisher getEventPublisher() {
		return mEventPublisher;
	}

	public EventType getEventType() {
		return mEventType;
	}

}
