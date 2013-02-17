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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>
 * Contains static utility methods for dealing with collections.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/18/12
 * @since 1.0
 */
public class CollectionUtil {

	/**
	 * Inverts the given {@link Map}, assuming a one-to-one key-value
	 * correspondence.
	 * 
	 * @param map
	 *            the {@code Map} to invert
	 * @return inverted {@code Map}
	 */
	public static <K, V> Map<V, K> invert(Map<K, V> map) {
		Map<V, K> inv = new HashMap<V, K>();
		for (Entry<K, V> entry : map.entrySet())
			inv.put(entry.getValue(), entry.getKey());
		return inv;
	}

}
