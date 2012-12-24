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

package com.clarionmedia.infinitum.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.clarionmedia.infinitum.aop.JoinPoint;
import com.clarionmedia.infinitum.aop.ProceedingJoinPoint;

/**
 * <p>
 * Indicates that the annotated advice is to be executed around a
 * {@link JoinPoint}. A specialized {@link ProceedingJoinPoint} will be passed
 * as an argument to methods annotated with this.
 * </p>
 * <p>
 * {@code Around} advice has the ability to prevent a {@code JoinPoint} from
 * being executed by returning its own return value or throwing an exception.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/12/12
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Around {

	/**
	 * Declares the beans and, optionally, specific methods which make up a
	 * pointcut.
	 * 
	 * @return array of bean names, which may or may not include specific
	 *         methods to create a pointcut
	 */
	String[] beans() default {};

	/**
	 * Declares the packages such that any contained type's methods make up a
	 * pointcut.
	 * 
	 * @return array of package names to create a pointcut
	 */
	String[] within() default {};
	
	/**
	 * Declares the advice precedence. A smaller number indicates a higher
	 * precedence, while a larger number indicates a lower precedence. The
	 * default value is {@link Integer#MAX_VALUE}. The precedence determines the
	 * order in which advice is executed.
	 * 
	 * @return the advice precedence
	 */
	int order() default Integer.MAX_VALUE;

}
