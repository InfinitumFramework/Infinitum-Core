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

package com.clarionmedia.infinitum.aop.impl;

import java.lang.reflect.Method;

import com.clarionmedia.infinitum.aop.AbstractJoinPoint;
import com.clarionmedia.infinitum.aop.ProceedingJoinPoint;
import com.clarionmedia.infinitum.aop.annotation.Aspect;
import com.clarionmedia.infinitum.internal.Preconditions;

/**
 * <p>
 * Basic implementation of {@link ProceedingJoinPoint}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/14/12
 * @since 1.0
 */
public class BasicProceedingJoinPoint extends AbstractJoinPoint implements ProceedingJoinPoint {

	private ProceedingJoinPoint mNext;

	/**
	 * Creates a new {@code BasicProceedingJoinPoint}.
	 * 
	 * @param advisor
	 *            the {@link Aspect} containing the advice to apply
	 * @param advice
	 *            the advice {@link Method} to apply at this {link JoinPoint}
	 */
	public BasicProceedingJoinPoint(Object advisor, Method advice) {
		super(advisor, advice);
	}

	/**
	 * Creates a new {@code BasicProceedingJoinPoint} by copying from the given
	 * {@code BasicProceedingJoinPoint}.
	 * 
	 * @param joinPoint
	 *            the {@code BasicProceedingJoinPoint} to copy
	 */
	public BasicProceedingJoinPoint(BasicProceedingJoinPoint joinPoint) {
		super(joinPoint.mAdvisor, joinPoint.mAdvice);
		mNext = joinPoint.mNext;
	}

	@Override
	public Method getMethod() {
		return mMethod;
	}

	@Override
	public Object[] getArguments() {
		return mArguments;
	}

	@Override
	public Object getTarget() {
		return mTarget;
	}

	@Override
	public Class<?> getTargetType() {
		if (mTarget == null)
			return null;
		return mTarget.getClass();
	}

	@Override
	public void setBeanName(String beanName) {
		mBeanName = beanName;
	}

	@Override
	public String getBeanName() {
		return mBeanName;
	}

	@Override
	public void setMethod(Method method) {
		mMethod = method;
		ProceedingJoinPoint next = next();
		if (next != null)
			next.setMethod(method);
	}

	@Override
	public void setArguments(Object[] args) {
		mArguments = args;
		ProceedingJoinPoint next = next();
		if (next != null)
			next.setArguments(args);
	}

	@Override
	public void setTarget(Object target) {
		mTarget = target;
	}

	@Override
	public boolean isClassScope() {
		return mIsClassScope;
	}

	@Override
	public void setClassScope(boolean isClassScope) {
		mIsClassScope = isClassScope;
	}

	@Override
	public AdviceLocation getLocation() {
		return AdviceLocation.Around;
	}

	@Override
	public void setLocation(AdviceLocation location) {
		// Does this even make sense?
	}

	@Override
	public Object invoke() throws Exception {
		Preconditions.checkNotNull(mAdvisor);
		Preconditions.checkNotNull(mAdvice);
		return mAdvice.invoke(mAdvisor, this);
	}

	@Override
	public Object proceed() throws Exception {
		ProceedingJoinPoint next = next();
		if (next == null)
			return mMethod.invoke(mTarget, mArguments);
		return next.invoke();
	}

	@Override
	public void setNext(ProceedingJoinPoint next) {
		mNext = next;
	}

	@Override
	public ProceedingJoinPoint next() {
		return mNext;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if ((object == null) || (object.getClass() != getClass()))
			return false;
		if (object.getClass() != this.getClass())
			return false;
		BasicProceedingJoinPoint other = (BasicProceedingJoinPoint) object;
		return other.mNext == other.mNext && super.equals(other);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash *= 31 + (mNext == null ? 0 : mNext.hashCode());
		hash *= 31 + super.hashCode();
		return hash;
	}

}
