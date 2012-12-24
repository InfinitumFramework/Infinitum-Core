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

package com.clarionmedia.infinitum.aop.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.clarionmedia.infinitum.aop.AopProxy;
import com.clarionmedia.infinitum.aop.Pointcut;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class DelegatingAdvisedProxyFactoryTest {

	private DelegatingAdvisedProxyFactory proxyFactory;

	@Before
	public void setup() {
		proxyFactory = new DelegatingAdvisedProxyFactory();
	}

	@Test
	public void testCreateProxy_dexMakerProxy() {
		AopProxy proxy = proxyFactory.createProxy(Robolectric.application, new Object(), new Pointcut("someBean",
				Object.class));
		assertThat(proxy, is(AdvisedDexMakerProxy.class));
	}

	@Test
	public void testCreateProxy_jdkDynamicProxy() {
		AopProxy proxy = proxyFactory.createProxy(Robolectric.application, new ArrayList<String>(), new Pointcut(
				"someBean", ArrayList.class));
		assertThat(proxy, is(AdvisedJdkDynamicProxy.class));
	}

}
