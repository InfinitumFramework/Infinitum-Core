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

package com.clarionmedia.infinitum.di.impl;

import com.clarionmedia.infinitum.aop.AopProxy;
import com.clarionmedia.infinitum.di.AbstractBeanDefinition;
import com.clarionmedia.infinitum.di.BeanFactory;

/**
 * <p>
 * Concrete implementation of {@link AbstractBeanDefinition} which describes
 * prototype beans.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 08/04/12
 * @since 1.0
 */
public class PrototypeBeanDefinition extends AbstractBeanDefinition {

	/**
	 * Constructs a new {@code PrototypeBeanDefinition}.
	 * 
	 * @param beanFactory
	 *            the {@link BeanFactory} containing this bean
	 */
	public PrototypeBeanDefinition(BeanFactory beanFactory) {
		super(beanFactory);
	}

	@Override
	public Object getBeanInstance() {
		Object bean = createBean();
		inject(bean);
		setFields(bean);
		postConstruct(bean);
		if (mBeanProxy != null) {
			AopProxy proxy = mBeanProxy.clone();
			proxy.setTarget(bean);
			return proxy.getProxy();
		}
		return bean;
	}

	@Override
	public Object getNonProxiedBeanInstance() {
		Object bean = createBean();
		setFields(bean);
		return bean;
	}

}
