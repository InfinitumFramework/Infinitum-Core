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

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.context.exception.InfinitumConfigurationException;
import com.clarionmedia.infinitum.di.AbstractBeanDefinition;
import com.clarionmedia.infinitum.di.BeanFactory;
import com.clarionmedia.infinitum.di.annotation.Autowired;
import com.clarionmedia.infinitum.exception.InfinitumRuntimeException;
import com.clarionmedia.infinitum.reflection.ClassReflector;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class AutowiredBeanPostProcessorTest {

	private static final String BEAN_CANDIDATE_1 = "fooBean";
	private static final String BEAN_CANDIDATE_2 = "barBean";

	@Mock
	private InfinitumContext mockContext;

	@Mock
	private AbstractBeanDefinition mockBeanDefinition;

	@Mock
	private AbstractBeanDefinition mockInjectedBean1;

	@Mock
	private AbstractBeanDefinition mockInjectedBean2;

	@Mock
	private ClassReflector mockClassReflector;

	@Mock
	private BeanFactory mockBeanFactory;

	private AutowiredBeanPostProcessor beanPostProcessor;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		doReturn(FooBean.class).when(mockBeanDefinition).getType();
		doReturn(BarBean.class).when(mockInjectedBean1).getType();
		doReturn(BarBean.class).when(mockInjectedBean2).getType();
		when(mockInjectedBean1.getName()).thenReturn(BEAN_CANDIDATE_1);
		when(mockInjectedBean2.getName()).thenReturn(BEAN_CANDIDATE_2);
		beanPostProcessor = new AutowiredBeanPostProcessor();
		beanPostProcessor.setClassReflector(mockClassReflector);
		when(mockContext.getBeanFactory()).thenReturn(mockBeanFactory);
		Map<String, AbstractBeanDefinition> beans = new HashMap<String, AbstractBeanDefinition>();
		beans.put(BEAN_CANDIDATE_1, mockInjectedBean1);
		beans.put(BEAN_CANDIDATE_2, mockInjectedBean2);
		when(mockBeanFactory.getBeanDefinitions()).thenReturn(beans);
	}

	@Test
	public void testPostProcessBean_noFieldsOrMethods() {
		// Setup
		when(mockClassReflector.getAllFields(FooBean.class)).thenReturn(new ArrayList<Field>());
		when(mockClassReflector.getAllMethods(FooBean.class)).thenReturn(new ArrayList<Method>());

		// Run
		beanPostProcessor.postProcessBean(mockContext, mockBeanDefinition);

		// Verify
		verify(mockClassReflector).getAllFields(FooBean.class);
		verify(mockClassReflector).getAllMethods(FooBean.class);
		verify(mockBeanDefinition, times(2)).getType();
		verify(mockBeanDefinition, times(0)).addFieldInjection(any(Field.class), any(AbstractBeanDefinition.class));
		verify(mockBeanDefinition, times(0)).addSetterInjection(any(Method.class), any(AbstractBeanDefinition.class));
		verify(mockBeanFactory, times(0)).findCandidateBeanName(any(Class.class));
		verify(mockBeanFactory, times(0)).getBeanDefinition(any(String.class));
	}

	@Test
	public void testPostProcessBean_noMethods_fieldCandidate() throws SecurityException, NoSuchFieldException {
		// Setup
		List<Field> fields = new ArrayList<Field>();
		Field field = FooBean.class.getDeclaredField("mFoo");
		fields.add(field);
		when(mockClassReflector.getAllFields(FooBean.class)).thenReturn(fields);
		when(mockClassReflector.getAllMethods(FooBean.class)).thenReturn(new ArrayList<Method>());
		when(mockBeanFactory.getBeanDefinition(BEAN_CANDIDATE_1)).thenReturn(mockInjectedBean1);

		// Run
		beanPostProcessor.postProcessBean(mockContext, mockBeanDefinition);

		// Verify
		verify(mockClassReflector).getAllFields(FooBean.class);
		verify(mockClassReflector).getAllMethods(FooBean.class);
		verify(mockBeanDefinition, times(2)).getType();
		verify(mockBeanDefinition).addFieldInjection(field, mockInjectedBean1);
		verify(mockBeanDefinition, times(0)).addSetterInjection(any(Method.class), any(AbstractBeanDefinition.class));
		verify(mockBeanFactory, times(0)).findCandidateBeanName(any(Class.class));
		verify(mockBeanFactory).getBeanDefinition(BEAN_CANDIDATE_1);
	}

	@Test
	public void testPostProcessBean_noMethods_fieldNoCandidate_found() throws SecurityException, NoSuchFieldException {
		// Setup
		List<Field> fields = new ArrayList<Field>();
		Field field = FooBean.class.getDeclaredField("mBar");
		fields.add(field);
		when(mockClassReflector.getAllFields(FooBean.class)).thenReturn(fields);
		when(mockClassReflector.getAllMethods(FooBean.class)).thenReturn(new ArrayList<Method>());
		when(mockBeanFactory.getBeanDefinition(BEAN_CANDIDATE_2)).thenReturn(mockInjectedBean2);
		when(mockBeanFactory.findCandidateBeanName(field.getType())).thenReturn(BEAN_CANDIDATE_2);

		// Run
		beanPostProcessor.postProcessBean(mockContext, mockBeanDefinition);

		// Verify
		verify(mockClassReflector).getAllFields(FooBean.class);
		verify(mockClassReflector).getAllMethods(FooBean.class);
		verify(mockBeanDefinition, times(2)).getType();
		verify(mockBeanDefinition).addFieldInjection(field, mockInjectedBean2);
		verify(mockBeanDefinition, times(0)).addSetterInjection(any(Method.class), any(AbstractBeanDefinition.class));
		verify(mockBeanFactory).findCandidateBeanName(field.getType());
		verify(mockBeanFactory).getBeanDefinition(BEAN_CANDIDATE_2);
	}

	@Test(expected = InfinitumRuntimeException.class)
	public void testPostProcessBean_noMethods_fieldNoCandidate_notFound() throws SecurityException, NoSuchFieldException {
		// Setup
		List<Field> fields = new ArrayList<Field>();
		Field field = FooBean.class.getDeclaredField("mBar");
		fields.add(field);
		when(mockClassReflector.getAllFields(FooBean.class)).thenReturn(fields);
		when(mockClassReflector.getAllMethods(FooBean.class)).thenReturn(new ArrayList<Method>());
		when(mockBeanFactory.getBeanDefinition(BEAN_CANDIDATE_2)).thenReturn(mockInjectedBean2);
		when(mockBeanFactory.findCandidateBeanName(field.getType())).thenReturn(null);

		// Run
		beanPostProcessor.postProcessBean(mockContext, mockBeanDefinition);

		// Verify
		assertTrue("postProcessBean should have thrown an InfinitumRuntimeException", false);
	}

	@Test
	public void testPostProcessBean_noFields_methodCandidate() throws SecurityException, NoSuchMethodException {
		// Setup
		List<Method> methods = new ArrayList<Method>();
		Method method = FooBean.class.getDeclaredMethod("setFoo1", BarBean.class);
		methods.add(method);
		when(mockClassReflector.getAllFields(FooBean.class)).thenReturn(new ArrayList<Field>());
		when(mockClassReflector.getAllMethods(FooBean.class)).thenReturn(methods);
		when(mockBeanFactory.getBeanDefinition(BEAN_CANDIDATE_1)).thenReturn(mockInjectedBean1);

		// Run
		beanPostProcessor.postProcessBean(mockContext, mockBeanDefinition);

		// Verify
		verify(mockClassReflector).getAllFields(FooBean.class);
		verify(mockClassReflector).getAllMethods(FooBean.class);
		verify(mockBeanDefinition, times(2)).getType();
		verify(mockBeanDefinition, times(0)).addFieldInjection(any(Field.class), any(AbstractBeanDefinition.class));
		verify(mockBeanDefinition).addSetterInjection(method, mockInjectedBean1);
		verify(mockBeanFactory, times(0)).findCandidateBeanName(any(Class.class));
		verify(mockBeanFactory).getBeanDefinition(BEAN_CANDIDATE_1);
	}

	@Test
	public void testPostProcessBean_noFields_methodNoCandidate_found() throws SecurityException, NoSuchMethodException {
		// Setup
		List<Method> methods = new ArrayList<Method>();
		Method method = FooBean.class.getDeclaredMethod("setFoo2", BarBean.class);
		methods.add(method);
		when(mockClassReflector.getAllMethods(FooBean.class)).thenReturn(methods);
		when(mockClassReflector.getAllFields(FooBean.class)).thenReturn(new ArrayList<Field>());
		when(mockBeanFactory.getBeanDefinition(BEAN_CANDIDATE_2)).thenReturn(mockInjectedBean2);
		when(mockBeanFactory.findCandidateBeanName(method.getParameterTypes()[0])).thenReturn(BEAN_CANDIDATE_2);

		// Run
		beanPostProcessor.postProcessBean(mockContext, mockBeanDefinition);

		// Verify
		verify(mockClassReflector).getAllFields(FooBean.class);
		verify(mockClassReflector).getAllMethods(FooBean.class);
		verify(mockBeanDefinition, times(2)).getType();
		verify(mockBeanDefinition, times(0)).addFieldInjection(any(Field.class), any(AbstractBeanDefinition.class));
		verify(mockBeanDefinition).addSetterInjection(method, mockInjectedBean2);
		verify(mockBeanFactory).findCandidateBeanName(method.getParameterTypes()[0]);
		verify(mockBeanFactory).getBeanDefinition(BEAN_CANDIDATE_2);
	}

	@Test(expected = InfinitumRuntimeException.class)
	public void testPostProcessBean_noFields_methodNoCandidate_notFound() throws SecurityException, NoSuchMethodException {
		// Setup
		List<Method> methods = new ArrayList<Method>();
		Method method = FooBean.class.getDeclaredMethod("setFoo2", BarBean.class);
		methods.add(method);
		when(mockClassReflector.getAllMethods(FooBean.class)).thenReturn(methods);
		when(mockClassReflector.getAllFields(FooBean.class)).thenReturn(new ArrayList<Field>());
		when(mockBeanFactory.getBeanDefinition(BEAN_CANDIDATE_2)).thenReturn(mockInjectedBean2);
		when(mockBeanFactory.findCandidateBeanName(method.getParameterTypes()[0])).thenReturn(null);

		// Run
		beanPostProcessor.postProcessBean(mockContext, mockBeanDefinition);

		// Verify
		assertTrue("postProcessBean should have thrown an InfinitumRuntimeException", false);
	}

	@Test(expected = InfinitumConfigurationException.class)
	public void testPostProcessBean_noFields_methodNoCandidate_invalidSetter() throws SecurityException, NoSuchMethodException {
		// Setup
		List<Method> methods = new ArrayList<Method>();
		Method method = FooBean.class.getDeclaredMethod("setFoo3", BarBean.class, int.class);
		methods.add(method);
		when(mockClassReflector.getAllMethods(FooBean.class)).thenReturn(methods);
		when(mockClassReflector.getAllFields(FooBean.class)).thenReturn(new ArrayList<Field>());
		when(mockBeanFactory.getBeanDefinition(BEAN_CANDIDATE_2)).thenReturn(mockInjectedBean2);
		when(mockBeanFactory.findCandidateBeanName(method.getParameterTypes()[0])).thenReturn(BEAN_CANDIDATE_2);

		// Run
		beanPostProcessor.postProcessBean(mockContext, mockBeanDefinition);

		// Verify
		assertTrue("postProcessBean should have thrown an InfinitumConfigurationException", false);
	}

	private class FooBean {

		@Autowired(BEAN_CANDIDATE_1)
		private BarBean mFoo;

		@Autowired
		private BarBean mBar;

		@Autowired(BEAN_CANDIDATE_1)
		private void setFoo1(BarBean foo) {
			mFoo = foo;
		}

		@Autowired
		private void setFoo2(BarBean foo) {
			mFoo = foo;
		}

		@Autowired
		private void setFoo3(BarBean foo, int x) {
			mFoo = foo;
		}

	}

	private class BarBean {

	}

}
