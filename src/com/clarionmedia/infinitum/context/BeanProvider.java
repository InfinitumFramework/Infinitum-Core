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

package com.clarionmedia.infinitum.context;

import java.util.List;

import com.clarionmedia.infinitum.di.AbstractBeanDefinition;
import com.clarionmedia.infinitum.di.BeanDefinitionBuilder;

/**
 * <p>
 * Provides a hook for implementing classes to provide bean definitions which
 * will be registered with the {@link BeanFactory}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 12/29/12
 * @since 1.0
 */
public interface BeanProvider {

	/**
	 * Returns the {@code AbstractBeanDefinition}s to be registered with the
	 * {@link BeanFactory}.
	 * 
	 * @param beanDefinitionBuilder
	 *            the {@link BeanDefinitionBuilder} to build
	 *            beans with
	 * @return {@link List} of {@code AbstractBeanDefinition}s
	 */
	List<AbstractBeanDefinition> getBeans(BeanDefinitionBuilder beanDefinitionBuilder);

}
