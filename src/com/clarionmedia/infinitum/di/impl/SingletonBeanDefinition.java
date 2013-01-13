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

import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.context.impl.InfinitumContextProxy;
import com.clarionmedia.infinitum.di.AbstractBeanDefinition;
import com.clarionmedia.infinitum.di.AbstractProxy;
import com.clarionmedia.infinitum.di.BeanFactory;
import com.clarionmedia.infinitum.reflection.ClassReflector;

/**
 * <p>
 * Concrete implementation of {@link AbstractBeanDefinition} which describes
 * singleton beans.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 08/04/12
 * @since 1.0
 */
public class SingletonBeanDefinition extends AbstractBeanDefinition {

	private Object mBean;
	private Object mProxiedBean;

	/**
	 * Constructs a new {@code SingletonBeanDefinition}.
	 * 
	 * @param beanFactory
	 *            the {@link BeanFactory} containing this bean
	 * @param classReflector
	 *            the {@link ClassReflector} to use
	 */
	public SingletonBeanDefinition(BeanFactory beanFactory, ClassReflector classReflector) {
		super(beanFactory, classReflector);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getBeanInstance() {
		if (InfinitumContext.class.isAssignableFrom(mType))
			return new InfinitumContextProxy((Class<? extends InfinitumContext>) mType).getProxy();
		if (mProxiedBean != null)
			return mProxiedBean;
		if (mBeanProxy != null) {
			mProxiedBean = mBeanProxy.getProxy();
			inject(AbstractProxy.getTarget(mProxiedBean));
			return mProxiedBean;
		}
		if (mBean != null)
			return mBean;
		mBean = createBean();
		inject(mBean);
		setFields(mBean);
		postConstruct(mBean);
		return mBean;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getNonProxiedBeanInstance() {
		if (InfinitumContext.class.isAssignableFrom(mType))
			return new InfinitumContextProxy((Class<? extends InfinitumContext>) mType).getProxy();
		if (mBean != null)
			return mBean;
		mBean = createBean();
		inject(mBean);
		setFields(mBean);
		postConstruct(mBean);
		return mBean;
	}

}
