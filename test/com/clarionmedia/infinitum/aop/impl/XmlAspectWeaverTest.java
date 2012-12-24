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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.Context;

import com.clarionmedia.infinitum.aop.AdvisedProxyFactory;
import com.clarionmedia.infinitum.aop.AopProxy;
import com.clarionmedia.infinitum.aop.AspectComponent;
import com.clarionmedia.infinitum.aop.JoinPoint;
import com.clarionmedia.infinitum.aop.Pointcut;
import com.clarionmedia.infinitum.aop.ProceedingJoinPoint;
import com.clarionmedia.infinitum.di.AbstractBeanDefinition;
import com.clarionmedia.infinitum.di.BeanFactory;
import com.clarionmedia.infinitum.exception.InfinitumRuntimeException;
import com.clarionmedia.infinitum.reflection.ClassReflector;
import com.clarionmedia.infinitum.reflection.PackageReflector;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class XmlAspectWeaverTest {
	
	private static final String BEAN_NAME = "someBean";
	
	@Mock
	private BeanFactory mockBeanFactory;
	
	@Mock
	private ClassReflector mockClassReflector;
	
	@Mock
	private PackageReflector mockPackageReflector;
	
	@Mock
	private AdvisedProxyFactory mockProxyFactory;
	
	@Mock
	private AopProxy mockProxy;
	
	@Mock
	private AbstractBeanDefinition mockBeanDefinition;
	
	@SuppressWarnings("deprecation")
	@InjectMocks
	private XmlAspectWeaver aspectWeaver = new XmlAspectWeaver();
	
	private Map<String, AbstractBeanDefinition> mockBeanMap;
	private List<String> mockBean;
	private AspectComponent mockAspect;
	private List<AspectComponent.Advice> adviceList;
	private AspectComponent.Advice withinAdvice;
	private AspectComponent.Advice beansAdviceWithMethod;
	private AspectComponent.Advice beansAdviceWithParameterizedMethod;
	private AspectComponent.Advice beansAdviceWithWildcardMethod;
	private AspectComponent.Advice beansAdviceWithBadMethod;
	private AspectComponent.Advice beansAdviceClassScope;
	private AspectComponent.Advice beansAdviceInvalidPointcut1;
	private AspectComponent.Advice beansAdviceInvalidPointcut2;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		List<AspectComponent> aspects = new ArrayList<AspectComponent>();
		mockAspect = new AspectComponent();
		withinAdvice = new AspectComponent.Advice();
		withinAdvice.setId("beforeAdvice_within");
		withinAdvice.setType("before");
		withinAdvice.setPointcut("within");
		withinAdvice.setValue("java.util");
		beansAdviceWithMethod = new AspectComponent.Advice();
		beansAdviceWithMethod.setId("beforeAdvice_beans");
		beansAdviceWithMethod.setType("before");
		beansAdviceWithMethod.setPointcut("beans");
		beansAdviceWithMethod.setValue(BEAN_NAME + ".toString()");
		beansAdviceWithParameterizedMethod = new AspectComponent.Advice();
		beansAdviceWithParameterizedMethod.setId("beforeAdvice_beans");
		beansAdviceWithParameterizedMethod.setType("before");
		beansAdviceWithParameterizedMethod.setPointcut("beans");
		beansAdviceWithParameterizedMethod.setValue(BEAN_NAME + ".add(java.lang.Object)");
		beansAdviceWithWildcardMethod = new AspectComponent.Advice();
		beansAdviceWithWildcardMethod.setId("beforeAdvice_beans");
		beansAdviceWithWildcardMethod.setType("before");
		beansAdviceWithWildcardMethod.setPointcut("beans");
		beansAdviceWithWildcardMethod.setValue(BEAN_NAME + ".add(*)");
		beansAdviceWithBadMethod = new AspectComponent.Advice();
		beansAdviceWithBadMethod.setId("beforeAdvice_beans");
		beansAdviceWithBadMethod.setType("before");
		beansAdviceWithBadMethod.setPointcut("beans");
		beansAdviceWithBadMethod.setValue(BEAN_NAME + ".fakeMethod()");
		beansAdviceClassScope = new AspectComponent.Advice();
		beansAdviceClassScope.setId("aroundAdvice_beans");
		beansAdviceClassScope.setType("around");
		beansAdviceClassScope.setPointcut("beans");
		beansAdviceClassScope.setValue(BEAN_NAME);
		beansAdviceInvalidPointcut1 = new AspectComponent.Advice();
		beansAdviceInvalidPointcut1.setId("beforeAdvice_beans");
		beansAdviceInvalidPointcut1.setType("before");
		beansAdviceInvalidPointcut1.setPointcut("beans");
		beansAdviceInvalidPointcut1.setValue(BEAN_NAME + ".invalidMethod(");
		beansAdviceInvalidPointcut2 = new AspectComponent.Advice();
		beansAdviceInvalidPointcut2.setId("beforeAdvice_beans");
		beansAdviceInvalidPointcut2.setType("before");
		beansAdviceInvalidPointcut2.setPointcut("beans");
		beansAdviceInvalidPointcut2.setValue(BEAN_NAME + ".)");
		adviceList = new ArrayList<AspectComponent.Advice>();
		mockAspect.setAdvice(adviceList);
		mockAspect.setClassName("com.clarionmedia.infinitum.aop.impl.XmlAspectWeaverTest$MockAspect");
		mockAspect.setId("advice");
		aspects.add(mockAspect);
		aspectWeaver.setAspects(aspects);
		mockBeanMap = new HashMap<String, AbstractBeanDefinition>();
		mockBean = new ArrayList<String>();
		mockBean.add("hello");
		mockBeanMap.put(BEAN_NAME, mockBeanDefinition);
		doReturn(mockBean.getClass()).when(mockBeanDefinition).getType();
		when(mockBeanDefinition.getName()).thenReturn(BEAN_NAME);
		when(mockBeanDefinition.getNonProxiedBeanInstance()).thenReturn(mockBean);
		when(mockBeanFactory.getBeanDefinitions()).thenReturn(mockBeanMap);
	}
	
	@After
	public void tearDown() {
		adviceList.clear();
	}
	
	@Test
	public void testWeave_noAdvice() {
		// Setup
		List<AspectComponent> originalAspects = aspectWeaver.getAspects();
		aspectWeaver.setAspects(new ArrayList<AspectComponent>());
		
		// Run
		aspectWeaver.weave(Robolectric.application, null);
		
		// Verify
		verify(mockBeanFactory, times(0)).loadBean(BEAN_NAME);
		verify(mockProxyFactory, times(0)).createProxy(any(Context.class), any(Object.class), any(Pointcut.class));
		verify(mockBeanFactory, times(0)).getBeanDefinitions();
		
		aspectWeaver.setAspects(originalAspects);
	}

	@Test
	public void testWeave_within() throws SecurityException, NoSuchMethodException {
		// Setup
		adviceList.add(withinAdvice);
		Method advice = MockAspect.class.getMethod("beforeAdvice_within", JoinPoint.class);
		when(mockClassReflector.getMethod(null, "beforeAdvice_within", JoinPoint.class)).thenReturn(advice);
		when(mockClassReflector.getClassInstance(any(Class.class))).thenReturn(new MockAspect());
		when(mockBeanFactory.loadBean(BEAN_NAME)).thenReturn(mockBean);
		when(mockProxyFactory.createProxy(any(Context.class), any(Object.class), any(Pointcut.class))).thenReturn(mockProxy);
		when(mockProxy.getProxy()).thenReturn(mockBean);
		
		// Run
		aspectWeaver.weave(Robolectric.application, null);
		
		// Verify
		verify(mockBeanFactory).loadBean(BEAN_NAME);
		verify(mockProxyFactory).createProxy(any(Context.class), any(Object.class), any(Pointcut.class));
		verify(mockBeanFactory, times(2)).getBeanDefinitions();
		assertTrue("Bean Map should have 1 bean entry", mockBeanMap.entrySet().size() == 1);
	}
	
	@Test
	public void testWeave_beansWithMethod() throws SecurityException, NoSuchMethodException {
		// Setup
		adviceList.add(beansAdviceWithMethod);
		Method advice = MockAspect.class.getMethod("beforeAdvice_beans", JoinPoint.class);
		Method toString = Object.class.getMethod("toString");
		when(mockClassReflector.getMethod(null, "beforeAdvice_beans", JoinPoint.class)).thenReturn(advice);
		when(mockClassReflector.getClassInstance(any(Class.class))).thenReturn(new MockAspect());
		when(mockBeanFactory.loadBean(BEAN_NAME)).thenReturn(mockBean);
		when(mockClassReflector.getMethod(ArrayList.class, "toString")).thenReturn(toString);
		when(mockProxyFactory.createProxy(any(Context.class), any(Object.class), any(Pointcut.class))).thenReturn(mockProxy);
		
		// Run
		aspectWeaver.weave(Robolectric.application, null);
		
		// Verify
		verify(mockBeanFactory, times(2)).loadBean(BEAN_NAME);
		verify(mockProxyFactory).createProxy(any(Context.class), any(Object.class), any(Pointcut.class));
		verify(mockBeanFactory).getBeanDefinitions();
		assertTrue("Bean Map should have 1 bean entry", mockBeanMap.entrySet().size() == 1);
	}
	
	@Test
	public void testWeave_beansWithParameterizedMethod() throws SecurityException, NoSuchMethodException {
		// Setup
		adviceList.add(beansAdviceWithParameterizedMethod);
		Method advice = MockAspect.class.getMethod("beforeAdvice_beans", JoinPoint.class);
		Method add = ArrayList.class.getMethod("add", Object.class);
		when(mockClassReflector.getMethod(null, "beforeAdvice_beans", JoinPoint.class)).thenReturn(advice);
		when(mockClassReflector.getClassInstance(any(Class.class))).thenReturn(new MockAspect());
		when(mockBeanFactory.loadBean(BEAN_NAME)).thenReturn(mockBean);
		when(mockPackageReflector.getClass("java.lang.Object")).thenReturn(null);
		when(mockClassReflector.getMethod(ArrayList.class, "add", (Class<?>) null)).thenReturn(add);
		when(mockProxyFactory.createProxy(any(Context.class), any(Object.class), any(Pointcut.class))).thenReturn(mockProxy);
		
		// Run
		aspectWeaver.weave(Robolectric.application, null);
		
		// Verify
		verify(mockBeanFactory, times(2)).loadBean(BEAN_NAME);
		verify(mockProxyFactory).createProxy(any(Context.class), any(Object.class), any(Pointcut.class));
		verify(mockBeanFactory).getBeanDefinitions();
		assertTrue("Bean Map should have 1 bean entry", mockBeanMap.entrySet().size() == 1);
	}
	
	@Test(expected = InfinitumRuntimeException.class)
	public void testWeave_beansWithParameterizedMethod_notFoundThrowsException() throws SecurityException, NoSuchMethodException {
		// Setup
		adviceList.add(beansAdviceWithParameterizedMethod);
		Method advice = MockAspect.class.getMethod("beforeAdvice_beans", JoinPoint.class);
		when(mockClassReflector.getMethod(null, "beforeAdvice_beans", JoinPoint.class)).thenReturn(advice);
		when(mockClassReflector.getClassInstance(any(Class.class))).thenReturn(new MockAspect());
		when(mockBeanFactory.loadBean(BEAN_NAME)).thenReturn(mockBean);
		when(mockPackageReflector.getClass("java.lang.Object")).thenReturn(null);
		when(mockClassReflector.getMethod(ArrayList.class, "add", (Class<?>) null)).thenReturn(null);
		when(mockProxyFactory.createProxy(any(Context.class), any(Object.class), any(Pointcut.class))).thenReturn(mockProxy);
		
		// Run
		aspectWeaver.weave(Robolectric.application, null);
		
		// Verify
		assertTrue("Weave should have thrown an InfinitumRuntimeException", false);
	}
	
	@Test
	public void testWeave_beansClassScope() throws SecurityException, NoSuchMethodException {
		// Setup
		adviceList.add(beansAdviceClassScope);
		Method advice = MockAspect.class.getMethod("aroundAdvice_beans", ProceedingJoinPoint.class);
		Method toString = Object.class.getMethod("toString");
		when(mockClassReflector.getMethod(null, "aroundAdvice_beans", ProceedingJoinPoint.class)).thenReturn(advice);
		when(mockClassReflector.getClassInstance(any(Class.class))).thenReturn(new MockAspect());
		when(mockBeanFactory.loadBean(BEAN_NAME)).thenReturn(mockBean);
		when(mockClassReflector.getMethod(ArrayList.class, "toString")).thenReturn(toString);
		when(mockProxyFactory.createProxy(any(Context.class), any(Object.class), any(Pointcut.class))).thenReturn(mockProxy);
		
		// Run
		aspectWeaver.weave(Robolectric.application, null);
		
		// Verify
		verify(mockBeanFactory, times(2)).loadBean(BEAN_NAME);
		verify(mockProxyFactory).createProxy(any(Context.class), any(Object.class), any(Pointcut.class));
		verify(mockBeanFactory).getBeanDefinitions();
		assertTrue("Bean Map should have 1 bean entry", mockBeanMap.entrySet().size() == 1);
	}
	
	@Test(expected = InfinitumRuntimeException.class)
	public void testWeave_beansWithBadMethodThrowsException() throws SecurityException, NoSuchMethodException {
		// Setup
		adviceList.add(beansAdviceWithBadMethod);
		Method advice = MockAspect.class.getMethod("beforeAdvice_beans", JoinPoint.class);
		Method toString = Object.class.getMethod("toString");
		when(mockClassReflector.getMethod(null, "beforeAdvice_beans", JoinPoint.class)).thenReturn(advice);
		when(mockClassReflector.getClassInstance(any(Class.class))).thenReturn(new MockAspect());
		when(mockBeanFactory.loadBean(BEAN_NAME)).thenReturn(mockBean);
		when(mockClassReflector.getMethod(ArrayList.class, "toString")).thenReturn(toString);
		when(mockProxyFactory.createProxy(any(Context.class), any(Object.class), any(Pointcut.class))).thenReturn(mockProxy);
		
		// Run
		aspectWeaver.weave(Robolectric.application, null);
		
		// Verify
		assertTrue("Weave should have thrown an InfinitumRuntimeException", false);
	}
	
	@Test(expected = InfinitumRuntimeException.class)
	public void testWeave_beansWithInvalidPointcutThrowsException_malformedMethod() throws SecurityException, NoSuchMethodException {
		// Setup
		adviceList.add(beansAdviceInvalidPointcut1);
		Method advice = MockAspect.class.getMethod("beforeAdvice_beans", JoinPoint.class);
		Method toString = Object.class.getMethod("toString");
		when(mockClassReflector.getMethod(null, "beforeAdvice_beans", JoinPoint.class)).thenReturn(advice);
		when(mockClassReflector.getClassInstance(any(Class.class))).thenReturn(new MockAspect());
		when(mockBeanFactory.loadBean(BEAN_NAME)).thenReturn(mockBean);
		when(mockClassReflector.getMethod(ArrayList.class, "toString")).thenReturn(toString);
		when(mockProxyFactory.createProxy(any(Context.class), any(Object.class), any(Pointcut.class))).thenReturn(mockProxy);
		
		// Run
		aspectWeaver.weave(Robolectric.application, null);
		
		// Verify
		assertTrue("Weave should have thrown an InfinitumRuntimeException", false);
	}
	
	@Test(expected = InfinitumRuntimeException.class)
	public void testWeave_beansWithInvalidPointcutThrowsException_missingMethod() throws SecurityException, NoSuchMethodException {
		// Setup
		adviceList.add(beansAdviceInvalidPointcut2);
		Method advice = MockAspect.class.getMethod("beforeAdvice_beans", JoinPoint.class);
		Method toString = Object.class.getMethod("toString");
		when(mockClassReflector.getMethod(null, "beforeAdvice_beans", JoinPoint.class)).thenReturn(advice);
		when(mockClassReflector.getClassInstance(any(Class.class))).thenReturn(new MockAspect());
		when(mockBeanFactory.loadBean(BEAN_NAME)).thenReturn(mockBean);
		when(mockClassReflector.getMethod(ArrayList.class, "toString")).thenReturn(toString);
		when(mockProxyFactory.createProxy(any(Context.class), any(Object.class), any(Pointcut.class))).thenReturn(mockProxy);
		
		// Run
		aspectWeaver.weave(Robolectric.application, null);
		
		// Verify
		assertTrue("Weave should have thrown an InfinitumRuntimeException", false);
	}
	
	@Test
	public void testWeave_beansWithWildcardMethod() throws SecurityException, NoSuchMethodException {
		// Setup
		adviceList.add(beansAdviceWithWildcardMethod);
		Method advice = MockAspect.class.getMethod("beforeAdvice_beans", JoinPoint.class);
		List<Method> methods = new ArrayList<Method>();
		methods.add(ArrayList.class.getMethod("add", Object.class));
		methods.add(ArrayList.class.getMethod("add", int.class, Object.class));
		when(mockClassReflector.getMethod(null, "beforeAdvice_beans", JoinPoint.class)).thenReturn(advice);
		when(mockClassReflector.getClassInstance(any(Class.class))).thenReturn(new MockAspect());
		when(mockBeanFactory.loadBean(BEAN_NAME)).thenReturn(mockBean);
		when(mockClassReflector.getMethodsByName(ArrayList.class, "add")).thenReturn(methods);
		when(mockProxyFactory.createProxy(any(Context.class), any(Object.class), any(Pointcut.class))).thenReturn(mockProxy);
		
		// Run
		aspectWeaver.weave(Robolectric.application, null);
		
		// Verify
		verify(mockBeanFactory, times(2)).loadBean(BEAN_NAME);
		verify(mockProxyFactory).createProxy(any(Context.class), any(Object.class), any(Pointcut.class));
		verify(mockBeanFactory).getBeanDefinitions();
		assertTrue("Bean Map should have 1 bean entry", mockBeanMap.entrySet().size() == 1);
	}

	private static class MockAspect {

		@SuppressWarnings("unused")
		public void beforeAdvice_within(JoinPoint joinPoint) {

		}
		
		@SuppressWarnings("unused")
		public void beforeAdvice_beans(JoinPoint joinPoint) {

		}
		
		@SuppressWarnings("unused")
		public void aroundAdvice_beans(ProceedingJoinPoint joinPoint) {
			
		}

	}

}
