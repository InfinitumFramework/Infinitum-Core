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
import com.clarionmedia.infinitum.di.annotation.Autowired;
import android.app.Activity;

/**
 * <p>
 * Indicates that the annotated {@link Activity} is to be injected with an
 * Android layout. This is essentially a replacement for calling
 * {@link Activity#setContentView(int)} and is required in order for
 * {@code Activity} injection to function properly if {@code setContentView} is
 * not called explicitly before injection occurs.
 * </p>
 * <p>
 * The {@code value} attribute corresponds to the ID of the layout to inject.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/18/12
 * @since 1.0
 * @see Autowired
 * @see InjectResource
 * @see InjectView
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InjectLayout {

	/**
	 * Declares the ID of the Android layout to inject.
	 * 
	 * @return layout ID
	 */
	int value();

}
