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

package com.clarionmedia.infinitum.activity.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import com.clarionmedia.infinitum.di.annotation.Autowired;

/**
 * <p>
 * Indicates that the annotated {@link Field} is to be injected with an Android
 * resource.
 * </p>
 * <p>
 * The {@code value} attribute corresponds to the ID of the resource to inject.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/18/12
 * @since 1.0
 * @see Autowired
 * @see InjectLayout
 * @see InjectView
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InjectResource {

	/**
	 * Declares the ID of the Android resource to inject.
	 * 
	 * @return resource ID
	 */
	int value();

}
