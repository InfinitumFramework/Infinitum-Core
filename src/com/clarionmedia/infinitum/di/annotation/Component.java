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

package com.clarionmedia.infinitum.di.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Indicates that the annotated {@link Class} is a framework component, meaning
 * it is a candidate for auto-detection by the framework if classpath scanning
 * is enabled.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/11/12
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {

	/**
	 * Declares the {@Component} name to be used.
	 * 
	 * @return the suggested {@Component} name
	 */
	String value() default "";

}
