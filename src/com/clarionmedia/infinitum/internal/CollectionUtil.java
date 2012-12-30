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
     * @param map the {@code Map} to invert
     * @return inverted {@code Map}
     */
    public static <K, V> Map<V, K> invert(Map<K, V> map) {
        Map<V, K> inv = new HashMap<V, K>();
        for (Entry<K, V> entry : map.entrySet())
            inv.put(entry.getValue(), entry.getKey());
        return inv;
    }

}
