/*
 * Copyright (C) 2013 Clarion Media, LLC
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

package com.clarionmedia.infinitum.context.impl;

import android.content.Context;
import android.content.res.Resources;
import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.context.RestfulContext;
import com.clarionmedia.infinitum.context.exception.InfinitumConfigurationException;
import com.clarionmedia.infinitum.di.BeanFactory;
import com.clarionmedia.infinitum.event.AbstractEvent;
import com.clarionmedia.infinitum.event.EventSubscriber;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.simpleframework.xml.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class XmlContextFactoryTest {

    private static final String PACKAGE_NAME = "com.clarionmedia.infinitum";

    @Mock
    private Serializer mockSerializer;

    @Mock
    private Context mockContext;

    @Mock
    private Context mockApplicationContext;

    @Mock
    private Resources mockResources;

    @Mock
    private XmlApplicationContext mockXmlApplicationContext;

    private XmlContextFactory contextFactory;
    private InputStream inputStream;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        contextFactory = new XmlContextFactory(mockSerializer);
        when(mockContext.getApplicationContext()).thenReturn(mockApplicationContext);
        when(mockApplicationContext.getResources()).thenReturn(mockResources);
        when(mockApplicationContext.getPackageName()).thenReturn(PACKAGE_NAME);
        inputStream = getClass().getResourceAsStream("/infinitum.cfg.xml");
    }

    @After
    public void tearDown() {
        contextFactory.clearConfiguration();
        try {
            inputStream.close();
        } catch (IOException e) {
        }
    }

    @Test(expected = InfinitumConfigurationException.class)
    public void testConfigure_implicit_notFound() {
        // Setup
        when(mockResources.getIdentifier("infinitum", "raw", PACKAGE_NAME)).thenReturn(0);

        // Run
        contextFactory.configure(mockContext);

        // Verify
        assertTrue("configure should have thrown an InfinitumConfigurationException", false);
    }

    @Test(expected = InfinitumConfigurationException.class)
    public void testConfigure_implicit_deserializationFailure() throws Exception {
        // Setup
        int id = 42;
        when(mockResources.getIdentifier("infinitum", "raw", PACKAGE_NAME)).thenReturn(id);
        when(mockResources.openRawResource(id)).thenReturn(inputStream);
        when(mockSerializer.read(any(Class.class), any(String.class))).thenReturn(null);

        // Run
        contextFactory.configure(mockContext);

        // Verify
        assertTrue("configure should have thrown an InfinitumConfigurationException", false);
    }

    @Test
    public void testConfigure_implicit() throws Exception {
        // Setup
        int id = 42;
        when(mockResources.getIdentifier("infinitum", "raw", PACKAGE_NAME)).thenReturn(id);
        when(mockResources.openRawResource(id)).thenReturn(inputStream);
        when(mockSerializer.read(any(Class.class), any(String.class))).thenReturn(mockXmlApplicationContext);

        // Run
        InfinitumContext actual = contextFactory.configure(mockContext);

        // Verify
        assertNotNull("configure result should not be null", actual);
        assertEquals("configure result should equal the expected value", mockXmlApplicationContext, actual);
        verify(mockContext).getApplicationContext();
        verify(mockApplicationContext, times(2)).getResources();
        verify(mockResources).getIdentifier("infinitum", "raw", PACKAGE_NAME);
        verify(mockResources).openRawResource(id);
        verify(mockSerializer).read(any(Class.class), any(String.class));
        verify(mockXmlApplicationContext).postProcess(mockApplicationContext);
    }

    @Test(expected = InfinitumConfigurationException.class)
    public void testConfigure_explicit_deserializationFailure() throws Exception {
        // Setup
        int id = 42;
        when(mockResources.openRawResource(id)).thenReturn(inputStream);
        when(mockSerializer.read(any(Class.class), any(String.class))).thenReturn(null);

        // Run
        contextFactory.configure(mockContext, id);

        // Verify
        assertTrue("configure should have thrown an InfinitumConfigurationException", false);
    }

    @Test
    public void testConfigure_explicit() throws Exception {
        // Setup
        int id = 42;
        when(mockResources.openRawResource(id)).thenReturn(inputStream);
        when(mockSerializer.read(any(Class.class), any(String.class))).thenReturn(mockXmlApplicationContext);

        // Run
        InfinitumContext actual = contextFactory.configure(mockContext, id);

        // Verify
        assertNotNull("configure result should not be null", actual);
        assertEquals("configure result should equal the expected value", mockXmlApplicationContext, actual);
        verify(mockContext).getApplicationContext();
        verify(mockApplicationContext).getResources();
        verify(mockResources).openRawResource(id);
        verify(mockSerializer).read(any(Class.class), any(String.class));
        verify(mockXmlApplicationContext).postProcess(mockApplicationContext);
    }

    @Test(expected = InfinitumConfigurationException.class)
    public void testGetContext_notConfigured() {
        // Run
        contextFactory.getContext();

        // Verify
        assertTrue("getContext should have thrown an InfinitumConfigurationException", false);
    }

    @Test
    public void testGetContext() throws Exception {
        // Setup
        int id = 42;
        when(mockResources.getIdentifier("infinitum", "raw", PACKAGE_NAME)).thenReturn(id);
        when(mockResources.openRawResource(id)).thenReturn(inputStream);
        when(mockSerializer.read(any(Class.class), any(String.class))).thenReturn(mockXmlApplicationContext);
        contextFactory.configure(mockContext);

        // Run
        InfinitumContext actual = contextFactory.getContext();

        // Verify
        assertNotNull("configure result should not be null", actual);
        assertEquals("configure result should equal the expected value", mockXmlApplicationContext, actual);
    }

    @Test(expected = InfinitumConfigurationException.class)
    public void testGetContext_generic_notConfigured() {
        // Run
        contextFactory.getContext(InfinitumContext.class);

        // Verify
        assertTrue("getContext should have thrown an InfinitumConfigurationException", false);
    }

    @Test(expected = InfinitumConfigurationException.class)
    public void testGetContext_generic_notFound() throws Exception {
        // Setup
        int id = 42;
        List<InfinitumContext> contexts = new ArrayList<InfinitumContext>();
        when(mockResources.getIdentifier("infinitum", "raw", PACKAGE_NAME)).thenReturn(id);
        when(mockResources.openRawResource(id)).thenReturn(inputStream);
        when(mockSerializer.read(any(Class.class), any(String.class))).thenReturn(mockXmlApplicationContext);
        when(mockXmlApplicationContext.getChildContexts()).thenReturn(contexts);
        contextFactory.configure(mockContext);

        // Run
        ChildInfinitumContext actual = contextFactory.getContext(ChildInfinitumContext.class);

        // Verify
        assertTrue("getContext should have thrown an InfinitumConfigurationException", false);
    }

    @Test
    public void testGetContext_generic() throws Exception {
        // Setup
        int id = 42;
        List<InfinitumContext> contexts = new ArrayList<InfinitumContext>();
        ChildInfinitumContext expected = new ChildInfinitumContext();
        contexts.add(expected);
        when(mockResources.getIdentifier("infinitum", "raw", PACKAGE_NAME)).thenReturn(id);
        when(mockResources.openRawResource(id)).thenReturn(inputStream);
        when(mockSerializer.read(any(Class.class), any(String.class))).thenReturn(mockXmlApplicationContext);
        when(mockXmlApplicationContext.getChildContexts()).thenReturn(contexts);
        contextFactory.configure(mockContext);

        // Run
        ChildInfinitumContext actual = contextFactory.getContext(ChildInfinitumContext.class);

        // Verify
        assertNotNull("getContext result should not be null", actual);
        assertEquals("getContext result should equal the expected value", expected, actual);
        verify(mockXmlApplicationContext).getChildContexts();
    }

    @Test
    public void testGetContext_generic_parent() throws Exception {
        // Setup
        int id = 42;
        when(mockResources.getIdentifier("infinitum", "raw", PACKAGE_NAME)).thenReturn(id);
        when(mockResources.openRawResource(id)).thenReturn(inputStream);
        when(mockSerializer.read(any(Class.class), any(String.class))).thenReturn(mockXmlApplicationContext);
        contextFactory.configure(mockContext);

        // Run
        XmlApplicationContext actual = contextFactory.getContext(XmlApplicationContext.class);

        // Verify
        assertNotNull("getContext result should not be null", actual);
        assertEquals("getContext result should equal the expected value", mockXmlApplicationContext, actual);
    }

    private class ChildInfinitumContext implements InfinitumContext {

        @Override
        public void postProcess(Context context) {

        }

        @Override
        public boolean isDebug() {
            return false;
        }

        @Override
        public Context getAndroidContext() {
            return null;
        }

        @Override
        public BeanFactory getBeanFactory() {
            return null;
        }

        @Override
        public Object getBean(String name) {
            return null;
        }

        @Override
        public <T> T getBean(String name, Class<T> clazz) {
            return null;
        }

        @Override
        public boolean isComponentScanEnabled() {
            return false;
        }

        @Override
        public List<InfinitumContext> getChildContexts() {
            return null;
        }

        @Override
        public void addChildContext(InfinitumContext context) {

        }

        @Override
        public InfinitumContext getParentContext() {
            return null;
        }

        @Override
        public <T extends InfinitumContext> T getChildContext(Class<T> contextType) {
            return null;
        }

        @Override
        public RestfulContext getRestContext() {
            return null;
        }

        @Override
        public void publishEvent(AbstractEvent event) {

        }

        @Override
        public void subscribeForEvents(EventSubscriber subscriber) {

        }
    }
}
