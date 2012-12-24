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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;

import com.clarionmedia.infinitum.aop.Advice;
import com.clarionmedia.infinitum.aop.AdvisedProxyFactory;
import com.clarionmedia.infinitum.aop.AopProxy;
import com.clarionmedia.infinitum.aop.AspectWeaver;
import com.clarionmedia.infinitum.aop.JoinPoint;
import com.clarionmedia.infinitum.aop.Pointcut;
import com.clarionmedia.infinitum.aop.annotation.After;
import com.clarionmedia.infinitum.aop.annotation.Around;
import com.clarionmedia.infinitum.aop.annotation.Aspect;
import com.clarionmedia.infinitum.aop.annotation.Before;
import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.di.AbstractBeanDefinition;
import com.clarionmedia.infinitum.di.BeanFactory;
import com.clarionmedia.infinitum.exception.InfinitumRuntimeException;
import com.clarionmedia.infinitum.internal.CollectionUtil;
import com.clarionmedia.infinitum.reflection.ClassReflector;
import com.clarionmedia.infinitum.reflection.PackageReflector;
import com.clarionmedia.infinitum.reflection.impl.DefaultClassReflector;
import com.clarionmedia.infinitum.reflection.impl.DefaultPackageReflector;

/**
 * <p>
 * Implementation of {@link AspectWeaver} for processing aspects annotated with
 * {@link Aspect}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/12/12
 * @since 1.0
 */
public class AnnotationsAspectWeaver implements AspectWeaver {

	private ClassReflector mClassReflector;
	private PackageReflector mPackageReflector;
	private BeanFactory mBeanFactory;
	private AdvisedProxyFactory mProxyFactory;

	/**
	 * Creates a new {@code AnnotationsAspectWeaver} with the given
	 * {@link InfinitumContext}.
	 * 
	 * @param context
	 *            the {@code InfinitumContext} to retrieve beans from
	 */
	public AnnotationsAspectWeaver(InfinitumContext context) {
		mClassReflector = new DefaultClassReflector();
		mPackageReflector = new DefaultPackageReflector();
		mProxyFactory = new DelegatingAdvisedProxyFactory();
		mBeanFactory = context.getBeanFactory();
	}

	@Override
	public void weave(Context context, Set<Class<?>> aspects) {
		for (Pointcut pointcut : getPointcuts(aspects)) {
			String beanName = pointcut.getBeanName();
			Object bean = mBeanFactory.loadBean(beanName);
			AopProxy proxy = mProxyFactory.createProxy(context, bean, pointcut);
			mBeanFactory.getBeanDefinitions().get(beanName).setBeanProxy(proxy);
		}
	}

	// Build a Collection of Pointcuts for the given aspects
	private Collection<Pointcut> getPointcuts(Set<Class<?>> aspects) {
		Map<String, Pointcut> pointcutMap = new HashMap<String, Pointcut>();
		for (Class<?> aspect : aspects) {
			if (!aspect.isAnnotationPresent(Aspect.class))
				continue;
			Object advisor = mClassReflector.getClassInstance(aspect);
			// Process @Before advice
			processAdvice(advisor, Before.class, pointcutMap);
			// Process @After advice
			processAdvice(advisor, After.class, pointcutMap);
			// Process @Around advice
			processAdvice(advisor, Around.class, pointcutMap);
		}
		return pointcutMap.values();
	}

	// Processes advice indicated by @Before, @After, or @Around in an aspect
	private void processAdvice(Object advisor,
			Class<? extends Annotation> adviceType,
			Map<String, Pointcut> pointcutMap) {
		List<Method> methods = mClassReflector.getAllMethodsAnnotatedWith(
				advisor.getClass(), adviceType);
		for (Method adviceMethod : methods) {
			Advice advice = new Advice(adviceMethod.getAnnotation(adviceType));
			processBeanJoinPoints(advisor, advice, adviceMethod, pointcutMap);
			processWithinJoinPoints(advisor, advice, adviceMethod, pointcutMap);
		}
	}

	// Processes JoinPoints specified by the "beans" attribute
	// e.g. @Before(beans = { "fooBean", "barBean.method(*)" })
	private void processBeanJoinPoints(Object advisor, Advice advice,
			Method adviceMethod, Map<String, Pointcut> pointcutMap) {
		for (String bean : advice.beans()) {
			bean = bean.trim();
			if (bean.length() == 0)
				continue;
			String beanName = bean;
			boolean isClassScope = false;
			if (bean.contains("."))
				beanName = bean.substring(0, bean.indexOf('.'));
			else
				isClassScope = true;
			Object beanObject = mBeanFactory.loadBean(beanName);
			JoinPoint joinPoint = advice.isAround()
					? new BasicProceedingJoinPoint(advisor, adviceMethod)
					: new BasicJoinPoint(advisor, adviceMethod,
							advice.getLocation());
			joinPoint.setBeanName(beanName);
			joinPoint.setTarget(beanObject);
			joinPoint.setOrder(advice.order());
			if (isClassScope) {
				joinPoint.setClassScope(true);
				putJoinPoint(pointcutMap, joinPoint);
			} else {
				// It's a specific method or methods matcher
				processBeanMethodJoinPoint(bean, beanObject, advisor, advice,
						joinPoint, pointcutMap);
			}
		}
	}

