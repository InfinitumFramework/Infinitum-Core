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

package com.clarionmedia.infinitum.reflection.impl;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import android.content.Context;

import com.clarionmedia.infinitum.exception.InfinitumRuntimeException;
import com.clarionmedia.infinitum.logging.Logger;
import com.clarionmedia.infinitum.reflection.PackageReflector;

import dalvik.system.DexFile;

/**
 * <p>
 * This class provides reflection methods for working with packages contained
 * within projects that are using Infinitum and their contained resources.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 02/14/12
 * @since 1.0
 */
public class DefaultPackageReflector implements PackageReflector {

	private Logger mLogger;

	/**
	 * Creates a new {@code DefaultPackageReflector} instance.
	 */
	public DefaultPackageReflector() {
		mLogger = Logger.getInstance(getClass().getSimpleName());
	}

	@Override
	public Class<?> getClass(String className) {
		Class<?> clazz;
		try {
			clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new InfinitumRuntimeException("Class '" + className + "' could not be resolved.");
		}
		return clazz;
	}

	@Override
	public synchronized Set<Class<?>> getPackageClasses(Context context, String... packageNames) {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		try {
			DexFile dex = new DexFile(context.getApplicationInfo().sourceDir);
			Enumeration<String> entries = dex.entries();
			while (entries.hasMoreElements()) {
				String entry = entries.nextElement();
				for (String packageName : packageNames) {
					Locale locale = Locale.getDefault();
					if (entry.toLowerCase(locale).startsWith(packageName.toLowerCase(locale))) {
						classes.add(getClass(entry));
						break;
					}
				}
			}
			return classes;
		} catch (IOException e) {
			mLogger.error("Component scanning is not supported in this environment.", e);
			return classes;
		}
	}

}
