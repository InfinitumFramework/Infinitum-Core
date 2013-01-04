/*
 * Copyright (c) 2012 Tyler Treat
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

import com.clarionmedia.infinitum.di.AbstractBeanDefinition;
import com.clarionmedia.infinitum.di.AbstractProxy;
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
			AbstractProxy proxy = mBeanProxy.clone();
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
