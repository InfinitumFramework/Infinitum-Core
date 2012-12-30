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

package com.clarionmedia.infinitum.internal.caching;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * A {@link Map} implementation that acts as a least-recently-used (LRU) cache.
 * If the maximum cache capacity is reached, the {@link Object} that was last
 * accessed will be evicted. This is implemented using a {@link LinkedHashMap},
 * such that when an {@code Object} is accessed or added, it is placed at the
 * head of the queue. When space is needed, the {@code Object} at the end of the
 * queue is evicted.
 * </p>
 * <p>
 * By default, the cache size is measured in the number of {@code Objects} it
 * can store; however, units can be imposed by overriding the
 * {@link LruCache#sizeOf(Object, Object)} method.
 * </p>
 *
 * @author Tyler Treat
 * @version 1.0 08/08/12
 * @since 1.0
 */
public class LruCache<K, V> implements Map<K, V> {

    private final LinkedHashMap<K, V> mMap;
    private int mSize;
    private int mMaxSize;
    private int mPutCount;
    private int mCreateCount;
    private int mEvictionCount;
    private int mHitCount;
    private int mMissCount;

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is the
     *                maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this
     *                cache.
     */
    public LruCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.mMaxSize = maxSize;
        this.mMap = new LinkedHashMap<K, V>(0, 0.75f, true);
    }

    /**
     * Returns the value for {@code key} if it exists in the cache or can be
     * created by {@code #create}. If a value was returned, it is moved to the
     * head of the queue. This returns null if a value is not cached and cannot
     * be created.
     */
    @SuppressWarnings("unchecked")
    @Override
    public final V get(Object key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        V mapValue;
        synchronized (this) {
            mapValue = mMap.get(key);
            if (mapValue != null) {
                mHitCount++;
                return mapValue;
            }
            mMissCount++;
        }

		/*
         * Attempt to create a value. This may take a long time, and the map may
		 * be different when create() returns. If a conflicting value was added
		 * to the map while create() was working, we leave that value in the map
		 * and release the created value.
		 */

        V createdValue = create(key);
        if (createdValue == null) {
            return null;
        }

        synchronized (this) {
            mCreateCount++;
            mapValue = mMap.put((K) key, createdValue);

            if (mapValue != null) {
                // There was a conflict so undo that last put
                mMap.put((K) key, mapValue);
            } else {
                mSize += safeSizeOf(key, createdValue);
            }
        }

        if (mapValue != null) {
            entryRemoved(false, key, createdValue, mapValue);
            return mapValue;
        } else {
            trimToSize(mMaxSize);
            return createdValue;
        }
    }

    /**
     * Caches {@code value} for {@code key}. The value is moved to the head of
     * the queue.
     *
     * @return the previous value mapped by {@code key}.
     */
    @Override
    public final V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }

        V previous;
        synchronized (this) {
            mPutCount++;
            mSize += safeSizeOf(key, value);
            previous = mMap.put(key, value);
            if (previous != null) {
                mSize -= safeSizeOf(key, previous);
            }
        }

        if (previous != null) {
            entryRemoved(false, key, previous, value);
        }

        trimToSize(mMaxSize);
        return previous;
    }

    /**
     * Checks if the given key is contained in the cache.
     *
     * @return boolean indicating if the key exists
     */
    @Override
    public final boolean containsKey(Object key) {
        synchronized (this) {
            return mMap.containsKey(key);
        }
    }

    /**
     * Removes the entry for {@code key} if it exists.
     *
     * @return the previous value mapped by {@code key}.
     */
    public final V remove(Object key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        V previous;
        synchronized (this) {
            previous = mMap.remove(key);
            if (previous != null) {
                mSize -= safeSizeOf(key, previous);
            }
        }

        if (previous != null) {
            entryRemoved(false, key, previous, null);
        }

        return previous;
    }

    /**
     * Called for entries that have been evicted or removed. This method is
     * invoked when a value is evicted to make space, removed by a call to
     * {@link #remove}, or replaced by a call to {@link #put}. The default
     * implementation does nothing.
     * <p/>
     * <p/>
     * The method is called without synchronization: other threads may access
     * the cache while this method is executing.
     *
     * @param evicted  true if the entry is being removed to make space, false if the
     *                 removal was caused by a {@link #put} or {@link #remove}.
     * @param newValue the new value for {@code key}, if it exists. If non-null, this
     *                 removal was caused by a {@link #put}. Otherwise it was caused
     *                 by an eviction or a {@link #remove}.
     */
    protected void entryRemoved(boolean evicted, Object key, V oldValue, V newValue) {
    }

    /**
     * Called after a cache miss to compute a value for the corresponding key.
     * Returns the computed value or null if no value can be computed. The
     * default implementation returns null.
     * <p/>
     * <p/>
     * The method is called without synchronization: other threads may access
     * the cache while this method is executing.
     * <p/>
     * <p/>
     * If a value for {@code key} exists in the cache when this method returns,
     * the created value will be released with {@link #entryRemoved} and
     * discarded. This can occur when multiple threads request the same key at
     * the same time (causing multiple values to be created), or when one thread
     * calls {@link #put} while another is creating a value for the same key.
     */
    protected V create(Object key) {
        return null;
    }

    private int safeSizeOf(Object key, V value) {
        int result = sizeOf(key, value);
        if (result < 0) {
            throw new IllegalStateException("Negative size: " + key + "="
                    + value);
        }
        return result;
    }

    /**
     * Returns the size of the entry for {@code key} and {@code value} in
     * user-defined units. The default implementation returns 1 so that size is
     * the number of entries and max size is the maximum number of entries.
     * <p/>
     * <p/>
     * An entry's size must not change while it is in the cache.
     */
    protected int sizeOf(Object key, V value) {
        return 1;
    }

    /**
     * Clear the cache, calling {@link #entryRemoved} on each removed entry.
     */
    @Override
    public final void clear() {
        trimToSize(-1); // -1 will evict 0-sized elements
    }

    /**
     * For caches that do not override {@link #sizeOf}, this returns the number
     * of entries in the cache. For all other caches, this returns the sum of
     * the sizes of the entries in this cache.
     */
    public synchronized final int size() {
        return mSize;
    }

    /**
     * For caches that do not override {@link #sizeOf}, this returns the maximum
     * number of entries in the cache. For all other caches, this returns the
     * maximum sum of the sizes of the entries in this cache.
     */
    public synchronized final int maxSize() {
        return mMaxSize;
    }

    /**
     * Returns the number of times {@link #get} returned a value.
     */
    public synchronized final int hitCount() {
        return mHitCount;
    }

    /**
     * Returns the number of times {@link #get} returned null or required a new
     * value to be created.
     */
    public synchronized final int missCount() {
        return mMissCount;
    }

    /**
     * Returns the number of times {@link #create(Object)} returned a value.
     */
    public synchronized final int createCount() {
        return mCreateCount;
    }

    /**
     * Returns the number of times {@link #put} was called.
     */
    public synchronized final int putCount() {
        return mPutCount;
    }

    /**
     * Returns the number of values that have been evicted.
     */
    public synchronized final int evictionCount() {
        return mEvictionCount;
    }

    /**
     * Returns a copy of the current contents of the cache, ordered from least
     * recently accessed to most recently accessed.
     */
    public synchronized final Map<K, V> snapshot() {
        return new LinkedHashMap<K, V>(mMap);
    }

    @Override
    public synchronized final boolean containsValue(Object value) {
        return mMap.containsValue(value);
    }

    @Override
    public synchronized final Set<Entry<K, V>> entrySet() {
        return mMap.entrySet();
    }

    @Override
    public synchronized final boolean isEmpty() {
        return mMap.isEmpty();
    }

    @Override
    public synchronized final Set<K> keySet() {
        return mMap.keySet();
    }

    @Override
    public synchronized final void putAll(Map<? extends K, ? extends V> other) {
        for (Entry<? extends K, ? extends V> entry : other.entrySet())
            put(entry.getKey(), entry.getValue());
    }

    @Override
    public synchronized final Collection<V> values() {
        return mMap.values();
    }

    @Override
    public synchronized final String toString() {
        int accesses = mHitCount + mMissCount;
        int hitPercent = accesses != 0 ? (100 * mHitCount / accesses) : 0;
        return String.format(
                "LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", mMaxSize,
                mHitCount, mMissCount, hitPercent);
    }

    /**
     * @param maxSize the maximum size of the cache before returning. May be -1 to
     *                evict even 0-sized elements.
     */
    private void trimToSize(int maxSize) {
        while (true) {
            K key;
            V value;
            synchronized (this) {
                if (mSize < 0 || (mMap.isEmpty() && mSize != 0)) {
                    throw new IllegalStateException(getClass().getName()
                            + ".sizeOf() is reporting inconsistent results!");
                }

                if (mSize <= maxSize || mMap.isEmpty()) {
                    break;
                }

                Map.Entry<K, V> toEvict = mMap.entrySet().iterator().next();
                key = toEvict.getKey();
                value = toEvict.getValue();
                mMap.remove(key);
                mSize -= safeSizeOf(key, value);
                mEvictionCount++;
            }

            entryRemoved(true, key, value, null);
        }
    }
}