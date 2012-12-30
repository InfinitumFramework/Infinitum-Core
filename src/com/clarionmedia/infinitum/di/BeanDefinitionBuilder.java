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
     * @param name the name to set
     * @return {@code BeanDefinitionBuilder} to allow chaining
     */
    BeanDefinitionBuilder setName(String name);

    /**
     * Sets the bean {@link Class} type.
     *
     * @param type the type to set
     * @return {@code BeanDefinitionBuilder} to allow chaining
     */
    BeanDefinitionBuilder setType(Class<?> type);

    /**
     * Sets the bean properties. These are {@code Field}-value pairs which can
     * be injected into the bean.
     *
     * @param properties the bean properties to set
     * @return {@code BeanDefinitionBuilder} to allow chaining
     */
    BeanDefinitionBuilder setProperties(Map<String, Object> properties);

    /**
     * Sets the bean lifecycle scope. The {@code singleton} scope is used by
     * default if this is not specified.
     *
     * @param scope the scope to set, such as {@code prototype} or
     *              {@code singleton}
     * @return {@code BeanDefinitionBuilder} to allow chaining
     */
    BeanDefinitionBuilder setScope(String scope);

}
