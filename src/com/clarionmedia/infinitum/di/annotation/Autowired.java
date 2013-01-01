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
import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.di.BeanPostProcessor;
import com.clarionmedia.infinitum.context.exception.InfinitumConfigurationException;

/**
 * <p>
 * Indicates that the annotated constructor, setter, or field is to be autowired
 * by Infinitum.
 * </p>
 * <p>
 * If a constructor is marked with this annotation, it will be used to
 * initialize the bean. Only one such constructor can carry this annotation, and
 * it does not have to be public.
 * </p>
 * <p>
 * Fields and methods will be autowired immediately after all bean
 * initialization has completed using a {@link BeanPostProcessor}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/05/12
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD})
public @interface Autowired {

	/**
	 * Declares the qualifier indicating the name of the bean to autowire. If
	 * this is not defined, Infinitum will inject a candidate bean specified in
	 * the {@link InfinitumContext}. If there is more than one candidate bean,
	 * an {@link InfinitumConfigurationException} will be thrown.
	 * 
	 * @return the name of the bean to inject
	 */
	String value() default "";

}
