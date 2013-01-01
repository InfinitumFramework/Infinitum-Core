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

package com.clarionmedia.infinitum.di;

import android.app.Activity;
import android.view.View;

/**
 * <p>
 * Responsible for injecting an {@link Activity} with Android resources and
 * framework components.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/18/12
 * @since 1.0
 */
public interface ActivityInjector {

	/**
	 * Defines the type of input event that occurs on a {@link View}.
	 */
	public static enum Event {
		OnClick, OnLongClick, OnCreateContextMenu, OnFocusChange, OnKey, OnTouch
	};

	/**
	 * Injects the appropriate resources and components into any annotated
	 * fields.
	 */
	void inject();

}
