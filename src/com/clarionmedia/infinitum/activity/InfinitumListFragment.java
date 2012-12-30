/*
 * Copyright (c) 2012 Tyler Treat
 * 
 * This file is part of Infinitum Framework.
 *
 * Infinitum Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Infinitum Framework is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Infinitum Framework.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.clarionmedia.infinitum.activity;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

import com.clarionmedia.infinitum.context.ContextFactory;
import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.di.ActivityInjector;
import com.clarionmedia.infinitum.di.impl.ObjectInjector;

/**
 * <p>
 * This {@link ListFragment} extension takes care of framework initialization,
 * provides support for resource injection and event binding, and exposes an
 * {@link InfinitumContext}.
 * </p>
 *
 * @author Tyler Treat
 * @version 1.0 12/18/12
 * @see InfinitumFragment
 * @since 1.0
 */
public class InfinitumListFragment extends ListFragment {

    private InfinitumContext mInfinitumContext;
    private int mInfinitumConfigId;
    private ContextFactory mContextFactory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContextFactory = ContextFactory.newInstance();
        mInfinitumContext = mInfinitumConfigId == 0 ?
                mContextFactory.configure(getActivity()) :
                mContextFactory.configure(getActivity(), mInfinitumConfigId);
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
     * @param configId Infinitum config ID
     */
    protected void setInfinitumConfigId(int configId) {
        mInfinitumConfigId = configId;
    }

}
