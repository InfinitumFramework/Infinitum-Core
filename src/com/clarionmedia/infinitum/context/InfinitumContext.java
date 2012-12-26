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

import android.content.Context;

import com.clarionmedia.infinitum.di.BeanFactory;

/**
 * <p>
 * Acts as a container for framework-wide context information. This should not
 * be instantiated directly but rather obtained through a {@link ContextFactory}
 * , which creates an instance of this from {@code infinitum.cfg.xml}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 05/18/12
 * @since 05/18/12
 */
public interface InfinitumContext {

	/**
	 * Must be executed after the {@code InfinitumContext} has been initialized.
	 * 
	 * @param context
	 *            the {@link Context} used for post processing
	 */
	void postProcess(Context context);

	/**
	 * Indicates if debug is enabled or not. If it is enabled, Infinitum will
	 * produce log statements in {@code Logcat}, otherwise it will not produce
	 * any logging. This value is also used by Infinitum's logging framework.
	 * 
	 * @return {@code true} if debug is on, {@code false} if not
	 */
	boolean isDebug();

	/**
	 * Retrieves the Android {@link Context} for this {@code InfinitumContext},
	 * which contains application-wide context information.
	 * 
	 * @return {@code Context}
	 */
	Context getAndroidContext();

	/**
	 * Retrieves the {@link BeanFactory} for this {@code InfinitumContext}. The
	 * {@code BeanContainer} is used to retrieve beans that have been configured
	 * in {@code infinitum.cfg.xml}.
	 * 
	 * @return {@code BeanContainer}
	 */
	BeanFactory getBeanFactory();

	/**
	 * Retrieves a bean with the given name. Beans are configured in
	 * {@code infinitum.cfg.xml}.
	 * 
	 * @param name
	 *            the name of the bean to retrieve
	 * @return a bean instance or {@code null} if no bean has been configured
	 *         with the given name
	 */
	Object getBean(String name);

	/**
	 * Retrieves a bean with the given name and {@link Class}. Beans are
	 * configured in {@code infinitum.cfg.xml}.
	 * 
	 * @param name
	 *            the name of the bean to retrieve
	 * @param clazz
	 *            the type of the bean to retrieve
	 * @return an instance of the bean
	 */
	<T> T getBean(String name, Class<T> clazz);

	/**
	 * Indicates if component scan is enabled.
	 * 
	 * @return {@code true} if component scan is enabled, {@code false} if not
	 */
	boolean isComponentScanEnabled();

	/**
	 * Returns a {@link List} of the children contexts for this
	 * {@code InfinitumContext}.
	 * 
	 * @return children contexts
	 */
	List<InfinitumContext> getChildContexts();

	/**
	 * Adds the given {@code InfinitumContext} as a child context to this
	 * {@code InfinitumContext}.
	 * 
	 * @param context
	 *            the child context to add
	 */
	void addChildContext(InfinitumContext context);

	/**
	 * Returns the parent context for this {@code InfinitumContext} or
	 * {@code null} if there is none.
	 * 
	 * @return parent context
	 */
	InfinitumContext getParentContext();
	
	/**
	 * Retrieves the {@link RestfulContext} for this {@code InfinitumContext}.
	 * The {@code RestfulConfiguration} contains configuration settings for the
	 * RESTful client.
	 * 
	 * @return {@code RestfulContext}
	 */
	RestfulContext getRestContext();

}
