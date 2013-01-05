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
 * This interface provides reflection methods for working with the application
 * classpath.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 05/17/12
 * @since 1.0
 */
public interface ClasspathReflector {

	/**
	 * Retrieves a {@link Set} of {@link Class} instances whose are contained in
	 * one of the given package names.
	 */
	Set<Class<?>> getPackageClasses(Context context, String... packageNames);

}
