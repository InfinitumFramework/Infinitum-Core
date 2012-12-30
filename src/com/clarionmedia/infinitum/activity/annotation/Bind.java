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
 * @see InjectView
 * @since 1.0
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
