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

package com.clarionmedia.infinitum.internal;

/**
 * <p>
 * Contains static utility methods for dealing with strings.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 03/15/12
 * @since 1.0
 */
public class StringUtil {

	/**
	 * Capitalizes the first letter of the given {@link String}.
	 * 
	 * @param string
	 *            the {@code String} to capitalize
	 * @return capitalized {@code String}
	 */
	public static String capitalizeString(String string) {
		if (string == null)
			return string;
		if (string.length() == 0)
			return string;
		if (string.length() == 1)
			return string.toUpperCase();
		return Character.toUpperCase(string.charAt(0)) + string.substring(1);
	}

	/**
	 * Returns the formatted field name by removing the 'm' prefix found in
	 * Android naming conventions and putting the name to lowercase.
	 * 
	 * @param name
	 *            the field name to format
	 * @return the formatted name
	 */
	public static String formatFieldName(String name) {
		if (!name.startsWith("m") || name.length() <= 1)
			return name.toLowerCase();
		if (Character.isUpperCase(name.charAt(1)))
			return name.substring(1).toLowerCase();
		return name.toLowerCase();
	}

	/**
	 * Returns a camelcase-formatted version of the given {@link String}. For
	 * example, passing in {@code FooBar} will return {@code fooBar}.
	 * 
	 * @param string
	 *            the {@code String} to format
	 * @return the camelcase version of {@code string}
	 */
	public static String toCamelCase(String string) {
		if (string == null || string.length() == 0)
			return string;
		if (string.length() == 1)
			return string.toLowerCase();
		return string.substring(0, 1).toLowerCase() + string.substring(1);
	}

}
