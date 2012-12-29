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

import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.context.impl.InfinitumContextProxy;
import com.clarionmedia.infinitum.di.AbstractBeanDefinition;
import com.clarionmedia.infinitum.di.AopProxy;
import com.clarionmedia.infinitum.di.BeanFactory;

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
	 */
	public SingletonBeanDefinition(BeanFactory beanFactory) {
		super(beanFactory);
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
			inject(AopProxy.getTarget(mProxiedBean));
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
