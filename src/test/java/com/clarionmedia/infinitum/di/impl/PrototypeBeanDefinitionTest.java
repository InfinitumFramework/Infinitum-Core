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
package com.clarionmedia.infinitum.di.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.clarionmedia.infinitum.context.exception.InfinitumConfigurationException;
import com.clarionmedia.infinitum.di.AbstractBeanDefinition;
import com.clarionmedia.infinitum.di.BeanFactory;
import com.clarionmedia.infinitum.di.annotation.Autowired;
import com.clarionmedia.infinitum.di.annotation.PostConstruct;
import com.clarionmedia.infinitum.reflection.ClassReflector;

public class PrototypeBeanDefinitionTest {

	private static final String PROPERTY_VALUE = "foo";

	@Mock
	private BeanFactory mockBeanFactory;

	@Mock
	private ClassReflector mockClassReflector;

	@Mock
	private AbstractBeanDefinition mockRelatedBean;

	private BarBean relatedBean;
	private Field injectedField;
	private Method injectedSetter;
	private Field propertyField;
	private PrototypeBeanDefinition beanDefinition;

	@Before
	public void setup() throws SecurityException, NoSuchFieldException, NoSuchMethodException {
		MockitoAnnotations.initMocks(this);
		beanDefinition = new PrototypeBeanDefinition(mockBeanFactory, mockClassReflector);
		beanDefinition.setType(FooBean.class);
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("mProperty", PROPERTY_VALUE);
		beanDefinition.setProperties(properties);
		injectedField = FooBean.class.getDeclaredField("mField");
		beanDefinition.addFieldInjection(injectedField, mockRelatedBean);
		injectedSetter = FooBean.class.getDeclaredMethod("setField", BarBean.class);
		beanDefinition.addSetterInjection(injectedSetter, mockRelatedBean);
		relatedBean = new BarBean();
		when(mockRelatedBean.getBeanInstance()).thenReturn(relatedBean);
		propertyField = FooBean.class.getDeclaredField("mProperty");
		when(mockClassReflector.getField(FooBean.class, "mProperty")).thenReturn(propertyField);
	}

	@Test(expected = InfinitumConfigurationException.class)
	public void testGetBeanInstance_multipleAutowiredConstructors() throws SecurityException, NoSuchMethodException {
		// Setup
		Constructor<FooBean> ctor = FooBean.class.getDeclaredConstructor(BarBean.class);
		List<Constructor<?>> ctors = new ArrayList<Constructor<?>>();
		ctors.add(ctor);
		ctors.add(ctor);
		when(mockClassReflector.getAllConstructors(FooBean.class)).thenReturn(ctors);

		// Run
		beanDefinition.getBeanInstance();

		// Verify
		assertTrue("getBeanInstance should have thrown an InfinitumConfigurationException", false);
	}

	@Test
	public void testGetBeanInstance_autowiredConstructor() throws SecurityException, NoSuchMethodException {
		// Setup
		Constructor<FooBean> ctor = FooBean.class.getDeclaredConstructor(BarBean.class);
		List<Constructor<?>> ctors = new ArrayList<Constructor<?>>();
		ctors.add(ctor);
		when(mockClassReflector.getAllConstructors(FooBean.class)).thenReturn(ctors);
		BarBean beanArg = new BarBean();
		when(mockBeanFactory.findCandidateBean(BarBean.class)).thenReturn(beanArg);
		FooBean bean = new FooBean(beanArg);
		when(mockClassReflector.getClassInstance(ctor, beanArg)).thenReturn(bean);
		Method postConstruct = FooBean.class.getDeclaredMethod("init");
		List<Method> postConstructMethods = new ArrayList<Method>();
		postConstructMethods.add(postConstruct);
		when(mockClassReflector.getAllMethodsAnnotatedWith(FooBean.class, PostConstruct.class)).thenReturn(postConstructMethods);

		// Run
		Object actual = beanDefinition.getBeanInstance();

		// Verify
		verify(mockClassReflector).setFieldValue(bean, injectedField, relatedBean);
		verify(mockClassReflector).invokeMethod(bean, injectedSetter, relatedBean);
		verify(mockClassReflector).setFieldValue(bean, propertyField, PROPERTY_VALUE);
		verify(mockClassReflector).invokeMethod(bean, postConstruct);
		assertEquals("getBeanInstance result should equal the expected value", bean, actual);
	}
	
