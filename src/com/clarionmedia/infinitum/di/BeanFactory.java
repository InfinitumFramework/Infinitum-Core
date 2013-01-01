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

package com.clarionmedia.infinitum.di;

import java.util.List;
import java.util.Map;

import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.context.exception.InfinitumConfigurationException;
import com.clarionmedia.infinitum.di.annotation.Bean;

/**
 * <p>
 * Stores beans that have been configured in {@code infinitum.cfg.xml} or
 * through annotations. The {@code BeanFactory} acts as a service locator for
 * {@link InfinitumContext}. Beans are retrieved by their name and registered by
 * providing a name, class, and field values.
 * </p>
 * <p>
 * {@code BeanFactory} is responsible for maintaining a bean registry,
 * initializing bean instances, and performing any necessary dependency
 * injections.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 05/18/12
 * @since 1.0
 */
public interface BeanFactory {

	/**
	 * Retrieves an instance of the bean with the given name. The name is
	 * configured in {@code infinitum.cfg.xml} or the {@link Bean} annotation.
	 * 
	 * @param name
	 *            the name of the bean to retrieve
	 * @return an instance of the bean
	 * @throws InfinitumConfigurationException
	 *             if the bean does not exist or could not be constructed
	 */
	Object loadBean(String name) throws InfinitumConfigurationException;

	/**
	 * Retrieves an instance of the bean with the given name and {@link Class}.
	 * The name is configured in {@code infinitum.cfg.xml} or the {@link Bean}
	 * annotation.
	 * 
	 * @param name
	 *            the name of the bean to retrieve
	 * @param clazz
	 *            the type of the bean to retrieve
	 * @return an instance of the bean
	 * @throws InfinitumConfigurationException
	 *             if the bean does not exist, could not be constructed, or is
	 *             of the wrong type
	 */
	<T> T loadBean(String name, Class<T> clazz) throws InfinitumConfigurationException;

	/**
	 * Retrieves the {@link AbstractBeanDefinition} for the bean with the given
	 * name.
	 * 
	 * @param name
	 *            bean name
	 * @return {@code AbstractBeanDefinition}
	 */
	AbstractBeanDefinition getBeanDefinition(String name);

	/**
	 * Checks if a bean with the given name exists.
	 * 
	 * @param name
	 *            the name to check
	 * @return {@code true} if it exists, {@code false} if not
	 */
	boolean beanExists(String name);

	/**
	 * Registers the given {@code Beans} with the {@code BeanFactory}.
	 * 
	 * @param beans
	 *            the {@code Beans} to register
	 */
	void registerBeans(List<XmlBean> beans);

	/**
	 * Registers the bean with the {@code BeanFactory}.
	 * 
	 * @param beanDefinition
	 *            the {@link AbstractBeanDefinition} to register
	 * 
	 */
	void registerBean(AbstractBeanDefinition beanDefinition);

	/**
	 * Retrieves the bean {@link Map} for this {@code BeanFactory}.
	 * 
	 * @return {@code Map} of bean names and their corresponding
	 *         {@link BeanDefinition} instances
	 */
	Map<String, AbstractBeanDefinition> getBeanDefinitions();

	/**
	 * Returns the {@link Class} type for the bean with the given name.
	 * 
	 * @param name
	 *            bean name
	 * @return bean type or {@code null} if no such bean exists
	 */
	Class<?> getBeanType(String name);

	/**
	 * Returns the associated {@link InfinitumContext}.
	 * 
	 * @return {@code InfinitumContext}
	 */
	InfinitumContext getContext();

}
