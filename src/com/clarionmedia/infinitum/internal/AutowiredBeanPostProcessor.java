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

package com.clarionmedia.infinitum.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.context.exception.InfinitumConfigurationException;
import com.clarionmedia.infinitum.di.AbstractBeanDefinition;
import com.clarionmedia.infinitum.di.BeanFactory;
import com.clarionmedia.infinitum.di.BeanPostProcessor;
import com.clarionmedia.infinitum.di.BeanUtils;
import com.clarionmedia.infinitum.di.annotation.Autowired;
import com.clarionmedia.infinitum.di.annotation.Component;
import com.clarionmedia.infinitum.exception.InfinitumRuntimeException;
import com.clarionmedia.infinitum.reflection.ClassReflector;
import com.clarionmedia.infinitum.reflection.impl.DefaultClassReflector;

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
@Component
public class AutowiredBeanPostProcessor implements BeanPostProcessor {

	private ClassReflector mClassReflector;

	@Override
	public void postProcessBean(InfinitumContext context, AbstractBeanDefinition beanDefinition) {
		mClassReflector = new DefaultClassReflector();
		registerFieldInjections(context.getBeanFactory(), beanDefinition);
		registerSetterInjections(context.getBeanFactory(), beanDefinition);
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
		AbstractBeanDefinition value;
		if (candidate == null || candidate.trim().length() == 0)
			candidate = BeanUtils.findCandidateBeanName(beanFactory, field.getType());
		if (candidate == null)
			throw new InfinitumRuntimeException("Unable to satisfy autowired dependency of type '" + field.getType().getName() + "' in bean of type '" + bean.getType().getName() + "'.");
		value = beanFactory.getBeanDefinition(candidate);
		bean.addFieldInjection(field, value);
	}

	private void registerSetterInjection(BeanFactory beanFactory, AbstractBeanDefinition bean, Method setter, String candidate) {
		AbstractBeanDefinition value;
		if (candidate == null || candidate.trim().length() == 0) {
			if (setter.getParameterTypes().length != 1)
				throw new InfinitumConfigurationException("Autowired setter method '" + setter.getName() + " in bean of type '" + bean.getType().getName() + "' is not a single argument method.");
			candidate = BeanUtils.findCandidateBeanName(beanFactory, setter.getParameterTypes()[0]);
		}
		value = beanFactory.getBeanDefinition(candidate);
		bean.addSetterInjection(setter, value);
	}

}
