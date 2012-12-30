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

import com.clarionmedia.infinitum.context.InfinitumContext;

/**
 * <p>
 * This interface enables an {@link InfinitumContext} to have its
 * {@link BeanFactory} modified after it has been configured.
 * </p>
 *
 * @author Tyler Treat
 * @version 1.0
 * @since 07/05/12
 */
public interface BeanFactoryPostProcessor {

    /**
     * Post process the {@link BeanFactory} after the {@link InfinitumContext}
     * has been initialized.
     *
     * @param beanFactory the {@code BeanFactory} to modify
     */
    void postProcessBeanFactory(BeanFactory beanFactory);

}
