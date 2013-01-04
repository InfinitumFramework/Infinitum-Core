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

package com.clarionmedia.infinitum.reflection;

import java.util.Set;

import android.content.Context;

/**
 * <p>
 * This interface provides reflection methods for working with packages
 * contained within projects that are using Infinitum and their contained
 * resources.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 05/17/12
 * @since 1.0
 */
public interface PackageReflector {

	/**
	 * Retrieves a {@link Class} instance for the {@code Class} described by the
	 * given package-qualified name. Returns {@code null} if the {@code Class}
	 * can not be found.
	 * 
	 * @param className
	 *            the package-qualified name of the desired {@code Class}
	 * @return instance of the {@code Class} or {@code null}if the {@code Class}
	 *         was not found
	 */
	Class<?> getClass(String className);

	/**
	 * Retrieves a {@link Set} of {@link Class} instances whose are contained in
	 * one of the given package names.
	 */
	Set<Class<?>> getPackageClasses(Context context, String... packageNames);

}
