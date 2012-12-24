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
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import com.clarionmedia.infinitum.aop.JdkDynamicProxy;
import com.clarionmedia.infinitum.aop.JoinPoint;
import com.clarionmedia.infinitum.aop.Pointcut;
import com.clarionmedia.infinitum.aop.ProceedingJoinPoint;
import com.clarionmedia.infinitum.internal.Preconditions;

/**
 * <p>
 * Implementation of {@link JdkDynamicProxy} that provides AOP advice support
 * for proxies based on the JDK's {@link Proxy}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/14/12
 * @since 1.0
 */
public final class AdvisedJdkDynamicProxy extends JdkDynamicProxy {

	private List<JoinPoint> mBeforeAdvice;
	private List<JoinPoint> mAfterAdvice;
	private ProceedingJoinPoint mAroundAdvice;
	private Pointcut mPointcut;

	/**
	 * Creates a new {@code AdvisedJdkDynamicProxy}.
	 * 
	 * @param target
	 *            the proxied {@link Object}
	 * @param pointcut
	 *            the {@link Pointcut} to provide advice
	 * @param interfaces
	 *            the interfaces the proxy will implement
	 */
	public AdvisedJdkDynamicProxy(Object target, Pointcut pointcut,
			Class<?>[] interfaces) {
		super(target, interfaces);
		Preconditions.checkNotNull(pointcut);
		mPointcut = pointcut;
		mBeforeAdvice = new ArrayList<JoinPoint>();
		mAfterAdvice = new ArrayList<JoinPoint>();
		ProceedingJoinPoint next = null;
		for (JoinPoint joinPoint : pointcut.getJoinPoints()) {
			switch (joinPoint.getLocation()) {
				case Before :
					mBeforeAdvice.add(joinPoint);
					break;
				case After :
					mAfterAdvice.add(joinPoint);
					break;
				case Around :
					ProceedingJoinPoint proceedingJoinPoint = (ProceedingJoinPoint) joinPoint;
					if (next != null)
						proceedingJoinPoint.setNext(next);
					next = proceedingJoinPoint;
					break;
			}
		}
		mAroundAdvice = next;

	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		for (JoinPoint joinPoint : mBeforeAdvice) {
			if (applies(joinPoint, method)) {
				joinPoint.setMethod(method);
				joinPoint.setArguments(args);
				joinPoint.invoke();
			}
		}
		Object ret;
		if (mAroundAdvice == null || !applies(mAroundAdvice, method)) {
			ret = method.invoke(mTarget, args);
		} else {
			mAroundAdvice.setMethod(method);
			mAroundAdvice.setArguments(args);
			ret = mAroundAdvice.invoke();
		}
		for (JoinPoint joinPoint : mAfterAdvice) {
			if (applies(joinPoint, method)) {
				joinPoint.setMethod(method);
				joinPoint.setArguments(args);
				joinPoint.invoke();
			}
		}
		return ret;
	}
	
	@Override
	public AdvisedJdkDynamicProxy clone() {
		return new AdvisedJdkDynamicProxy(mTarget, mPointcut, mInterfaces);
	}

}
