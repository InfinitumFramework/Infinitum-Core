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

package com.clarionmedia.infinitum.context.impl;

import android.content.Context;
import com.clarionmedia.infinitum.context.ContextFactory;
import com.clarionmedia.infinitum.context.InfinitumContext;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class InfinitumContextProxyTest {

    @Mock
    private ContextFactory mockContextFactory;

    @Mock
    private InfinitumContext mockInfinitumContext;

    @Mock
    private Context mockContext;

    private InfinitumContextProxy contextProxy;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        contextProxy = new InfinitumContextProxy(InfinitumContext.class, mockContextFactory);
        when(mockContextFactory.getContext(InfinitumContext.class)).thenReturn(mockInfinitumContext);
    }

    @Test
    public void testInvoke_lazyInitialize() throws Throwable {
        // Setup
        Method method = mockInfinitumContext.getClass().getMethod("getAndroidContext");
        when(mockInfinitumContext.getAndroidContext()).thenReturn(mockContext);

        // Run
        Object actual = contextProxy.invoke(contextProxy, method, null);

        // Verify
        assertNotNull("invoke result should not be null", actual);
        assertEquals("invoke result should equal the expected value", mockContext, actual);
        verify(mockContextFactory).getContext(InfinitumContext.class);
        verify(mockInfinitumContext).getAndroidContext();
    }

    @Test
    public void testInvoke_initializedOnce() throws Throwable {
        // Setup
        Method method = mockInfinitumContext.getClass().getMethod("getAndroidContext");
        when(mockInfinitumContext.getAndroidContext()).thenReturn(mockContext);

        // Run
        contextProxy.invoke(contextProxy, method, null);
        Object actual = contextProxy.invoke(contextProxy, method, null);

        assertNotNull("invoke result should not be null", actual);
        assertEquals("invoke result should equal the expected value", mockContext, actual);
        verify(mockContextFactory).getContext(InfinitumContext.class);
        verify(mockInfinitumContext, times(2)).getAndroidContext();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClone() {
        // Run
        contextProxy.clone();

        // Verify
        assertTrue("clone should have thrown an UnsupportedOperationException", false);
    }

}
