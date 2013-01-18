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

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clarionmedia.infinitum.context.ContextFactory;
import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.context.exception.InfinitumConfigurationException;
import com.clarionmedia.infinitum.di.ActivityInjector;
import com.clarionmedia.infinitum.di.impl.ObjectInjector;
import com.clarionmedia.infinitum.event.EventPublisher;
import com.clarionmedia.infinitum.event.impl.LifecycleEvent;
import com.clarionmedia.infinitum.event.impl.LifecycleEvent.LifecycleHook;
import com.clarionmedia.infinitum.reflection.impl.JavaClassReflector;

/**
 * <p>
 * This {@link ListFragment} extension takes care of framework initialization,
 * provides support for resource injection and event binding, and exposes an
 * {@link InfinitumContext}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 12/18/12
 * @since 1.0
 * @see InfinitumFragment
 */
public class InfinitumListFragment extends ListFragment implements EventPublisher {

	private InfinitumContext mInfinitumContext;
	private int mInfinitumConfigId;
	private ContextFactory mContextFactory;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mContextFactory = ContextFactory.newInstance();
		mInfinitumContext = mInfinitumConfigId == 0 ?
				mContextFactory.configure(getActivity()) :
				mContextFactory.configure(getActivity(), mInfinitumConfigId);
		final ActivityInjector injector = new ObjectInjector(mInfinitumContext,  new JavaClassReflector(),this);
		injector.inject();
		mInfinitumContext.publishEvent(new LifecycleEvent(this, LifecycleHook.ON_CREATE));
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mInfinitumContext.publishEvent(new LifecycleEvent(this, LifecycleHook.ON_ACTIVITY_CREATED));
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		mInfinitumContext.publishEvent(new LifecycleEvent(this, LifecycleHook.ON_ATTACH));
		super.onAttach(activity);
	}
	
	@Override
	public void onDetach() {
		mInfinitumContext.publishEvent(new LifecycleEvent(this, LifecycleHook.ON_DETACH));
		super.onDetach();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mInfinitumContext.publishEvent(new LifecycleEvent(this, LifecycleHook.ON_CREATE_VIEW));
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onResume() {
		mInfinitumContext.publishEvent(new LifecycleEvent(this, LifecycleHook.ON_RESUME));
		super.onResume();
	}

	@Override
	public void onPause() {
		mInfinitumContext.publishEvent(new LifecycleEvent(this, LifecycleHook.ON_PAUSE));
		super.onPause();
	}
	
	@Override
	public void onStart() {
		mInfinitumContext.publishEvent(new LifecycleEvent(this, LifecycleHook.ON_START));
		super.onStart();
	}

	@Override
	public void onStop() {
		mInfinitumContext.publishEvent(new LifecycleEvent(this, LifecycleHook.ON_STOP));
		super.onStop();
	}

	@Override
	public void onDestroy() {
		mInfinitumContext.publishEvent(new LifecycleEvent(this, LifecycleHook.ON_DESTROY));
		super.onDestroy();
	}
	
	@Override
	public void onDestroyView() {
		mInfinitumContext.publishEvent(new LifecycleEvent(this, LifecycleHook.ON_DESTROY_VIEW));
		super.onDestroyView();
	}
	
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		mInfinitumContext.publishEvent(new LifecycleEvent(this, LifecycleHook.ON_VIEW_STATE_RESTORED));
		super.onViewStateRestored(savedInstanceState);
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
	 * Returns the {@link InfinitumContext} of the specified type if available.
	 * This is a convenience method which is equivalent to
	 * {@code getInfinitumContext().getChildContext(contextType)}.
	 * 
	 * @param contextType
	 *            the {@code InfinitumContext} type to retrieve
	 * @return {@code InfinitumContext}
	 * @throws InfinitumConfigurationException
	 *             if the desired type is not available
	 */
	protected <T extends InfinitumContext> T getInfinitumContext(Class<T> contextType) {
		return mInfinitumContext.getChildContext(contextType);
	}

	/**
	 * Sets the resource ID of the Infinitum XML config to use.
	 * 
	 * @param configId
	 *            Infinitum config ID
	 */
	protected void setInfinitumConfigId(int configId) {
		mInfinitumConfigId = configId;
	}

}
