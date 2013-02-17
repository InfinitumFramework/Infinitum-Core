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

import com.clarionmedia.infinitum.context.InfinitumContext;

/**
 * <p>
 * Allows for beans to be modified after they are initialized by the container.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/05/12
 * @since 1.0
 */
public interface BeanPostProcessor {

	/**
	 * Invoked on the given {@link AbstractBeanDefinition} after it has been
	 * initialized.
	 * 
	 * @param context
	 *            the {@link InfinitumContext} the given bean is registered with
	 * @param beanDefinition
	 *            the {@code AbstractBeanDefinition} to post process
	 */
	void postProcessBean(InfinitumContext context,
			AbstractBeanDefinition beanDefinition);

}
