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

package com.clarionmedia.infinitum.activity.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.clarionmedia.infinitum.di.ActivityInjector.Event;
import android.view.KeyEvent;
import android.view.View;

/**
 * <p>
 * Indicates that the annotated {@link View} is to be bound to a callback method
 * for a given event type.
 * </p>
 * <p>
 * The {@code event} attribute corresponds to the event type, while
 * {@code callback} indicates the name of the method to be invoked. If an
 * {@link Event} is not specified, {@code Event.OnClick} is used by default.
 * </p>
 * <p>
 * The callback method should match the normal listener method in terms of its
 * return type and arguments. For example, an {@code onClick} callback should be
 * {@code void} and take a {@code View} as its argument, while an {@code onKey}
 * callback must return a {@code boolean} and take a {@code View}, {@code int},
 * and {@link KeyEvent} as its arguments.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/19/12
 * @since 1.0
 * @see InjectView
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Bind {

	/**
	 * Declares the event type for this binding. Defaults to
	 * {@code Event.OnClick}.
	 * 
	 * @return {@link Event} binding
	 */
	Event event() default Event.OnClick;

	/**
	 * Declares the name of the method to invoked.
	 * 
	 * @return method name
	 */
	String value();

}
