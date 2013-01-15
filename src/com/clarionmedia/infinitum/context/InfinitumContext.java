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

package com.clarionmedia.infinitum.context;

import java.util.List;

import android.content.Context;

import com.clarionmedia.infinitum.activity.LifecycleEvent;
import com.clarionmedia.infinitum.context.exception.InfinitumConfigurationException;
import com.clarionmedia.infinitum.di.BeanFactory;
import com.clarionmedia.infinitum.event.EventSubscriber;

/**
 * <p>
 * Acts as a container for framework-wide context information. This should not
 * be instantiated directly but rather obtained through a {@link ContextFactory}
 * , which creates an instance of this from {@code infinitum.cfg.xml}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 05/18/12
 * @since 1.0
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
	 * Retrieves a child {@link InfinitumContext} singleton of the given type.
	 * 
	 * @param contextType
	 *            the type of the {@code InfinitumContext} to retrieve
	 * @return the {@code InfinitumContext} singleton
	 * @throws InfinitumConfigurationException
	 *             if the desired type is not available
	 */
	<T extends InfinitumContext> T getChildContext(Class<T> contextType);

	/**
	 * Retrieves the {@link RestfulContext} for this {@code InfinitumContext}.
	 * The {@code RestfulConfiguration} contains configuration settings for the
	 * RESTful client.
	 * 
	 * @return {@code RestfulContext}
	 */
	RestfulContext getRestContext();

	/**
	 * Publishes the given {@link LifecycleEvent} to the event system.
	 * 
	 * @param event
	 *            the {@code LifecycleEvent} to publish
	 */
	void publishEvent(LifecycleEvent event);

	/**
	 * Registers the given {@link EventSubscriber} for events published by this
	 * {@code InfinitumContext}.
	 * 
	 * @param subscriber
	 *            the {@code EventSubscriber} to register for events
	 */
	void subscribeForEvents(EventSubscriber subscriber);

}