	@Test
	public void testGetBeanInstance() throws SecurityException, NoSuchMethodException {
		// Setup
		when(mockClassReflector.getAllConstructors(FooBean.class)).thenReturn(new ArrayList<Constructor<?>>());
		BarBean beanArg = new BarBean();
		when(mockBeanFactory.findCandidateBean(BarBean.class)).thenReturn(beanArg);
		FooBean bean = new FooBean();
		when(mockClassReflector.getClassInstance(FooBean.class)).thenReturn(bean);
		Method postConstruct = FooBean.class.getDeclaredMethod("init");
		List<Method> postConstructMethods = new ArrayList<Method>();
		postConstructMethods.add(postConstruct);
		when(mockClassReflector.getAllMethodsAnnotatedWith(FooBean.class, PostConstruct.class)).thenReturn(postConstructMethods);

		// Run
		Object actual = beanDefinition.getBeanInstance();

		// Verify
		verify(mockClassReflector).setFieldValue(bean, injectedField, relatedBean);
		verify(mockClassReflector).invokeMethod(bean, injectedSetter, relatedBean);
		verify(mockClassReflector).setFieldValue(bean, propertyField, PROPERTY_VALUE);
		verify(mockClassReflector).invokeMethod(bean, postConstruct);
		assertEquals("getBeanInstance result should equal the expected value", bean, actual);
	}
	
	@Test
	public void testGetBeanInstance_isPrototype() throws SecurityException, NoSuchMethodException {
		// Setup
		when(mockClassReflector.getAllConstructors(FooBean.class)).thenReturn(new ArrayList<Constructor<?>>());
		BarBean beanArg = new BarBean();
		when(mockBeanFactory.findCandidateBean(BarBean.class)).thenReturn(beanArg);
		FooBean bean1 = new FooBean();
		FooBean bean2 = new FooBean();
		when(mockClassReflector.getClassInstance(FooBean.class)).thenReturn(bean1).thenReturn(bean2);
		Method postConstruct = FooBean.class.getDeclaredMethod("init");
		List<Method> postConstructMethods = new ArrayList<Method>();
		postConstructMethods.add(postConstruct);
		when(mockClassReflector.getAllMethodsAnnotatedWith(FooBean.class, PostConstruct.class)).thenReturn(postConstructMethods);

		// Run
		Object firstActual = beanDefinition.getBeanInstance();
		Object secondActual = beanDefinition.getBeanInstance();

		// Verify
		assertFalse("getBeanInstance results should not reference the same object", firstActual == secondActual);
	}
	
	@Test(expected = InfinitumConfigurationException.class)
	public void testGetBeanInstance_multiplePostConstruct() throws SecurityException, NoSuchMethodException {
		// Setup
		when(mockClassReflector.getAllConstructors(FooBean.class)).thenReturn(new ArrayList<Constructor<?>>());
		BarBean beanArg = new BarBean();
		when(mockBeanFactory.findCandidateBean(BarBean.class)).thenReturn(beanArg);
		FooBean bean = new FooBean();
		when(mockClassReflector.getClassInstance(FooBean.class)).thenReturn(bean);
		Method postConstruct = FooBean.class.getDeclaredMethod("init");
		List<Method> postConstructMethods = new ArrayList<Method>();
		postConstructMethods.add(postConstruct);
		postConstructMethods.add(postConstruct);
		when(mockClassReflector.getAllMethodsAnnotatedWith(FooBean.class, PostConstruct.class)).thenReturn(postConstructMethods);

		// Run
		beanDefinition.getBeanInstance();

		// Verify
		assertTrue("getBeanInstance should have thrown an InfinitumConfigurationException", false);
	}

	@Test
	public void testGetNonProxiedBeanInstance_autowiredConstructor() throws SecurityException, NoSuchMethodException {
		// Setup
		Constructor<FooBean> ctor = FooBean.class.getDeclaredConstructor(BarBean.class);
		List<Constructor<?>> ctors = new ArrayList<Constructor<?>>();
		ctors.add(ctor);
		when(mockClassReflector.getAllConstructors(FooBean.class)).thenReturn(ctors);
		BarBean beanArg = new BarBean();
		when(mockBeanFactory.findCandidateBean(BarBean.class)).thenReturn(beanArg);
		FooBean bean = new FooBean(beanArg);
		when(mockClassReflector.getClassInstance(ctor, beanArg)).thenReturn(bean);
		Method postConstruct = FooBean.class.getDeclaredMethod("init");
		List<Method> postConstructMethods = new ArrayList<Method>();
		postConstructMethods.add(postConstruct);
		when(mockClassReflector.getAllMethodsAnnotatedWith(FooBean.class, PostConstruct.class)).thenReturn(postConstructMethods);

		// Run
		Object actual = beanDefinition.getNonProxiedBeanInstance();

		// Verify
		verify(mockClassReflector).setFieldValue(bean, injectedField, relatedBean);
		verify(mockClassReflector).invokeMethod(bean, injectedSetter, relatedBean);
		verify(mockClassReflector).setFieldValue(bean, propertyField, PROPERTY_VALUE);
		verify(mockClassReflector).invokeMethod(bean, postConstruct);
		assertEquals("getNonProxiedBeanInstance result should equal the expected value", bean, actual);
	}
	
