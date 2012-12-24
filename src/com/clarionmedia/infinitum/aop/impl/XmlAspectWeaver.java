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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;

import com.clarionmedia.infinitum.aop.AdvisedProxyFactory;
import com.clarionmedia.infinitum.aop.AopProxy;
import com.clarionmedia.infinitum.aop.AspectComponent;
import com.clarionmedia.infinitum.aop.AspectWeaver;
import com.clarionmedia.infinitum.aop.JoinPoint;
import com.clarionmedia.infinitum.aop.ProceedingJoinPoint;
import com.clarionmedia.infinitum.aop.JoinPoint.PointcutType;
import com.clarionmedia.infinitum.aop.Pointcut;
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
 * Implementation of {@link AspectWeaver} for processing aspects specified in
 * XML.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/18/12
 * @since 1.0
 */
public class XmlAspectWeaver implements AspectWeaver {

	private ClassReflector mClassReflector;
	private PackageReflector mPackageReflector;
	private BeanFactory mBeanFactory;
	private AdvisedProxyFactory mProxyFactory;
	private List<AspectComponent> mAspects;
	
	@Deprecated
	public XmlAspectWeaver() {
	}

	/**
	 * Creates a new {@code XmlAspectWeaver} with the given {@link InfinitumContext}.
	 * 
	 * @param context
	 *            the {@code InfinitumContext} to retrieve beans from
	 * @param aspects
	 *            the aspects to weave
	 */
	public XmlAspectWeaver(InfinitumContext context,
			List<AspectComponent> aspects) {
		mClassReflector = new DefaultClassReflector();
		mPackageReflector = new DefaultPackageReflector();
		mProxyFactory = new DelegatingAdvisedProxyFactory();
		mBeanFactory = context.getBeanFactory();
		mAspects = aspects;
	}

	@Override
	public void weave(Context context, Set<Class<?>> aspects) {
		for (Pointcut pointcut : getPointcuts(mAspects)) {
			String beanName = pointcut.getBeanName();
			Object bean = mBeanFactory.loadBean(beanName);
			AopProxy proxy = mProxyFactory.createProxy(context, bean, pointcut);
			mBeanFactory.getBeanDefinitions().get(beanName).setBeanProxy(proxy);
		}
	}

	/**
	 * Returns a {@link List} of {@link AspectComponent} instances for this
	 * {@code XmlAspectWeaver}.
	 * 
	 * @return {@code List} of {@code AspectComponents}
	 */
	public List<AspectComponent> getAspects() {
		return mAspects;
	}

	/**
	 * Sets the {@link AspectComponent} instances for this
	 * {@code XmlAspectWeaver}.
	 * 
	 * @param aspects
	 *            the {@code AspectComponents} to set
	 */
	public void setAspects(List<AspectComponent> aspects) {
		mAspects = aspects;
	}

	// Build a Collection of Pointcuts for the given aspects
	private Collection<Pointcut> getPointcuts(List<AspectComponent> aspects) {
		Map<String, Pointcut> pointcutMap = new HashMap<String, Pointcut>();
		// Process all aspects
		for (AspectComponent aspect : aspects) {
			processAspect(aspect, pointcutMap);
		}
		return pointcutMap.values();
	}

	// Processes XML advice
	private void processAspect(AspectComponent aspect,
			Map<String, Pointcut> pointcutMap) {
		for (AspectComponent.Advice advice : aspect.getAdvice()) {
			String methodName = advice.getId();
			Class<?> aspectClass = mPackageReflector.getClass(aspect
					.getClassName());
			Method adviceMethod = mClassReflector.getMethod(aspectClass,
					methodName, advice.isAround()
							? ProceedingJoinPoint.class
							: JoinPoint.class);
			Object advisor = mClassReflector.getClassInstance(aspectClass);
			if (advice.getPointcutType() == PointcutType.Beans)
				processBeanJoinPoints(advisor, advice, adviceMethod,
						pointcutMap);
			else if (advice.getPointcutType() == PointcutType.Within)
				processWithinJoinPoints(advisor, advice, adviceMethod,
						pointcutMap);
		}
	}

	// Processes JoinPoints specified by the "beans" pointcut attribute value
	// e.g. <advice id="advice" type="before" pointcut="beans"
	// value="fooBean, barBean.method(*)" />
	private void processBeanJoinPoints(Object advisor,
			AspectComponent.Advice advice, Method adviceMethod,
			Map<String, Pointcut> pointcutMap) {
		for (String bean : advice.getSeparatedValues()) {
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
			joinPoint.setOrder(advice.getOrder());
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

	// Processes JoinPoints specified by the "beans" pointcut attribute value
	// which indicate
	// methods to advise
	// e.g. <advice id="advice" type="before" pointcut="beans"
	// value="barBean.method(*)" />
	private void processBeanMethodJoinPoint(String bean, Object beanObject,
			Object advisor, AspectComponent.Advice advice, JoinPoint joinPoint,
			Map<String, Pointcut> pointcutMap) {
		if (!bean.endsWith(")"))
			throw new InfinitumRuntimeException("Invalid pointcut '" + bean
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
			throw new InfinitumRuntimeException("Invalid pointcut '" + bean
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

	// Processes JoinPoints specified by the "within" pointcut attribute value
	// e.g. <advice id="advice" type="before" pointcut="within"
	// value="com.foo.bar.service, com.foo.bar.dao" />
	private void processWithinJoinPoints(Object advisor,
			AspectComponent.Advice advice, Method adviceMethod,
			Map<String, Pointcut> pointcutMap) {
		for (String pkg : advice.getSeparatedValues()) {
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
				joinPoint.setOrder(advice.getOrder());
				joinPoint.setClassScope(true);
				putJoinPoint(pointcutMap, joinPoint);
			}
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
