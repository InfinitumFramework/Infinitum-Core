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
     * @param context        the {@link InfinitumContext} the given bean is registered with
     * @param beanDefinition the {@code AbstractBeanDefinition} to post process
     */
    void postProcessBean(InfinitumContext context,
                         AbstractBeanDefinition beanDefinition);

}