	@Test
	public void testGetNonProxiedBeanInstance() throws SecurityException, NoSuchMethodException {
		// Setup
		when(mockClassReflector.getAllConstructors(FooBean.class)).thenReturn(new ArrayList<Constructor<?>>());
		BarBean beanArg = new BarBean();
		when(mockBeanFactory.findCandidateBean(BarBean.class)).thenReturn(beanArg);
		FooBean bean = new FooBean();
		when(mockClassReflector.getClassInstance(FooBean.class)).thenReturn(bean);
		Method postConstruct = FooBean.class.getDeclaredMethod("init");
		List<Method> postConstructMethods = new ArrayList<Method>();
		postConstructMethods.add(postConstruct);
		when(mockClassReflector.getAllMethodsAnnotatedWith(FooBean.class, PostConstruct.class)).thenReturn(postConstructMethods);

		// Run
		Object actual = beanDefinition.getNonProxiedBeanInstance();

		// Verify
		verify(mockClassReflector).setFieldValue(bean, injectedField, relatedBean);
		verify(mockClassReflector).invokeMethod(bean, injectedSetter, relatedBean);
		verify(mockClassReflector).setFieldValue(bean, propertyField, PROPERTY_VALUE);
		verify(mockClassReflector).invokeMethod(bean, postConstruct);
		assertEquals("getNonProxiedBeanInstance result should equal the expected value", bean, actual);
	}
	
	@Test
	public void testGetNonProxiedBeanInstance_isPrototype() throws SecurityException, NoSuchMethodException {
		// Setup
		when(mockClassReflector.getAllConstructors(FooBean.class)).thenReturn(new ArrayList<Constructor<?>>());
		BarBean beanArg = new BarBean();
		when(mockBeanFactory.findCandidateBean(BarBean.class)).thenReturn(beanArg);
		FooBean bean1 = new FooBean();
		FooBean bean2 = new FooBean();
		when(mockClassReflector.getClassInstance(FooBean.class)).thenReturn(bean1).thenReturn(bean2);
		Method postConstruct = FooBean.class.getDeclaredMethod("init");
		List<Method> postConstructMethods = new ArrayList<Method>();
		postConstructMethods.add(postConstruct);
		when(mockClassReflector.getAllMethodsAnnotatedWith(FooBean.class, PostConstruct.class)).thenReturn(postConstructMethods);

		// Run
		Object firstActual = beanDefinition.getNonProxiedBeanInstance();
		Object secondActual = beanDefinition.getNonProxiedBeanInstance();

		// Verify
		assertFalse("getNonProxiedBeanInstance results should not reference the same object", firstActual == secondActual);
	}
	
	@Test(expected = InfinitumConfigurationException.class)
	public void testGetNonProxiedBeanInstance_multiplePostConstruct() throws SecurityException, NoSuchMethodException {
		// Setup
		when(mockClassReflector.getAllConstructors(FooBean.class)).thenReturn(new ArrayList<Constructor<?>>());
		BarBean beanArg = new BarBean();
		when(mockBeanFactory.findCandidateBean(BarBean.class)).thenReturn(beanArg);
		FooBean bean = new FooBean();
		when(mockClassReflector.getClassInstance(FooBean.class)).thenReturn(bean);
		Method postConstruct = FooBean.class.getDeclaredMethod("init");
		List<Method> postConstructMethods = new ArrayList<Method>();
		postConstructMethods.add(postConstruct);
		postConstructMethods.add(postConstruct);
		when(mockClassReflector.getAllMethodsAnnotatedWith(FooBean.class, PostConstruct.class)).thenReturn(postConstructMethods);

		// Run
		beanDefinition.getNonProxiedBeanInstance();

		// Verify
		assertTrue("getNonProxiedBeanInstance should have thrown an InfinitumConfigurationException", false);
	}

	private static class FooBean {

		@SuppressWarnings("unused")
		private BarBean mBar;
		@SuppressWarnings("unused")
		private BarBean mField;
		@SuppressWarnings("unused")
		private BarBean mVal;
		@SuppressWarnings("unused")
		private String mProperty;

		@Autowired
		public FooBean(BarBean bar) {
			mBar = bar;
		}

		public FooBean() {

		}

		@Autowired
		public void setField(BarBean val) {
			mVal = val;
		}

		@PostConstruct
		public void init() {

		}

	}

	private static class BarBean {

	}

}
