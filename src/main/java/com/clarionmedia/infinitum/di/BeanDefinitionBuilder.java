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

package com.clarionmedia.infinitum.di;

import java.util.Map;

/**
 * <p>
 * Provides a means to construct {@link AbstractBeanDefinition} instances. This
 * can be used to dynamically build bean definitions with the purpose of
 * registering them with a {@link BeanFactory} later.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 08/04/12
 * @since 1.0
 */
public interface BeanDefinitionBuilder {

	/**
	 * Builds the {@link AbstractBeanDefinition} instance.
	 * 
	 * @return configured {@code AbstractBeanDefinition}
	 */
	AbstractBeanDefinition build();

	/**
	 * Sets the bean name.
	 * 
	 * @param name
	 *            the name to set
	 * @return {@code BeanDefinitionBuilder} to allow chaining
	 */
	BeanDefinitionBuilder setName(String name);

	/**
	 * Sets the bean {@link Class} type.
	 * 
	 * @param type
	 *            the type to set
	 * @return {@code BeanDefinitionBuilder} to allow chaining
	 */
	BeanDefinitionBuilder setType(Class<?> type);

	/**
	 * Sets the bean properties. These are {@code Field}-value pairs which can
	 * be injected into the bean.
	 * 
	 * @param properties
	 *            the bean properties to set
	 * @return {@code BeanDefinitionBuilder} to allow chaining
	 */
	BeanDefinitionBuilder setProperties(Map<String, Object> properties);

	/**
	 * Sets the bean lifecycle scope. The {@code singleton} scope is used by
	 * default if this is not specified.
	 * 
	 * @param scope
	 *            the scope to set, such as {@code prototype} or
	 *            {@code singleton}
	 * @return {@code BeanDefinitionBuilder} to allow chaining
	 */
	BeanDefinitionBuilder setScope(String scope);

}
