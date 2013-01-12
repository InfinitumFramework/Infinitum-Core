package com.clarionmedia.infinitum.di.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.clarionmedia.infinitum.di.AbstractBeanDefinition;
import com.clarionmedia.infinitum.di.BeanFactory;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class GenericBeanDefinitionBuilderTest {

	@Mock
	private BeanFactory mockBeanFactory;

	private GenericBeanDefinitionBuilder beanDefinitionBuilder;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		beanDefinitionBuilder = new GenericBeanDefinitionBuilder(mockBeanFactory);
	}

	@Test(expected = IllegalStateException.class)
	public void testBuild_noName() {
		// Setup
		beanDefinitionBuilder.setType(Integer.class).setScope("singleton");

		// Run
		beanDefinitionBuilder.build();

		// Verify
		assertTrue("build should throw an IllegalStateException", false);
	}

	@Test(expected = IllegalStateException.class)
	public void testBuild_noType() {
		// Setup
		beanDefinitionBuilder.setName("bean").setScope("singleton");

		// Run
		beanDefinitionBuilder.build();

		// Verify
		assertTrue("build should throw an IllegalStateException", false);
	}
	
	@Test
	public void testBuild_defaultScope() {
		// Setup
		String name = "bean";
		Class<?> type = Integer.class;
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("prop", 42);
		beanDefinitionBuilder.setName(name).setType(type).setProperties(properties);

		// Run
		AbstractBeanDefinition actual = beanDefinitionBuilder.build();

		// Verify
		assertEquals("build result should have the expected name", name, actual.getName());
		assertEquals("build result should have the expected type", type, actual.getType());
		assertEquals("build result should have the expected properties", properties, actual.getProperties());
		assertEquals("build result should be a SingletonBeanDefinition", SingletonBeanDefinition.class, actual.getClass());
	}

	@Test
	public void testBuild_singleton() {
		// Setup
		String name = "bean";
		Class<?> type = Integer.class;
		String scope = "singleton";
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("prop", 42);
		beanDefinitionBuilder.setName(name).setType(type).setScope(scope).setProperties(properties);

		// Run
		AbstractBeanDefinition actual = beanDefinitionBuilder.build();

		// Verify
		assertEquals("build result should have the expected name", name, actual.getName());
		assertEquals("build result should have the expected type", type, actual.getType());
		assertEquals("build result should have the expected properties", properties, actual.getProperties());
		assertEquals("build result should be a SingletonBeanDefinition", SingletonBeanDefinition.class, actual.getClass());
	}
	
	@Test
	public void testBuild_prototype() {
		// Setup
		String name = "bean";
		Class<?> type = Integer.class;
		String scope = "prototype";
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("prop", 42);
		beanDefinitionBuilder.setName(name).setType(type).setScope(scope).setProperties(properties);

		// Run
		AbstractBeanDefinition actual = beanDefinitionBuilder.build();

		// Verify
		assertEquals("build result should have the expected name", name, actual.getName());
		assertEquals("build result should have the expected type", type, actual.getType());
		assertEquals("build result should have the expected properties", properties, actual.getProperties());
		assertEquals("build result should be a PrototypeBeanDefinition", PrototypeBeanDefinition.class, actual.getClass());
	}

}
