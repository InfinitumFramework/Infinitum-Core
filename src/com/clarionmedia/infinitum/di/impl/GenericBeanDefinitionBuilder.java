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

import java.util.Map;

import com.clarionmedia.infinitum.di.AbstractBeanDefinition;
import com.clarionmedia.infinitum.di.BeanDefinitionBuilder;
import com.clarionmedia.infinitum.di.BeanFactory;

/**
 * <p>
 * Generic implementation of {@link BeanDefinitionBuilder}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 08/04/12
 * @since 1.0
 */
public class GenericBeanDefinitionBuilder implements BeanDefinitionBuilder {

	private String mName;
	private Class<?> mType;
	private Map<String, Object> mProperties;
	private String mScope;
	private BeanFactory mBeanFactory;

	/**
	 * Creates a new {@code GenericBeanDefinitionBuilder}.
	 * 
	 * @param beanFactory
	 *            the {@link BeanFactory} created bean definitions are scoped to
	 */
	public GenericBeanDefinitionBuilder(BeanFactory beanFactory) {
		mBeanFactory = beanFactory;
	}

	@Override
	public AbstractBeanDefinition build() {
		if (mName == null || mType == null)
			throw new IllegalStateException(
					"Must provide bean name and type to build definition.");
		AbstractBeanDefinition ret;
		if (mScope == null || mScope.equalsIgnoreCase("singleton"))
			ret = new SingletonBeanDefinition(mBeanFactory);
		else
			ret = new PrototypeBeanDefinition(mBeanFactory);
		ret.setName(mName);
		ret.setType(mType);
		ret.setProperties(mProperties);
		mName = null;
		mType = null;
		mScope = null;
		mProperties = null;
		return ret;
	}

	@Override
	public BeanDefinitionBuilder setName(String name) {
		mName = name;
		return this;
	}

	@Override
	public BeanDefinitionBuilder setType(Class<?> type) {
		mType = type;
		return this;
	}

	@Override
	public BeanDefinitionBuilder setProperties(Map<String, Object> properties) {
		mProperties = properties;
		return this;
	}

	@Override
	public BeanDefinitionBuilder setScope(String scope) {
		mScope = scope;
		return this;
	}

}
