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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.clarionmedia.infinitum.aop.JoinPoint;
import com.clarionmedia.infinitum.aop.JoinPoint.AdviceLocation;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class BasicJoinPointTest {
	
	private BasicJoinPoint joinPoint;
	private AdviceLocation location = AdviceLocation.Before;
	private MockAspect mockAdvisor;
	private Method method;
	private Object[] args;
	private String beanName;
	private Object target;
	
	@Before
	public void setup() throws NoSuchMethodException, SecurityException {
		mockAdvisor = new MockAspect();
		args = new Object[0];
		beanName = "someBean";
		target = new Object();
		method = mockAdvisor.getClass().getMethod("advice", JoinPoint.class);
		joinPoint = new BasicJoinPoint(mockAdvisor, method, location);
		joinPoint.setArguments(args);
		joinPoint.setBeanName(beanName);
		joinPoint.setMethod(method);
		joinPoint.setTarget(target);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor() {
		new BasicJoinPoint(null, null, null);
		assertTrue("BasicJoinPoint constructor should have thrown an IllegalArgumentException", false);
	}
	
	@Test
	public void testInvoke() throws Exception {
		// Run
		String result = (String) joinPoint.invoke();
		
		// Verify
		assertEquals("invoke should have returned \"invoked\"", "invoked", result);
	}
	
	@Test
	public void testHashCode_equal() {
		// Setup
		BasicJoinPoint otherJoinPoint = new BasicJoinPoint(mockAdvisor, method, location);
		otherJoinPoint.setArguments(args);
		otherJoinPoint.setBeanName(beanName);
		otherJoinPoint.setMethod(method);
		otherJoinPoint.setTarget(target);
		
		// Run
		int firstHash = joinPoint.hashCode();
		int secondHash = otherJoinPoint.hashCode();
		
		// Verify
		assertEquals("Hash codes should be equal", firstHash, secondHash);
	}
	
	@Test
	public void testHashCode_notEqual() {
		// Setup
		BasicJoinPoint otherJoinPoint = new BasicJoinPoint(new Object(), method, AdviceLocation.After);
		otherJoinPoint.setArguments(args);
		otherJoinPoint.setBeanName(beanName);
		otherJoinPoint.setMethod(method);
		otherJoinPoint.setTarget(target);
		
		// Run
		int firstHash = joinPoint.hashCode();
		int secondHash = otherJoinPoint.hashCode();
		
		// Verify
		assertFalse("Hash codes should not be equal", firstHash == secondHash);
	}
	
	@Test
	public void testEquals_equal() {
		// Setup
		BasicJoinPoint otherJoinPoint = new BasicJoinPoint(mockAdvisor, method, location);
		otherJoinPoint.setArguments(args);
		otherJoinPoint.setBeanName(beanName);
		otherJoinPoint.setMethod(method);
		otherJoinPoint.setTarget(target);
		
		// Run
		boolean result = joinPoint.equals(otherJoinPoint);
		
		// Verify
		assertTrue("BasicJoinPoints should be equal", result);
	}
	
	@Test
	public void testEquals_notEqual() {
		// Setup
		BasicJoinPoint otherJoinPoint = new BasicJoinPoint(mockAdvisor, method, AdviceLocation.After);
		otherJoinPoint.setArguments(args);
		otherJoinPoint.setBeanName(beanName);
		otherJoinPoint.setMethod(method);
		otherJoinPoint.setTarget(target);
		
		// Run
		boolean result = joinPoint.equals(otherJoinPoint);
		
		// Verify
		assertFalse("BasicJoinPoints should not be equal", result);
	}
	
	private static class MockAspect {
		
		@SuppressWarnings("unused")
		public String advice(JoinPoint joinPoint) {
			return "invoked";
		}
		
	}

}
