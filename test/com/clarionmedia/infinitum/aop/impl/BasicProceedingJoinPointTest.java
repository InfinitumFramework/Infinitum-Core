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
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.clarionmedia.infinitum.aop.ProceedingJoinPoint;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class BasicProceedingJoinPointTest {
	
	private BasicProceedingJoinPoint proceedingJoinPoint;
	private MockAspect mockAdvisor;
	private Method advice;
	private Method method;
	private Object[] args;
	private String beanName;
	private List<String> target;
	
	@Before
	public void setup() throws NoSuchMethodException, SecurityException {
		mockAdvisor = new MockAspect();
		args = new Object[0];
		beanName = "someBean";
		target = new ArrayList<String>();
		target.add("hello");
		advice = mockAdvisor.getClass().getMethod("firstAdvice", ProceedingJoinPoint.class);
		method = ArrayList.class.getMethod("toString");
		proceedingJoinPoint = new BasicProceedingJoinPoint(mockAdvisor, advice);
		proceedingJoinPoint.setArguments(args);
		proceedingJoinPoint.setBeanName(beanName);
		proceedingJoinPoint.setMethod(method);
		proceedingJoinPoint.setTarget(target);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor() {
		new BasicJoinPoint(null, null, null);
		assertTrue("BasicProceedingJoinPoint constructor should have thrown an IllegalArgumentException", false);
	}
	
	@Test
	public void testInvoke_notChained() throws Exception {
		// Run
		String result = (String) proceedingJoinPoint.invoke();
		
		// Verify
		assertEquals("invoke should have returned target return value", "[hello]", result);
	}
	
	@Test
	public void testInvoke_chained() throws Exception {
		// Setup
		BasicProceedingJoinPoint otherJoinPoint = new BasicProceedingJoinPoint(mockAdvisor, advice);
		otherJoinPoint.setArguments(args);
		otherJoinPoint.setBeanName(beanName);
		otherJoinPoint.setMethod(method);
		otherJoinPoint.setTarget(target);
		otherJoinPoint.setNext(proceedingJoinPoint);
		
		// Run
		String result = (String) otherJoinPoint.invoke();
		
		// Verify
		assertEquals("invoke should have returned target return value", "[hello]", result);
	}
	
	@Test
	public void testHashCode_equal() {
		// Setup
		BasicProceedingJoinPoint otherJoinPoint = new BasicProceedingJoinPoint(mockAdvisor, advice);
		otherJoinPoint.setArguments(args);
		otherJoinPoint.setBeanName(beanName);
		otherJoinPoint.setMethod(method);
		otherJoinPoint.setTarget(target);
		
		// Run
		int firstHash = proceedingJoinPoint.hashCode();
		int secondHash = otherJoinPoint.hashCode();
		
		// Verify
		assertEquals("Hash codes should be equal", firstHash, secondHash);
	}
	
	@Test
	public void testHashCode_notEqual() {
		// Setup
		BasicProceedingJoinPoint otherJoinPoint = new BasicProceedingJoinPoint(new Object(), advice);
		otherJoinPoint.setArguments(args);
		otherJoinPoint.setBeanName(beanName);
		otherJoinPoint.setMethod(method);
		otherJoinPoint.setTarget(target);
		
		// Run
		int firstHash = proceedingJoinPoint.hashCode();
		int secondHash = otherJoinPoint.hashCode();
		
		// Verify
		assertFalse("Hash codes should not be equal", firstHash == secondHash);
	}
	
	@Test
	public void testEquals_equal() {
		// Setup
		BasicProceedingJoinPoint otherJoinPoint = new BasicProceedingJoinPoint(mockAdvisor, advice);
		otherJoinPoint.setArguments(args);
		otherJoinPoint.setBeanName(beanName);
		otherJoinPoint.setMethod(method);
		otherJoinPoint.setTarget(target);
		
		// Run
		boolean result = proceedingJoinPoint.equals(otherJoinPoint);
		
		// Verify
		assertTrue("BasicJoinPoints should be equal", result);
	}
	
	@Test
	public void testEquals_notEqual() {
		// Setup
		BasicProceedingJoinPoint otherJoinPoint = new BasicProceedingJoinPoint(mockAdvisor, advice);
		otherJoinPoint.setArguments(args);
		otherJoinPoint.setBeanName("differentBean");
		otherJoinPoint.setMethod(method);
		otherJoinPoint.setTarget(target);
		
		// Run
		boolean result = proceedingJoinPoint.equals(otherJoinPoint);
		
		// Verify
		assertFalse("BasicJoinPoints should not be equal", result);
	}
	
	private static class MockAspect {
		
		@SuppressWarnings("unused")
		public Object firstAdvice(ProceedingJoinPoint joinPoint) throws Exception {
			return joinPoint.proceed();
		}
		
		@SuppressWarnings("unused")
		public Object secondAdvice(ProceedingJoinPoint joinPoint) throws Exception {
			return joinPoint.proceed();
		}
		
	}

}
