/*
 * Copyright (c) 2012 Tyler Treat
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

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.clarionmedia.infinitum.context.ContextFactory;
import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.di.ActivityInjector;
import com.clarionmedia.infinitum.di.impl.ObjectInjector;

/**
 * <p>
 * This {@link FragmentActivity} extension takes care of framework initialization,
 * provides support for resource injection and event binding, and exposes an
 * {@link InfinitumContext}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 12/18/12
 * @since 1.0
 * @see InfinitumActivity
 * @see InfinitumListActivity
 */
public class InfinitumFragmentActivity extends FragmentActivity {

	private InfinitumContext mInfinitumContext;
	private int mInfinitumConfigId;
	private ContextFactory mContextFactory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mContextFactory = ContextFactory.newInstance();
		mInfinitumContext = mInfinitumConfigId == 0 ?
				mContextFactory.configure(this) :
				mContextFactory.configure(this, mInfinitumConfigId);
		final ActivityInjector injector = new ObjectInjector(mInfinitumContext, this);
		injector.inject();
		super.onCreate(savedInstanceState);
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
	 * Sets the resource ID of the Infinitum XML config to use.
	 * 
	 * @param configId
	 *            Infinitum config ID
	 */
	protected void setInfinitumConfigId(int configId) {
		mInfinitumConfigId = configId;
	}

}
