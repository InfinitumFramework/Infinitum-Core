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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.context.exception.InfinitumConfigurationException;
import com.clarionmedia.infinitum.di.AbstractBeanDefinition;
import com.clarionmedia.infinitum.di.XmlBean;
import com.clarionmedia.infinitum.di.BeanFactory;
import com.clarionmedia.infinitum.reflection.PackageReflector;
import com.clarionmedia.infinitum.reflection.impl.DefaultPackageReflector;

/**
 * <p>
 * Implementation of {@link BeanFactory} for storing beans that have been
 * configured in {@code infinitum.cfg.xml}. {@code ConfigurableBeanFactory} also
 * acts as a service locator for {@link InfinitumContext}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 04/23/12
 * @since 1.0
 */
public class ConfigurableBeanFactory implements BeanFactory {

	private PackageReflector mPackageReflector;
	private Map<String, AbstractBeanDefinition> mBeanDefinitions;
	private InfinitumContext mContext;

	/**
	 * Constructs a new {@code ConfigurableBeanFactory}.
	 * 
	 * @param context
	 *            the parent {@link InfinitumContext}
	 */
	public ConfigurableBeanFactory(InfinitumContext context) {
		mContext = context;
		mPackageReflector = new DefaultPackageReflector();
		mBeanDefinitions = new HashMap<String, AbstractBeanDefinition>();
	}

	@Override
	public Object loadBean(String name) throws InfinitumConfigurationException {
		if (!mBeanDefinitions.containsKey(name))
			throw new InfinitumConfigurationException("Bean '" + name + "' could not be resolved");
		return mBeanDefinitions.get(name).getBeanInstance();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T loadBean(String name, Class<T> clazz) throws InfinitumConfigurationException {
		Object bean = loadBean(name);
		if (!clazz.isInstance(bean))
			throw new InfinitumConfigurationException("Bean '" + name + "' was not of type '" + clazz.getName() + "'.");
		return (T) bean;
	}

	@Override
	public boolean beanExists(String name) {
		return mBeanDefinitions.containsKey(name);
	}

	@Override
	public void registerBeans(List<XmlBean> beans) {
		if (beans == null)
			return;
		for (XmlBean bean : beans) {
			Map<String, Object> propertiesMap = new HashMap<String, Object>();
			for (XmlBean.Property property : bean.getProperties()) {
				String name = property.getName();
				String ref = property.getRef();
				if (ref != null) {
					propertiesMap.put(name, loadBean(ref));
				} else {
					String value = property.getValue();
					propertiesMap.put(name, value);
				}
			}
			Class<?> clazz = mPackageReflector.getClass(bean.getClassName());
			AbstractBeanDefinition beanDefinition = new GenericBeanDefinitionBuilder(this).setName(bean.getId()).setType(clazz)
					.setProperties(propertiesMap).setScope(bean.getScope()).build();
			registerBean(beanDefinition);
		}
	}

	@Override
	public void registerBean(AbstractBeanDefinition beanDefinition) {
		mBeanDefinitions.put(beanDefinition.getName(), beanDefinition);
	}

	@Override
	public Map<String, AbstractBeanDefinition> getBeanDefinitions() {
		return mBeanDefinitions;
	}

	@Override
	public InfinitumContext getContext() {
		return mContext;
	}

	@Override
	public AbstractBeanDefinition getBeanDefinition(String name) {
		return mBeanDefinitions.get(name);
	}

	@Override
	public Class<?> getBeanType(String name) {
		AbstractBeanDefinition bean = mBeanDefinitions.get(name);
		if (bean == null)
			return null;
		return bean.getType();
	}

}
