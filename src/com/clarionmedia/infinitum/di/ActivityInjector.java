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

package com.clarionmedia.infinitum.di;

import android.app.Activity;
import android.view.View;

/**
 * <p>
 * Responsible for injecting an {@link Activity} with Android resources and
 * framework components.
 * </p>
 *
 * @author Tyler Treat
 * @version 1.0 07/18/12
 * @since 1.0
 */
public interface ActivityInjector {

    /**
     * Defines the type of input event that occurs on a {@link View}.
     */
    public static enum Event {
        OnClick, OnLongClick, OnCreateContextMenu, OnFocusChange, OnKey, OnTouch
    }

    ;

    /**
     * Injects the appropriate resources and components into any annotated
     * fields.
     */
    void inject();

}
