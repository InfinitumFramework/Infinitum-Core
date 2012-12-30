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
