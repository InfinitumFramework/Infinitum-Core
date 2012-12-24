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

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.Context;

import com.clarionmedia.infinitum.aop.AdvisedProxyFactory;
import com.clarionmedia.infinitum.aop.AopProxy;
import com.clarionmedia.infinitum.aop.JoinPoint;
import com.clarionmedia.infinitum.aop.Pointcut;
import com.clarionmedia.infinitum.aop.ProceedingJoinPoint;
import com.clarionmedia.infinitum.aop.annotation.Aspect;
import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.di.AbstractBeanDefinition;
import com.clarionmedia.infinitum.di.BeanFactory;
import com.clarionmedia.infinitum.exception.InfinitumRuntimeException;
import com.clarionmedia.infinitum.reflection.ClassReflector;
import com.clarionmedia.infinitum.reflection.PackageReflector;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class AnnotationsAspectWeaverTest {
	
	private static final String BEAN_NAME = "someBean";
	
	private InfinitumContext mockInfinitumContext = mock(InfinitumContext.class);

	@Mock
	private BeanFactory mockBeanFactory;
	
	@Mock
	private ClassReflector mockClassReflector;
	
	@SuppressWarnings("unused")
	@Mock
	private PackageReflector mockPackageReflector;
	
	@Mock
	private AdvisedProxyFactory mockProxyFactory;
	
	@Mock
	private AopProxy mockProxy;
	
	@Mock
	private AbstractBeanDefinition mockBeanDefinition;
	
	@InjectMocks
	private AnnotationsAspectWeaver aspectWeaver = new AnnotationsAspectWeaver(mockInfinitumContext);
	
	private Map<String, AbstractBeanDefinition> mockBeanMap;
	
	private List<String> mockBean;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockBean = new ArrayList<String>();
		mockBean.add("hello");
		mockBeanMap = new HashMap<String, AbstractBeanDefinition>();
		mockBeanMap.put(BEAN_NAME, mockBeanDefinition);
		doReturn(mockBean.getClass()).when(mockBeanDefinition).getType();
		when(mockBeanDefinition.getName()).thenReturn(BEAN_NAME);
		when(mockBeanDefinition.getNonProxiedBeanInstance()).thenReturn(mockBean);
		when(mockBeanFactory.getBeanDefinitions()).thenReturn(mockBeanMap);
	}
	
	@Test
	public void testWeave_noAdvice() {
		// Setup
		Set<Class<?>> aspects = new HashSet<Class<?>>();
		
		// Run
		aspectWeaver.weave(Robolectric.application, aspects);
		
		// Verify
		verify(mockBeanFactory, times(0)).loadBean(BEAN_NAME);
		verify(mockProxyFactory, times(0)).createProxy(any(Context.class), any(Object.class), any(Pointcut.class));
		verify(mockBeanFactory, times(0)).getBeanDefinitions();
	}

	@Test
	public void testWeave_within() throws SecurityException, NoSuchMethodException {
		// Setup
		Set<Class<?>> aspects = new HashSet<Class<?>>();
		aspects.add(MockAspect.class);
		Method advice = MockAspect.class.getMethod("beforeAdvice_within",
				JoinPoint.class);
		List<Method> adviceMethods = new ArrayList<Method>();
		adviceMethods.add(advice);
		when(mockClassReflector.getClassInstance(MockAspect.class))
				.thenReturn(new MockAspect());
		when(mockClassReflector.getAllMethodsAnnotatedWith(MockAspect.class,
				com.clarionmedia.infinitum.aop.annotation.Before.class))
				.thenReturn(adviceMethods);
		when(mockProxyFactory.createProxy(any(Context.class), any(Object.class), any(Pointcut.class))).thenReturn(mockProxy);
		when(mockProxy.getProxy()).thenReturn(mockBean);
		
		// Run
		aspectWeaver.weave(Robolectric.application, aspects);
		
		// Verify
		verify(mockBeanFactory).loadBean(BEAN_NAME);
		verify(mockProxyFactory).createProxy(any(Context.class), any(Object.class), any(Pointcut.class));
		verify(mockBeanFactory, times(2)).getBeanDefinitions();
		assertTrue("Bean Map should have 1 bean entry", mockBeanMap.entrySet().size() == 1);
	}
	
	@Test
	public void testWeave_beans() throws SecurityException, NoSuchMethodException {
		// Setup
		Set<Class<?>> aspects = new HashSet<Class<?>>();
		aspects.add(MockAspect.class);
		Method advice = MockAspect.class.getMethod("beforeAdvice_beans", JoinPoint.class);
		Method toString = Object.class.getMethod("toString");
		List<Method> adviceMethods = new ArrayList<Method>();
		adviceMethods.add(advice);
		when(mockClassReflector.getClassInstance(MockAspect.class))
				.thenReturn(new MockAspect());
		when(mockClassReflector.getAllMethodsAnnotatedWith(MockAspect.class,
				com.clarionmedia.infinitum.aop.annotation.Before.class))
				.thenReturn(adviceMethods);
		when(mockBeanFactory.loadBean(BEAN_NAME)).thenReturn(mockBean);
		when(mockClassReflector.getMethod(ArrayList.class, "toString")).thenReturn(toString);
		when(mockProxyFactory.createProxy(any(Context.class), any(Object.class), any(Pointcut.class))).thenReturn(mockProxy);
		when(mockProxy.getProxy()).thenReturn(mockBean);
		
		// Run
		aspectWeaver.weave(Robolectric.application, aspects);
		
		// Verify
		verify(mockBeanFactory, times(2)).loadBean(BEAN_NAME);
		verify(mockProxyFactory).createProxy(any(Context.class), any(Object.class), any(Pointcut.class));
		verify(mockBeanFactory).getBeanDefinitions();
		assertTrue("Bean Map should have 1 bean entry", mockBeanMap.entrySet().size() == 1);
	}
	
	@Test
	public void testWeave_beansWildcard() throws SecurityException, NoSuchMethodException {
		// Setup
		Set<Class<?>> aspects = new HashSet<Class<?>>();
		aspects.add(MockAspect.class);
		Method advice = MockAspect.class.getMethod("beforeAdvice_beansWildcard", JoinPoint.class);
		List<Method> methods = new ArrayList<Method>();
		methods.add(ArrayList.class.getMethod("add", Object.class));
		methods.add(ArrayList.class.getMethod("add", int.class, Object.class));
		List<Method> adviceMethods = new ArrayList<Method>();
		adviceMethods.add(advice);
		when(mockClassReflector.getClassInstance(MockAspect.class))
				.thenReturn(new MockAspect());
		when(mockClassReflector.getAllMethodsAnnotatedWith(MockAspect.class,
				com.clarionmedia.infinitum.aop.annotation.Before.class))
				.thenReturn(adviceMethods);
		when(mockBeanFactory.loadBean(BEAN_NAME)).thenReturn(mockBean);
		when(mockClassReflector.getMethodsByName(ArrayList.class, "add")).thenReturn(methods);
		when(mockProxyFactory.createProxy(any(Context.class), any(Object.class), any(Pointcut.class))).thenReturn(mockProxy);
		when(mockProxy.getProxy()).thenReturn(mockBean);
		
		// Run
		aspectWeaver.weave(Robolectric.application, aspects);
		
		// Verify
		verify(mockBeanFactory, times(2)).loadBean(BEAN_NAME);
		verify(mockProxyFactory).createProxy(any(Context.class), any(Object.class), any(Pointcut.class));
		verify(mockBeanFactory).getBeanDefinitions();
		assertTrue("Bean Map should have 1 bean entry", mockBeanMap.entrySet().size() == 1);
	}
	
	@Test(expected = InfinitumRuntimeException.class)
	public void testWeave_beansWithBadMethodThrowsException() throws SecurityException, NoSuchMethodException {
		// Setup
		Set<Class<?>> aspects = new HashSet<Class<?>>();
		aspects.add(MockAspect.class);
		Method advice = MockAspect.class.getMethod("aroundAdvice_beansInvalidMethod", ProceedingJoinPoint.class);
		List<Method> methods = new ArrayList<Method>();
		methods.add(ArrayList.class.getMethod("add", Object.class));
		methods.add(ArrayList.class.getMethod("add", int.class, Object.class));
		List<Method> adviceMethods = new ArrayList<Method>();
		adviceMethods.add(advice);
		when(mockClassReflector.getClassInstance(MockAspect.class))
				.thenReturn(new MockAspect());
		when(mockClassReflector.getAllMethodsAnnotatedWith(MockAspect.class,
				com.clarionmedia.infinitum.aop.annotation.Around.class))
				.thenReturn(adviceMethods);
		when(mockBeanFactory.loadBean(BEAN_NAME)).thenReturn(mockBean);
		when(mockClassReflector.getMethodsByName(ArrayList.class, "add")).thenReturn(methods);
		when(mockProxyFactory.createProxy(any(Context.class), any(Object.class), any(Pointcut.class))).thenReturn(mockProxy);
		when(mockProxy.getProxy()).thenReturn(mockBean);
		
		// Run
		aspectWeaver.weave(Robolectric.application, aspects);
		
		// Verify
		assertTrue("Weave should have thrown an InfinitumRuntimeException", false);
	}
	
	@Test
	public void testWeave_beansWithMethod() throws SecurityException, NoSuchMethodException {
		// Setup
		Set<Class<?>> aspects = new HashSet<Class<?>>();
		aspects.add(MockAspect.class);
		Method advice = MockAspect.class.getMethod("aroundAdvice_beansWithMethod", ProceedingJoinPoint.class);
		Method add = ArrayList.class.getMethod("add", Object.class);
		List<Method> adviceMethods = new ArrayList<Method>();
		adviceMethods.add(advice);
		when(mockClassReflector.getClassInstance(MockAspect.class))
				.thenReturn(new MockAspect());
		when(mockClassReflector.getAllMethodsAnnotatedWith(MockAspect.class,
				com.clarionmedia.infinitum.aop.annotation.Around.class))
				.thenReturn(adviceMethods);
		when(mockBeanFactory.loadBean(BEAN_NAME)).thenReturn(mockBean);
		when(mockClassReflector.getMethod(ArrayList.class, "add", (Class<?>) null)).thenReturn(add);
		when(mockProxyFactory.createProxy(any(Context.class), any(Object.class), any(Pointcut.class))).thenReturn(mockProxy);
		when(mockProxy.getProxy()).thenReturn(mockBean);
		
		// Run
		aspectWeaver.weave(Robolectric.application, aspects);
		
		// Verify
		verify(mockBeanFactory, times(2)).loadBean(BEAN_NAME);
		verify(mockProxyFactory).createProxy(any(Context.class), any(Object.class), any(Pointcut.class));
		verify(mockBeanFactory).getBeanDefinitions();
		assertTrue("Bean Map should have 1 bean entry", mockBeanMap.entrySet().size() == 1);
	}

	@Aspect
	private static class MockAspect {

		@SuppressWarnings("unused")
		@com.clarionmedia.infinitum.aop.annotation.Before(within = { "java.util" })
		public void beforeAdvice_within(JoinPoint joinPoint) {

		}
		
		@SuppressWarnings("unused")
		@com.clarionmedia.infinitum.aop.annotation.Before(beans = { BEAN_NAME + ".toString()" })
		public void beforeAdvice_beans(JoinPoint joinPoint) {

		}
		
		@SuppressWarnings("unused")
		@com.clarionmedia.infinitum.aop.annotation.Before(beans = { BEAN_NAME + ".add(*)" })
		public void beforeAdvice_beansWildcard(JoinPoint joinPoint) {

		}
		
		@SuppressWarnings("unused")
		@com.clarionmedia.infinitum.aop.annotation.Around(beans = { BEAN_NAME + ".fakeMethod()" })
		public void aroundAdvice_beansInvalidMethod(ProceedingJoinPoint joinPoint) {
			
		}
		
		@SuppressWarnings("unused")
		@com.clarionmedia.infinitum.aop.annotation.Around(beans = { BEAN_NAME + ".add(java.lang.Object)" })
		public void aroundAdvice_beansWithMethod(ProceedingJoinPoint joinPoint) {

		}

	}

}