	// Processes JoinPoints specified by the "within" attribute
	// e.g. @Around(within = {"com.foo.bar.service", "com.foo.bar.dao"})
	private void processWithinJoinPoints(Object advisor, Advice advice,
			Method adviceMethod, Map<String, Pointcut> pointcutMap) {
		for (String pkg : advice.within()) {
			pkg = pkg.toLowerCase().trim();
			if (pkg.length() == 0)
				continue;
			Map<AbstractBeanDefinition, String> invertedMap = CollectionUtil.invert(mBeanFactory.getBeanDefinitions());
			for (AbstractBeanDefinition bean : invertedMap.keySet()) {
				if (!bean.getType().getName().startsWith(pkg))
					continue;
				JoinPoint joinPoint = advice.isAround()
						? new BasicProceedingJoinPoint(advisor, adviceMethod)
						: new BasicJoinPoint(advisor, adviceMethod,
								advice.getLocation());
				joinPoint.setBeanName(bean.getName());
				joinPoint.setTarget(bean.getNonProxiedBeanInstance());
				joinPoint.setOrder(advice.order());
				joinPoint.setClassScope(true);
				putJoinPoint(pointcutMap, joinPoint);
			}
		}
	}

	// Processes JoinPoints specified by the "beans" attribute which indicate
	// methods to advise
	// e.g. @Before(beans = { "barBean.method(*)" })
	private void processBeanMethodJoinPoint(String bean, Object beanObject,
			Object advisor, Advice advice, JoinPoint joinPoint,
			Map<String, Pointcut> pointcutMap) {
		if (!bean.endsWith(")"))
			throw new InfinitumRuntimeException("Invalid join point '" + bean
					+ "' in aspect '" + advisor.getClass().getName() + "'.");
		String methodName;
		String[] args;
		try {
			methodName = bean.substring(bean.indexOf('.') + 1,
					bean.indexOf('('));
			String params = bean.substring(bean.indexOf('(') + 1,
					bean.indexOf(')'));
			if (params.trim().length() == 0)
				args = new String[0];
			else
				args = params.split(",");
		} catch (IndexOutOfBoundsException e) {
			throw new InfinitumRuntimeException("Invalid join point '" + bean
					+ "' in aspect '" + advisor.getClass().getName() + "'.");
		}
		if (args.length == 0) {
			// Parameterless method
			Method method = mClassReflector.getMethod(beanObject.getClass(),
					methodName);
			if (method == null)
				throw new InfinitumRuntimeException("Method '" + methodName
						+ "' from pointcut '" + bean
						+ "' could not be found.");
			joinPoint.setMethod(method);
			putJoinPoint(pointcutMap, joinPoint);
		} else if (args[0].trim().equals("*")) {
			// Wildcard -- add all methods with the given name
			for (Method method : mClassReflector.getMethodsByName(
					beanObject.getClass(), methodName)) {
				JoinPoint copied = advice.isAround()
						? new BasicProceedingJoinPoint(
								(BasicProceedingJoinPoint) joinPoint)
						: new BasicJoinPoint((BasicJoinPoint) joinPoint);
				copied.setMethod(method);
				putJoinPoint(pointcutMap, copied);
			}
		} else {
			// Add method with the given arguments
			Class<?>[] argTypes = new Class<?>[args.length];
			for (int i = 0; i < args.length; i++) {
				argTypes[i] = mPackageReflector.getClass(args[i].trim());
			}
			Method method = mClassReflector.getMethod(beanObject.getClass(),
					methodName, argTypes);
			if (method == null)
				throw new InfinitumRuntimeException("Method '" + methodName
						+ "' from pointcut '" + bean
						+ "' could not be found.");
			joinPoint.setMethod(method);
			putJoinPoint(pointcutMap, joinPoint);
		}
	}

	// Adds the JoinPoint to a Pointcut in pointcutMap
	// If there's no Pointcut for the type, it will add one
	private void putJoinPoint(Map<String, Pointcut> pointcutMap,
			JoinPoint joinPoint) {
		if (pointcutMap.containsKey(joinPoint.getBeanName())) {
			pointcutMap.get(joinPoint.getBeanName()).addJoinPoint(joinPoint);
		} else {
			Pointcut pointcut = new Pointcut(joinPoint.getBeanName(),
					joinPoint.getTargetType());
			pointcut.addJoinPoint(joinPoint);
			pointcutMap.put(joinPoint.getBeanName(), pointcut);
		}
	}

}
