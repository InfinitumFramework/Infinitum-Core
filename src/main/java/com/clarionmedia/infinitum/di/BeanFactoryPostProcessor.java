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
 * This interface enables an {@link InfinitumContext} to have its
 * {@link BeanFactory} modified after it has been configured.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0
 * @since 1.0
 */
public interface BeanFactoryPostProcessor {

	/**
	 * Post process the {@link BeanFactory} after the {@link InfinitumContext}
	 * has been initialized.
	 * 
	 * @param beanFactory
	 *            the {@code BeanFactory} to modify
	 */
	void postProcessBeanFactory(BeanFactory beanFactory);

}
