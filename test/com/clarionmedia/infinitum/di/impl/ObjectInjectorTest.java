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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.view.View;
import android.widget.LinearLayout;

import com.clarionmedia.infinitum.activity.annotation.Bind;
import com.clarionmedia.infinitum.activity.annotation.InjectLayout;
import com.clarionmedia.infinitum.activity.annotation.InjectResource;
import com.clarionmedia.infinitum.activity.annotation.InjectView;
import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.context.exception.InfinitumConfigurationException;
import com.clarionmedia.infinitum.di.BeanFactory;
import com.clarionmedia.infinitum.di.annotation.Autowired;
import com.clarionmedia.infinitum.reflection.ClassReflector;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ObjectInjectorTest {
	
	private static final int STRING_RESOURCE_ID = 42;
	private static final String STRING_RESOURCE = "foo";
	private static final int VIEW_ID = 99;
	private static final LinearLayout LAYOUT_VIEW = mock(LinearLayout.class);
	private static final int LAYOUT_ID = 10;
	
	@Mock
	private InfinitumContext mockContext;
	
	@Mock
	private ClassReflector mockClassReflector;
	
	@Mock
	private BeanFactory mockBeanFactory;
	
	@Mock
	private Resources mockResources;
	
	private FooBean object;
	private ObjectInjector objectInjector;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		object = new FooBean();
		objectInjector = new ObjectInjector(mockContext, mockClassReflector, object);
		when(mockContext.getBeanFactory()).thenReturn(mockBeanFactory);
		when(mockResources.getResourceTypeName(STRING_RESOURCE_ID)).thenReturn("string");
		when(mockResources.getString(STRING_RESOURCE_ID)).thenReturn(STRING_RESOURCE);
	}
	
	@Test(expected = InfinitumConfigurationException.class)
	public void testInject_nonActivity_noCandidate() throws SecurityException, NoSuchFieldException {
		// Setup
		List<Field> fields = new ArrayList<Field>();
		Field field = FooBean.class.getDeclaredField("mBar");
		fields.add(field);
		when(mockClassReflector.getAllFields(FooBean.class)).thenReturn(fields);
		when(mockBeanFactory.findCandidateBean(BarBean.class)).thenReturn(null);
		
		// Run
		objectInjector.inject();
		
		// Verify
		assertTrue("inject should have thrown an InfinitumConfigurationException", false);
	}
	
	@Test
	public void testInject_nonActivity() throws SecurityException, NoSuchFieldException {
		// Set
		List<Field> fields = new ArrayList<Field>();
		Field field = FooBean.class.getDeclaredField("mBar");
		fields.add(field);
		when(mockClassReflector.getAllFields(FooBean.class)).thenReturn(fields);
		BarBean bean = new BarBean();
		when(mockBeanFactory.findCandidateBean(BarBean.class)).thenReturn(bean);
		
		// Run
		objectInjector.inject();
		
		// Verify
		verify(mockClassReflector).getAllFields(FooBean.class);
		verify(mockContext).getBeanFactory();
		verify(mockBeanFactory).findCandidateBean(BarBean.class);
		verify(mockClassReflector).setFieldValue(object, field, bean);
	}
	
	@Test
	public void testInject_activity() throws SecurityException, NoSuchFieldException {
		// Set
		FooActivity mockActivity = mock(FooActivity.class);
		when(mockActivity.getResources()).thenReturn(mockResources);
		when(mockActivity.findViewById(VIEW_ID)).thenReturn(LAYOUT_VIEW);
		objectInjector = new ObjectInjector(mockContext, mockClassReflector, mockActivity);
		List<Field> fields = new ArrayList<Field>();
		Field beanField = FooActivity.class.getDeclaredField("mBar");
		Field resField = FooActivity.class.getDeclaredField("mResource");
		Field viewField = FooActivity.class.getDeclaredField("mView");
		fields.add(beanField);
		fields.add(resField);
		fields.add(viewField);
		when(mockClassReflector.getAllFields(any(Class.class))).thenReturn(fields);
		BarBean bean = new BarBean();
		when(mockBeanFactory.findCandidateBean(BarBean.class)).thenReturn(bean);
		when(mockClassReflector.getFieldValue(mockActivity, viewField)).thenReturn(LAYOUT_VIEW);
		
		// Run
		objectInjector.inject();
		
		// Verify
		verify(mockClassReflector).getAllFields(any(Class.class));
		verify(mockContext).getBeanFactory();
		verify(mockBeanFactory).findCandidateBean(BarBean.class);
		verify(mockClassReflector).setFieldValue(mockActivity, beanField, bean);
		verify(mockClassReflector).setFieldValue(mockActivity, resField, STRING_RESOURCE);
		verify(mockClassReflector).setFieldValue(mockActivity, viewField, LAYOUT_VIEW);
		verify(LAYOUT_VIEW).setOnClickListener(any(View.OnClickListener.class));
	}
	
	private class FooBean {
		
		@Autowired
		private BarBean mBar;
		
	}
	
	private class BarBean {
		
	}
	
	@InjectLayout(LAYOUT_ID)
	private class FooActivity extends Activity {
		
		@Autowired
		private BarBean mBar;
				
		@InjectResource(STRING_RESOURCE_ID)
		private String mResource;
		
		@InjectView(VIEW_ID)
		@Bind("eventHandler")
		private LinearLayout mView;
		
		@SuppressWarnings("unused")
		public void eventHandler(View view) {
			
		}
	}

}
