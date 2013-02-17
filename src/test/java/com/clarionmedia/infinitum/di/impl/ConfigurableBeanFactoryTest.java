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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.clarionmedia.infinitum.di.XmlBean;
import com.clarionmedia.infinitum.reflection.ClassReflector;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ConfigurableBeanFactoryTest {

	@Mock
	private InfinitumContext mockContext;
	
	@Mock
	private ClassReflector mockClassReflector;
	
	@Mock
	private Map<String, AbstractBeanDefinition> mockBeanMap;
	
	@Mock
	private AbstractBeanDefinition mockBeanDefinition;

	private ConfigurableBeanFactory beanFactory;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		beanFactory = new ConfigurableBeanFactory(mockContext, mockClassReflector, mockBeanMap);
	}
	
	@Test(expected = InfinitumConfigurationException.class)
	public void testLoadBean_notResolved() {
		// Setup
		String name = "bean";
		when(mockBeanMap.containsKey(name)).thenReturn(false);
		
		// Run
		beanFactory.loadBean(name);
		
		// Verify
		assertTrue("loadBean should throw an InfinitumConfigurationException", false);
	}
	
	@Test
	public void testLoadBean_resolved() {
		// Setup
		String name = "bean";
		when(mockBeanMap.containsKey(name)).thenReturn(true);
		when(mockBeanMap.get(name)).thenReturn(mockBeanDefinition);
		Integer expected = 42;
		when(mockBeanDefinition.getBeanInstance()).thenReturn(expected);
		
		// Run
		Object actual = beanFactory.loadBean(name);
		
		// Verify
		verify(mockBeanMap).containsKey(name);
		verify(mockBeanMap).get(name);
		verify(mockBeanDefinition).getBeanInstance();
		assertEquals("loadBean result should equal the expected value", expected, actual);
	}
	
	@Test(expected = InfinitumConfigurationException.class)
	public void testTypeSafeLoadBean_notResolved() {
		// Setup
		String name = "bean";
		when(mockBeanMap.containsKey(name)).thenReturn(false);
		
		// Run
		beanFactory.loadBean(name, Integer.class);
		
		// Verify
		assertTrue("loadBean should throw an InfinitumConfigurationException", false);
	}
	
	@Test(expected = InfinitumConfigurationException.class)
	public void testTypeSafeLoadBean_typeMismatch() {
		// Setup
		String name = "bean";
		when(mockBeanMap.containsKey(name)).thenReturn(true);
		when(mockBeanMap.get(name)).thenReturn(mockBeanDefinition);
		Integer bean = 42;
		when(mockBeanDefinition.getBeanInstance()).thenReturn(bean);
		
		// Run
		beanFactory.loadBean(name, String.class);
		
		// Verify
		assertTrue("loadBean should throw an InfinitumConfigurationException", false);
	}
	
	@Test
	public void testTypeSafeLoadBean_resolved() {
		// Setup
		String name = "bean";
		when(mockBeanMap.containsKey(name)).thenReturn(true);
		when(mockBeanMap.get(name)).thenReturn(mockBeanDefinition);
		Integer expected = 42;
		when(mockBeanDefinition.getBeanInstance()).thenReturn(expected);
		
		// Run
		Integer actual = beanFactory.loadBean(name, Integer.class);
		
		// Verify
		verify(mockBeanMap).containsKey(name);
		verify(mockBeanMap).get(name);
		verify(mockBeanDefinition).getBeanInstance();
		assertEquals("loadBean result should equal the expected value", expected, actual);
	}
	
	@Test
	public void testBeanExists() {
		// Setup
		String name = "bean";
		boolean expected = true;
		when(mockBeanMap.containsKey(name)).thenReturn(expected);
		
		// Run
		boolean actual = beanFactory.beanExists(name);
		
		// Verify
		verify(mockBeanMap).containsKey(name);
		assertEquals("beanExists result should equal the expected value", expected, actual);
	}
	
	@Test
	public void testRegisterBeans_null() {
		// Run
		beanFactory.registerBeans(null);
		
		// Verify
		verify(mockClassReflector, times(0)).getClass(any(String.class));
		verify(mockBeanMap, times(0)).put(any(String.class), any(AbstractBeanDefinition.class));
	}
	
	@Test
	public void testRegisterBeans_noBeans() {
		// Run
		beanFactory.registerBeans(new ArrayList<XmlBean>());
		
		// Verify
		verify(mockClassReflector, times(0)).getClass(any(String.class));
		verify(mockBeanMap, times(0)).put(any(String.class), any(AbstractBeanDefinition.class));
	}
	
	@Test
	public void testRegisterBeans() {
		// Setup
		List<XmlBean> beans = new ArrayList<XmlBean>();
		XmlBean bean = new XmlBean();
		String clazz = "java.lang.Integer";
		bean.setClassName(clazz);
		String name = "bean";
		bean.setId(name);
		bean.setScope("singleton");
		List<XmlBean.Property> properties = new ArrayList<XmlBean.Property>();
		XmlBean.Property property = new XmlBean.Property();
		property.setName("prop");
		property.setValue("42");
		properties.add(property);
		bean.setProperties(properties);
		beans.add(bean);
		doReturn(Integer.class).when(mockClassReflector).getClass(clazz);
		
		// Run
		beanFactory.registerBeans(beans);
		
		// Verify
		verify(mockClassReflector).getClass(clazz);
		verify(mockBeanMap).put(eq(name), any(AbstractBeanDefinition.class));
	}
	
	@Test
	public void testRegisterBean_null() {
		// Run
		beanFactory.registerBean(null);
		
		// Verify
		verify(mockBeanMap, times(0)).put(any(String.class), any(AbstractBeanDefinition.class));
	}
	
	@Test
	public void testRegisterBean() {
		// Setup
		String name = "bean";
		when(mockBeanDefinition.getName()).thenReturn(name);
		
		// Run
		beanFactory.registerBean(mockBeanDefinition);
		
		// Verify
		verify(mockBeanMap).put(name, mockBeanDefinition);
	}
	
	@Test
	public void testGetBeanDefinition() {
		// Setup
		String name = "bean";
		when(mockBeanMap.get(name)).thenReturn(mockBeanDefinition);
		
		// Run
		AbstractBeanDefinition actual = beanFactory.getBeanDefinition(name);
		
		// Verify
		verify(mockBeanMap).get(name);
		assertEquals("getBeanDefinition result should equal the expected value", mockBeanDefinition, actual);
	}
	
	@Test
	public void testGetBeanType_doesNotExist() {
		// Setup
		String name = "bean";
		when(mockBeanMap.get(name)).thenReturn(null);
		
		// Run
		Class<?> actual = beanFactory.getBeanType(name);
		
		// Verify
		verify(mockBeanMap).get(name);
		assertNull("getBeanType result should be null", actual);
	}
	
	@Test
	public void testGetBeanType() {
		// Setup
		String name = "bean";
		when(mockBeanMap.get(name)).thenReturn(mockBeanDefinition);
		Class<?> expected = Integer.class;
		doReturn(expected).when(mockBeanDefinition).getType();
		
		// Run
		Class<?> actual = beanFactory.getBeanType(name);
		
		// Verify
		verify(mockBeanMap).get(name);
		assertEquals("getBeanType result should equal the expected value", expected, actual);
	}
	
	@Test
	public void findCandidateBeanName() {
		// Setup
		Map<String, AbstractBeanDefinition> map = new HashMap<String, AbstractBeanDefinition>();
		String expected = "bean";
		map.put(expected, mockBeanDefinition);
		when(mockBeanMap.entrySet()).thenReturn(map.entrySet());
		Class<?> clazz = Integer.class;
		doReturn(clazz).when(mockBeanDefinition).getType();
		when(mockBeanDefinition.getName()).thenReturn(expected);
		
		// Run
		String actual = beanFactory.findCandidateBeanName(clazz);
		
		// Verify
		verify(mockBeanMap).entrySet();
		verify(mockBeanDefinition).getType();
		verify(mockBeanDefinition).getName();
		assertEquals("findCandidateBeanName result should equal the expected value", expected, actual);
	}
	
	@Test(expected = InfinitumConfigurationException.class)
	public void findCandidateBeanName_multipleCandidates() {
		// Setup
		Map<String, AbstractBeanDefinition> map = new HashMap<String, AbstractBeanDefinition>();
		String expected = "bean";
		map.put(expected, mockBeanDefinition);
		map.put("anotherBean", mockBeanDefinition);
		when(mockBeanMap.entrySet()).thenReturn(map.entrySet());
		Class<?> clazz = Integer.class;
		doReturn(clazz).when(mockBeanDefinition).getType();
		when(mockBeanDefinition.getName()).thenReturn(expected);
		
		// Run
		beanFactory.findCandidateBeanName(clazz);
		
		// Verify
		assertTrue("findCandidateBeanName should throw an InfinitumConfigurationException", false);
	}
	
	@Test
	public void findCandidateBeanName_noCandidate() {
		// Setup
		Map<String, AbstractBeanDefinition> map = new HashMap<String, AbstractBeanDefinition>();
		when(mockBeanMap.entrySet()).thenReturn(map.entrySet());
		Class<?> clazz = Integer.class;
		
		// Run
		String actual = beanFactory.findCandidateBeanName(clazz);
		
		// Verify
		verify(mockBeanMap).entrySet();
		assertNull("findCandidateBeanName result should be null", actual);
	}
	
	@Test
	public void findCandidateBean() {
		// Setup
		Map<String, AbstractBeanDefinition> map = new HashMap<String, AbstractBeanDefinition>();
		String name = "bean";
		map.put(name, mockBeanDefinition);
		when(mockBeanMap.entrySet()).thenReturn(map.entrySet());
		Class<?> clazz = Integer.class;
		doReturn(clazz).when(mockBeanDefinition).getType();
		when(mockBeanDefinition.getName()).thenReturn(name);
		when(mockBeanMap.containsKey(name)).thenReturn(true);
		Integer expected = 42;
		when(mockBeanMap.get(name)).thenReturn(mockBeanDefinition);
		when(mockBeanDefinition.getBeanInstance()).thenReturn(expected);
		
		// Run
		Object actual = beanFactory.findCandidateBean(clazz);
		
		// Verify
		verify(mockBeanMap).entrySet();
		verify(mockBeanDefinition).getType();
		verify(mockBeanDefinition).getName();
		assertEquals("findCandidateBean result should equal the expected value", expected, actual);
	}
	
	@Test(expected = InfinitumConfigurationException.class)
	public void findCandidateBean_multipleCandidates() {
		// Setup
		Map<String, AbstractBeanDefinition> map = new HashMap<String, AbstractBeanDefinition>();
		String name = "bean";
		map.put(name, mockBeanDefinition);
		map.put("anotherBean", mockBeanDefinition);
		when(mockBeanMap.entrySet()).thenReturn(map.entrySet());
		Class<?> clazz = Integer.class;
		doReturn(clazz).when(mockBeanDefinition).getType();
		when(mockBeanDefinition.getName()).thenReturn(name);
		
		// Run
		beanFactory.findCandidateBean(clazz);
		
		// Verify
		assertTrue("findCandidateBean should throw an InfinitumConfigurationException", false);
	}
	
	@Test
	public void findCandidateBean_noCandidate() {
		// Setup
		Map<String, AbstractBeanDefinition> map = new HashMap<String, AbstractBeanDefinition>();
		when(mockBeanMap.entrySet()).thenReturn(map.entrySet());
		Class<?> clazz = Integer.class;
		
		// Run
		Object actual = beanFactory.findCandidateBean(clazz);
		
		// Verify
		verify(mockBeanMap).entrySet();
		assertNull("findCandidateBeanName result should be null", actual);
	}

}
