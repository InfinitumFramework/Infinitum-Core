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

import android.content.Context;

import com.clarionmedia.infinitum.context.exception.InfinitumConfigurationException;
import com.clarionmedia.infinitum.context.impl.XmlContextFactory;

/**
 * <p>
 * Provides access to an {@link InfinitumContext} singleton. Before an
 * {@code InfinitumContext} can be retrieved,
 * {@link ContextFactory#configure(Context, int)} must be called, which will
 * read in configuration settings from {@code infinitum.cfg.xml}. If an attempt
 * to access the context is made before it has been configured, an
 * {@link InfinitumConfigurationException} will be thrown.
 * </p>
 * <p>
 * {@code ContextFactory} instances can be retrieved by calling
 * {@link ContextFactory#newInstance()}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 05/18/12
 * @since 1.0
 */
public abstract class ContextFactory {

	protected static Context mContext;

	/**
	 * Retrieves a new {@code ContextFactory} instance.
	 * 
	 * @return {@code ContextFactory}
	 */
	public static ContextFactory newInstance() {
		return new XmlContextFactory();
	}

	/**
	 * Retrieves the Android {@link Context} registered with the configured
	 * {@link InfinitumContext}.
	 * 
	 * @return {@code Context}
	 */
	public Context getAndroidContext() {
		return mContext;
	}

	/**
	 * Configures Infinitum with the implicit configuration file
	 * {@code res/raw/infinitum.cfg.xml}. This method must be called before
	 * attempting to retrieve an {@link InfinitumContext}.
	 * 
	 * @param context
	 *            the calling {@link Context}.
	 * @return configured {@code InfinitumContext}
	 * @throws InfinitumConfigurationException
	 *             if the implied configuration file could not be found or if
	 *             the file could not be parsed
	 */
	public abstract InfinitumContext configure(Context context) throws InfinitumConfigurationException;

	/**
	 * Configures Infinitum with the specified configuration file. This method
	 * must be called before attempting to retrieve an {@link InfinitumContext}.
	 * 
	 * @param context
	 *            the calling {@code Context}
	 * @param configId
	 *            the resource ID for the raw XML configuration file
	 * @return configured {@code InfinitumContext}
	 * @throws InfinitumConfigurationException
	 *             if the configuration file could not be found or if the file
	 *             could not be parsed
	 */
	public abstract InfinitumContext configure(Context context, int configId) throws InfinitumConfigurationException;

	/**
	 * Retrieves the {@link InfinitumContext} singleton.
	 * {@link ContextFactory#configure} must be called before using this method.
	 * Otherwise, an {@link InfinitumConfigurationException} will be thrown.
	 * 
	 * @return the {@code InfinitumContext} singleton
	 * @throws InfinitumConfigurationException
	 *             if {@code configure} was not called
	 */
	public abstract InfinitumContext getContext() throws InfinitumConfigurationException;

	/**
	 * Retrieves an {@link InfinitumContext} singleton of the given type.
	 * {@link ContextFactory#configure} must be called before using this method.
	 * Otherwise, an {@link InfinitumConfigurationException} will be thrown.
	 * 
	 * @param contextType
	 *            the type of the {@code InfinitumContext} to retrieve
	 * @return the {@code InfinitumContext} singleton
	 * @throws InfinitumConfigurationException
	 *             if {@code configure} was not called or a context of the
	 *             desired type is not available
	 */
	public abstract <T extends InfinitumContext> T getContext(Class<T> contextType) throws InfinitumConfigurationException;

}
