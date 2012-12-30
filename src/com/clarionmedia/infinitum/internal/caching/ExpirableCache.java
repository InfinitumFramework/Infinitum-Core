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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Provides a time-expiration cache where cached values are asynchronously
 * evicted after a given timeout. Entries can be cached with their own
 * expiration time or rely on a default cache timeout.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 08/14/12
 * @since 1.0
 */
public class ExpirableCache<K, V> implements Map<K, V> {

	/**
	 * The expiration timeout used if none is specified.
	 */
	public static final long DEFAULT_EXPIRATION_TIMEOUT = 60;

	private final ConcurrentMap<K, V> mCache;
	private final ConcurrentMap<K, Long> mTimeoutCache;
	private final long mDefaultExpirationTimeout;
	private final ExecutorService mThreadPool;

	/**
	 * Creates a new {@code ExpirableCache} using the default expiration timeout
	 * of 60 seconds.
	 */
	public ExpirableCache() {
		this(DEFAULT_EXPIRATION_TIMEOUT);
	}

	/**
	 * Creates a new {@code ExpirableCache} with with the given default
	 * expiration time.
	 * 
	 * @param defaultExpiration
	 *            the default expiration time in seconds
	 */
	public ExpirableCache(final long defaultExpiration) {
		if (defaultExpiration <= 0)
			throw new IllegalArgumentException(
					"Cache expiration timeout must be greater than 0.");
		mCache = new ConcurrentHashMap<K, V>();
		mTimeoutCache = new ConcurrentHashMap<K, Long>();
		mDefaultExpirationTimeout = defaultExpiration;
		mThreadPool = Executors.newFixedThreadPool(256);
		scheduleCacheEviction();
	}

	/**
	 * Creates a new {@code ExpirableCache} with with the given default
	 * expiration time.
	 * 
	 * @param defaultExpiration
	 *            the default expiration time in seconds
	 * @param initialCapacity
	 *            the initial capacity of the cache
	 */
	public ExpirableCache(final long defaultExpiration, int initialCapacity) {
		if (defaultExpiration <= 0)
			throw new IllegalArgumentException(
					"Cache expiration timeout must be greater than 0.");
		mCache = new ConcurrentHashMap<K, V>(initialCapacity);
		mTimeoutCache = new ConcurrentHashMap<K, Long>(initialCapacity);
		mDefaultExpirationTimeout = defaultExpiration;
		mThreadPool = Executors.newFixedThreadPool(256);
		scheduleCacheEviction();
	}

	/**
	 * Returns the default expiration timeout for the cache.
	 * 
	 * @return default expiration timeout in seconds
	 */
	public long getDefaultExpirationTimeout() {
		return mDefaultExpirationTimeout;
	}

	/**
	 * Caches the given {@link Object} using the default expiration timeout.
	 * 
	 * @param key
	 *            the cache entry's key
	 * @param object
	 *            the {@code Object} to cache
	 * @return the previous cache entry with the associated key or {@code null}
	 *         if there was none
	 */
	@Override
	public V put(final K key, final V object) {
		mTimeoutCache.put(key, System.currentTimeMillis() + mDefaultExpirationTimeout * 1000);
		return put(key, object, mDefaultExpirationTimeout);
	}

	/**
	 * Caches the given {@link Object} using the given expiration timeout.
	 * 
	 * @param key
	 *            the cache entry's key
	 * @param object
	 *            the {@code Object} to cache
	 * @param expirationTimeout
	 *            the cache entry's expiration timeout in seconds
	 * @return the previous cache entry with the associated key or {@code null}
	 *         if there was none
	 */
	public V put(final K key, final V object, final long expirationTimeout) {
		mTimeoutCache.put(key, System.currentTimeMillis() + expirationTimeout * 1000);
		return mCache.put(key, object);
	}

	/**
	 * Returns the cache entry identified by the given key.
	 * 
	 * @param key
	 *            the key of the {@link Object} to retrieve
	 * @return the cached {@code Object} with the given key or {@code null} if
	 *         no entry exists (or if it expired)
	 */
	@Override
	public V get(final Object key) {
		final Long maxAge = mTimeoutCache.get(key);
		if (maxAge == null)
			return null;
		if (System.currentTimeMillis() > maxAge) {
			mThreadPool.execute(evictFromCache(key));
			return null;
		}
		return mCache.get(key);
	}

	/**
	 * Returns the cache entry identified by the given key.
	 * 
	 * @param key
	 *            the key of the {@link Object} to retrieve
	 * @param type
	 *            the type of the cache entry to retrieve
	 * @return the cached {@code Object} with the given key or {@code null} if
	 *         no entry exists (or if it expired)
	 */
	@SuppressWarnings("unchecked")
	public <R extends V> R get(final K key, final Class<R> type) {
		return (R) get(key);
	}

	@Override
	public synchronized boolean containsKey(Object key) {
		return mCache.containsKey(key);
	}

	@Override
	public synchronized boolean containsValue(Object value) {
		return mCache.containsValue(value);
	}

	@Override
	public synchronized V remove(Object key) {
		return mCache.remove(key);
	}

	@Override
	public synchronized void clear() {
		mCache.clear();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return mCache.entrySet();
	}

	@Override
	public synchronized boolean isEmpty() {
		return mCache.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		return mCache.keySet();
	}

	@Override
	public synchronized void putAll(Map<? extends K, ? extends V> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized int size() {
		return mCache.size();
	}

	@Override
	public Collection<V> values() {
		return mCache.values();
	}

	/**
	 * Schedules a thread pool to periodically sanitize the cache.
	 */
	private void scheduleCacheEviction() {
		Executors.newScheduledThreadPool(2).scheduleWithFixedDelay(
				new Runnable() {
					@Override
					public void run() {
						for (final K key : mTimeoutCache.keySet()) {
							if (System.currentTimeMillis() > mTimeoutCache.get(key))
								mThreadPool.execute(evictFromCache(key));
						}
					}
				}, mDefaultExpirationTimeout / 2, mDefaultExpirationTimeout,
				TimeUnit.SECONDS);
	}

	/**
	 * Returns a {@link Runnable} that evicts the {@link Object} with the given
	 * key from the cache.
	 * 
	 * @param key
	 *            the key of the {@code Object} to evict
	 */
	private final Runnable evictFromCache(final Object key) {
		return new Runnable() {
			public void run() {
				mCache.remove(key);
				mTimeoutCache.remove(key);
			}
		};
	}

}