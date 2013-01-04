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
