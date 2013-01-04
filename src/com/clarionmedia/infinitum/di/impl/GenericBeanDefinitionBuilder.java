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
