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

import java.lang.reflect.Method;
import com.clarionmedia.infinitum.aop.impl.BasicJoinPoint;
import com.clarionmedia.infinitum.aop.impl.BasicProceedingJoinPoint;
import com.clarionmedia.infinitum.aop.annotation.Aspect;
import com.clarionmedia.infinitum.internal.Preconditions;

/**
 * <p>
 * Abstract implementation of {@link JoinPoint}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/14/12
 * @since 1.0
 * @see BasicJoinPoint
 * @see BasicProceedingJoinPoint
 */
public abstract class AbstractJoinPoint implements JoinPoint {

	protected Object mTarget;
	protected Method mMethod;
	protected Object[] mArguments;
	protected String mBeanName;
	protected boolean mIsClassScope;
	protected Method mAdvice;
	protected Object mAdvisor;
	protected int mOrder;

	/**
	 * Creates a new {@code AbstractJoinPoint}.
	 * 
	 * @param advisor
	 *            the {@link Aspect} containing the advice to apply
	 * @param advice
	 *            the advice {@link Method} to apply at this {link JoinPoint}
	 */
	public AbstractJoinPoint(Object advisor, Method advice) {
		Preconditions.checkNotNull(advisor);
		Preconditions.checkNotNull(advice);
		mAdvisor = advisor;
		mAdvice = advice;
	}

	/**
	 * Creates a new {@code AbstractJoinPoint} by copying from the given
	 * {@code AbstractJoinPoint}.
	 * 
	 * @param joinPoint
	 *            the {@code AbstractJoinPoint} to copy
	 */
	public AbstractJoinPoint(AbstractJoinPoint joinPoint) {
		mAdvisor = joinPoint.mAdvisor;
		mAdvice = joinPoint.mAdvice;
		mArguments = joinPoint.mArguments;
		mBeanName = joinPoint.mBeanName;
		mIsClassScope = joinPoint.mIsClassScope;
		mMethod = joinPoint.mMethod;
		mOrder = joinPoint.mOrder;
		mTarget = joinPoint.mTarget;
	}

	@Override
	public Object getAdvisor() {
		return mAdvisor;
	}

	@Override
	public void setAdvisor(Object advisor) {
		mAdvisor = advisor;
	}

	@Override
	public Method getAdvice() {
		return mAdvice;
	}

	@Override
	public void setAdvice(Method advice) {
		mAdvice = advice;
	}

	@Override
	public int getOrder() {
		return mOrder;
	}

	@Override
	public void setOrder(int order) {
		mOrder = order;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if ((object == null)
				|| (!getClass().isAssignableFrom(object.getClass())))
			return false;
		AbstractJoinPoint other = (AbstractJoinPoint) object;
		return other.mIsClassScope == mIsClassScope
				&& other.mAdvice.equals(mAdvice)
				&& other.mAdvisor.equals(mAdvisor)
				&& other.mArguments.equals(mArguments)
				&& other.mBeanName.equals(mBeanName)
				&& other.mMethod.equals(mMethod)
				&& other.mTarget.equals(mTarget);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash *= 31 + (mTarget == null ? 0 : mTarget.hashCode());
		hash *= 31 + (mMethod == null ? 0 : mMethod.hashCode());
		hash *= 31 + (mArguments == null ? 0 : mArguments.hashCode());
		hash *= 31 + (mBeanName == null ? 0 : mBeanName.hashCode());
		hash *= 31 + (mIsClassScope ? 1 : 0);
		hash *= 31 + (mAdvice == null ? 0 : mAdvice.hashCode());
		hash *= 31 + (mAdvisor == null ? 0 : mAdvisor.hashCode());
		return hash;
	}
}
