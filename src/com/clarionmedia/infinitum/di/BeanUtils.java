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

package com.clarionmedia.infinitum.di;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.clarionmedia.infinitum.context.exception.InfinitumConfigurationException;

/**
 * <p>
 * Contains static utility methods for dealing with JavaBeans.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/05/12
 * @since 1.0
 */
public class BeanUtils {

	/**
	 * Resolves an autowire dependency for the given {@link Class}. This will
	 * return an instance of the {@code Class} or one of its derivatives from
	 * the given {@link BeanFactory}. If there is no candidate, it will return
	 * {@code null}. An {@link InfinitumConfigurationException} will be thrown
	 * if more than one candidate is found.
	 * 
	 * @param beanFactory
	 *            the {@code BeanFactory} to load the autowire candidate from
	 * @param clazz
	 *            the {@code Class} of the candidate
	 * @return bean candidate or {@code null} if none exists
	 * @throws InfinitumConfigurationException
	 *             if more than one autowire candidate is found
	 */
	public static Object findCandidateBean(BeanFactory beanFactory, Class<?> clazz) {
		String beanName = findCandidateBeanName(beanFactory, clazz);
		if (beanName == null)
			return null;
		return beanFactory.loadBean(beanName);
	}
	
	/**
	 * Retrieves the name of a bean which satisfies the given {@link Class}.
	 * 
	 * @param beanFactory
	 *            the {@code BeanFactory} to load the autowire candidate from
	 * @param clazz
	 *            the {@code Class} of the candidate
	 * @return bean candidate name or {@code null} if none exists
	 * @throws InfinitumConfigurationException
	 *             if more than one autowire candidate is found
	 */
	public static String findCandidateBeanName(BeanFactory beanFactory, Class<?> clazz) {
		AbstractBeanDefinition candidate = null;
		Map<AbstractBeanDefinition, String> invertedBeanMap = invert(beanFactory.getBeanDefinitions());
		for (AbstractBeanDefinition beanDef : invertedBeanMap.keySet()) {
			if (clazz.isAssignableFrom(beanDef.getType())) {
				// TODO: check if there is more than 1 candidate?
				candidate = beanDef;
				break;
			}
		}
		if (candidate == null)
			return null;
		return candidate.getName();
	}

	private static <V, K> Map<V, K> invert(Map<K, V> map) {
		Map<V, K> inv = new HashMap<V, K>();
		for (Entry<K, V> entry : map.entrySet()) {
			if (inv.containsKey(entry.getValue()))
				throw new InfinitumConfigurationException("More than 1 autowire candidate found of type '"
						+ entry.getValue() + "'.");
			inv.put(entry.getValue(), entry.getKey());
		}
		return inv;
	}

}
