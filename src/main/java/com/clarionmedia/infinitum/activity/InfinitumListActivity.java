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

package com.clarionmedia.infinitum.activity;

import android.app.ListActivity;
import android.os.Bundle;

import com.clarionmedia.infinitum.context.ContextFactory;
import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.context.exception.InfinitumConfigurationException;
import com.clarionmedia.infinitum.di.ActivityInjector;
import com.clarionmedia.infinitum.di.impl.ObjectInjector;
import com.clarionmedia.infinitum.event.AbstractEvent;
import com.clarionmedia.infinitum.event.EventPublisher;
import com.clarionmedia.infinitum.event.EventSubscriber;
import com.clarionmedia.infinitum.event.impl.LifecycleEvent;
import com.clarionmedia.infinitum.event.impl.LifecycleEvent.LifecycleHook;
import com.clarionmedia.infinitum.reflection.impl.JavaClassReflector;

/**
 * <p> This {@link ListActivity} extension takes care of framework initialization, provides support for resource
 * injection and event binding, and exposes an {@link InfinitumContext}. </p>
 *
 * @author Tyler Treat
 * @version 1.0.4 03/14/13
 * @see InfinitumActivity
 * @see InfinitumFragmentActivity
 * @since 1.0
 */
public class InfinitumListActivity extends ListActivity implements EventPublisher, EventSubscriber {

    private InfinitumContext mInfinitumContext;
    private int mInfinitumConfigId;
    private ContextFactory mContextFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContextFactory = ContextFactory.getInstance();
        mInfinitumContext = mInfinitumConfigId == 0 ? mContextFactory.configure(this) : mContextFactory.configure
                (this, mInfinitumConfigId);
        final ActivityInjector injector = new ObjectInjector(mInfinitumContext, new JavaClassReflector(), this);
        injector.inject();
        mInfinitumContext.subscribeForEvents(this);
        mInfinitumContext.publishEvent(new LifecycleEvent(this, LifecycleHook.ON_CREATE));
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        mInfinitumContext.publishEvent(new LifecycleEvent(this, LifecycleHook.ON_START));
        super.onStart();
    }

    @Override
    protected void onRestart() {
        mInfinitumContext.publishEvent(new LifecycleEvent(this, LifecycleHook.ON_RESTART));
        super.onRestart();
    }

    @Override
    protected void onResume() {
        mInfinitumContext.publishEvent(new LifecycleEvent(this, LifecycleHook.ON_RESUME));
        super.onResume();
    }

    @Override
    protected void onPause() {
        mInfinitumContext.publishEvent(new LifecycleEvent(this, LifecycleHook.ON_PAUSE));
        super.onPause();
    }

    @Override
    protected void onStop() {
        mInfinitumContext.publishEvent(new LifecycleEvent(this, LifecycleHook.ON_STOP));
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mInfinitumContext.publishEvent(new LifecycleEvent(this, LifecycleHook.ON_DESTROY));
        super.onDestroy();
    }

    /**
     * Returns the {@link InfinitumContext} for the {@code InfinitumActivity}.
     *
     * @return {@code InfinitumContext}
     */
    protected InfinitumContext getInfinitumContext() {
        return mInfinitumContext;
    }

    /**
     * Returns the {@link InfinitumContext} of the specified type if available. This is a convenience method which is
     * equivalent to {@code getInfinitumContext().getChildContext(contextType)}.
     *
     * @param contextType the {@code InfinitumContext} type to retrieve
     * @return {@code InfinitumContext}
     * @throws InfinitumConfigurationException
     *          if the desired type is not available
     */
    protected <T extends InfinitumContext> T getInfinitumContext(Class<T> contextType) {
        return mInfinitumContext.getChildContext(contextType);
    }

    /**
     * Sets the resource ID of the Infinitum XML config to use.
     *
     * @param configId Infinitum config ID
     */
    protected void setInfinitumConfigId(int configId) {
        mInfinitumConfigId = configId;
    }

    @Override
    public void onEventPublished(AbstractEvent event) {

    }

}
