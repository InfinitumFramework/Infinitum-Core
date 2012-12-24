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

import java.util.PriorityQueue;
import java.util.Queue;

import com.clarionmedia.infinitum.internal.Preconditions;

/**
 * <p>
 * Contains a set of {@link JoinPoint} instances and a {@link Class} which the
 * {@code JoinPoints} belong to.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/13/12
 * @since 1.0
 */
public class Pointcut {

	private static final int INITIAL_QUEUE_SIZE = 11;

	private Queue<JoinPoint> mJoinPoints;
	private String mBeanName;
	private Class<?> mClass;

	/**
	 * Creates a new {@code Pointcut}.
	 * 
	 * @param beanName
	 *            the name of the bean this {@code Pointcut} is for
	 * @param clazz
	 *            the type this {@code Pointcut} is for
	 */
	public Pointcut(String beanName, Class<?> clazz) {
		Preconditions.checkNotNull(beanName);
		Preconditions.checkNotNull(clazz);
		mJoinPoints = new PriorityQueue<JoinPoint>(INITIAL_QUEUE_SIZE, new JoinPoint.JoinPointComparator());
		mClass = clazz;
		mBeanName = beanName;
	}

	/**
	 * Returns the {@link Class} this {@code Pointcut} is associated with.
	 * 
	 * @return {@code Class}
	 */
	public Class<?> getPointcutType() {
		return mClass;
	}

	/**
	 * Sets the {@link Class} this {@code Pointcut} is associated with.
	 * 
	 * @param clazz
	 *            {@code Class}
	 */
	public void setPointcutType(Class<?> clazz) {
		mClass = clazz;
	}

	/**
	 * Returns the bean name this {@code Pointcut} is associated with.
	 * 
	 * @return bean name
	 */
	public String getBeanName() {
		return mBeanName;
	}

	/**
	 * Sets the bean name this {@code Pointcut} is associated with.
	 * 
	 * @param beanName
	 *            bean name
	 */
	public void setBeanName(String beanName) {
		mBeanName = beanName;
	}

	/**
	 * Returns the {@code Pointcut's} {@link JoinPoint} instances.
	 * 
	 * @return {@code JoinPoints}
	 */
	public Queue<JoinPoint> getJoinPoints() {
		return mJoinPoints;
	}

	/**
	 * Adds the given {@link JoinPoint} to the {@code Pointcut}.
	 * 
	 * @param joinPoint
	 *            the {@code JoinPoint} to add
	 */
	public void addJoinPoint(JoinPoint joinPoint) {
		mJoinPoints.add(joinPoint);
	}

	/**
	 * Removes the given {@link JoinPoint} from the {@code Pointcut} if it
	 * exists.
	 * 
	 * @param joinPoint
	 *            the {@code JoinPoint} to remove
	 */
	public void removeJoinPoint(JoinPoint joinPoint) {
		mJoinPoints.remove(joinPoint);
	}

}
