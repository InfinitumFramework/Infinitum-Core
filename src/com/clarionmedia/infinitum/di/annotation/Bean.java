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
import com.clarionmedia.infinitum.di.BeanFactory;

/**
 * <p>
 * Specialization of the {@link Component} annotation indicating that the
 * annotated {@link Class} is a dependency-injection bean, meaning it is a
 * candidate for auto-detection by the framework if classpath scanning is
 * enabled.
 * </p>
 * <p>
 * {@code Beans} that are picked up during auto-detection are registered with
 * the {@link InfinitumContext} and stored in its {@link BeanFactory}. If a bean
 * name is not suggested, the bean will be registered using the camelcase
 * version of its {@code Class} name. For example, an annotated {@code Class}
 * {@code FooBar} will use the bean name {@code fooBar} unless otherwise
 * specified.
 * </p>
 *
 * @author Tyler Treat
 * @version 1.0 07/11/12
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Bean {

    /**
     * Declares the {@code Bean} name to be used.
     *
     * @return the suggested {@code Bean} name
     */
    String value() default "";

}
