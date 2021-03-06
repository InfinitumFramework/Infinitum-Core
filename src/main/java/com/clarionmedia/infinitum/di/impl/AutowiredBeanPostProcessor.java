/*
 * Copyright (C) 2012 Clarion Media, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clarionmedia.infinitum.di.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.context.exception.InfinitumConfigurationException;
import com.clarionmedia.infinitum.di.AbstractBeanDefinition;
import com.clarionmedia.infinitum.di.BeanFactory;
import com.clarionmedia.infinitum.di.BeanPostProcessor;
import com.clarionmedia.infinitum.di.annotation.Autowired;
import com.clarionmedia.infinitum.exception.InfinitumRuntimeException;
import com.clarionmedia.infinitum.reflection.ClassReflector;
import com.clarionmedia.infinitum.reflection.impl.JavaClassReflector;

/**
 * <p>
 * Implementation of {@link BeanPostProcessor} used to inject autowired bean
 * dependencies after beans have been initialized.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/05/12
 * @since 1.0
 */
public class AutowiredBeanPostProcessor implements BeanPostProcessor {

	private ClassReflector mClassReflector;

	/**
	 * Constructs a new {@code AutowiredBeanPostProcessor} instance.
	 */
	public AutowiredBeanPostProcessor() {
		mClassReflector = new JavaClassReflector();
	}

	@Override
	public void postProcessBean(InfinitumContext context, AbstractBeanDefinition beanDefinition) {
		registerFieldInjections(context.getBeanFactory(), beanDefinition);
		registerSetterInjections(context.getBeanFactory(), beanDefinition);
	}

	/**
	 * Sets the {@link ClassReflector} for this
	 * {@code AutowiredBeanPostProcessor}.
	 * 
	 * @param classReflector
	 *            the {@code ClassReflector} to use
	 */
	public void setClassReflector(ClassReflector classReflector) {
		mClassReflector = classReflector;
	}

	private void registerFieldInjections(BeanFactory beanFactory, AbstractBeanDefinition bean) {
		for (Field field : mClassReflector.getAllFields(bean.getType())) {
			if (!field.isAnnotationPresent(Autowired.class))
				continue;
			Autowired autowired = field.getAnnotation(Autowired.class);
			registerFieldInjection(beanFactory, bean, field, autowired.value());
		}
	}

	private void registerSetterInjections(BeanFactory beanFactory, AbstractBeanDefinition bean) {
		for (Method method : mClassReflector.getAllMethods(bean.getType())) {
			if (!method.isAnnotationPresent(Autowired.class))
				continue;
			Autowired autowired = method.getAnnotation(Autowired.class);
			registerSetterInjection(beanFactory, bean, method, autowired.value());
		}
	}

	private void registerFieldInjection(BeanFactory beanFactory, AbstractBeanDefinition bean, Field field, String candidate) {
		if (candidate == null || candidate.trim().length() == 0)
			candidate = beanFactory.findCandidateBeanName(field.getType());
		if (candidate == null)
			throw new InfinitumRuntimeException("Unable to satisfy autowired dependency of type '" + field.getType().getName()
					+ "' in bean of type '" + bean.getType().getName() + "'.");
		AbstractBeanDefinition value = beanFactory.getBeanDefinition(candidate);
		bean.addFieldInjection(field, value);
	}

	private void registerSetterInjection(BeanFactory beanFactory, AbstractBeanDefinition bean, Method setter, String candidate) {
		if (setter.getParameterTypes().length != 1)
			throw new InfinitumConfigurationException("Autowired setter method '" + setter.getName() + " in bean of type '"
					+ bean.getType().getName() + "' is not a single-argument method.");
		if (candidate == null || candidate.trim().length() == 0)
			candidate = beanFactory.findCandidateBeanName(setter.getParameterTypes()[0]);
		if (candidate == null)
			throw new InfinitumRuntimeException("Unable to satisfy autowired dependency of type '"
					+ setter.getParameterTypes()[0].getName() + "' in bean of type '" + bean.getType().getName() + "'.");
		AbstractBeanDefinition value = beanFactory.getBeanDefinition(candidate);
		bean.addSetterInjection(setter, value);
	}

}
