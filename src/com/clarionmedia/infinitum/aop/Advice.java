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

package com.clarionmedia.infinitum.aop;

import java.lang.annotation.Annotation;

import com.clarionmedia.infinitum.aop.JoinPoint.AdviceLocation;
import com.clarionmedia.infinitum.aop.annotation.After;
import com.clarionmedia.infinitum.aop.annotation.Around;
import com.clarionmedia.infinitum.aop.annotation.Before;

/**
 * <p>
 * Wrapper for advice annotations {@link Before}, {@link After}, and
 * {@link Around}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/16/12
 * @since 1.0
 */
public class Advice {

	private Object mAdvice;

	/**
	 * Creates a new {@code Advice}.
	 * 
	 * @param advice
	 *            the advice {@link Annotation}
	 */
	public Advice(Annotation advice) {
		mAdvice = advice;
	}

	/**
	 * Returns the {@code Advice} {@link AdviceLocation}.
	 * 
	 * @return {@code Advice} {@code Location}
	 */
	public AdviceLocation getLocation() {
		if (Before.class.isAssignableFrom(mAdvice.getClass()))
			return AdviceLocation.Before;
		if (After.class.isAssignableFrom(mAdvice.getClass()))
			return AdviceLocation.After;
		if (Around.class.isAssignableFrom(mAdvice.getClass()))
			return AdviceLocation.Around;
		return null;
	}

	/**
	 * Returns the advice {@link Annotation} type.
	 * 
	 * @return advice type
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends Annotation> getAdviceType() {
		return (Class<? extends Annotation>) mAdvice.getClass();
	}

	/**
	 * Indicates if the {@code Advice} is an {@link Around} type.
	 * 
	 * @return {@code true} if it is {@code Around} type, {@code false} if not
	 */
	public boolean isAround() {
		if (Around.class.isAssignableFrom(mAdvice.getClass()))
			return true;
		return false;
	}

	/**
	 * Declares the beans and, optionally, specific methods which make up a
	 * pointcut.
	 * 
	 * @return array of bean names, which may or may not include specific
	 *         methods to create a pointcut
	 */
	public String[] beans() {
		if (Before.class.isAssignableFrom(mAdvice.getClass()))
			return ((Before) mAdvice).beans();
		if (After.class.isAssignableFrom(mAdvice.getClass()))
			return ((After) mAdvice).beans();
		if (Around.class.isAssignableFrom(mAdvice.getClass()))
			return ((Around) mAdvice).beans();
		return null;
	}

	/**
	 * Declares the packages such that any contained type's methods make up a
	 * pointcut.
	 * 
	 * @return array of package names to create a pointcut
	 */
	public String[] within() {
		if (Before.class.isAssignableFrom(mAdvice.getClass()))
			return ((Before) mAdvice).within();
		if (After.class.isAssignableFrom(mAdvice.getClass()))
			return ((After) mAdvice).within();
		if (Around.class.isAssignableFrom(mAdvice.getClass()))
			return ((Around) mAdvice).within();
		return null;
	}

	/**
	 * Declares the advice precedence. A smaller number indicates a higher
	 * precedence, while a larger number indicates a lower precedence. The
	 * default value is {@link Integer#MAX_VALUE}. The precedence determines the
	 * order in which advice is executed.
	 * 
	 * @return the advice precedence
	 */
	public int order() {
		if (Before.class.isAssignableFrom(mAdvice.getClass()))
			return ((Before) mAdvice).order();
		if (After.class.isAssignableFrom(mAdvice.getClass()))
			return ((After) mAdvice).order();
		if (Around.class.isAssignableFrom(mAdvice.getClass()))
			return ((Around) mAdvice).order();
		return Integer.MAX_VALUE;
	}

}
