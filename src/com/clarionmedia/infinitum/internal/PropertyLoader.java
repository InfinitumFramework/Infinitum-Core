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

package com.clarionmedia.infinitum.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.clarionmedia.infinitum.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;

/**
 * <p>
 * Contains methods for retrieving framework properties.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 05/22/12
 */
public class PropertyLoader {

	private Context mContext;

	/**
	 * Constructs a new {@code PropertyLoader}
	 * 
	 * @param context
	 *            the application {@link Context}
	 */
	public PropertyLoader(Context context) {
		mContext = context;
	}

	/**
	 * Retrieves the error message with the given name from the errors
	 * properties file.
	 * 
	 * @param error
	 *            the name of the error
	 * @return error message or {@code null} if no such error exists
	 */
	public String getErrorMessage(String error) {
		return getProperty(R.raw.errors, error);
	}

	/**
	 * Retrieves the context value with the given name from the context
	 * properties file
	 * 
	 * @param name
	 *            the name of the value
	 * @return value
	 */
	public String getContextValue(String name) {
		return getProperty(R.raw.context, name);
	}

	/**
	 * Retrieves the persistence value with the given name from the persistence
	 * properties file
	 * 
	 * @param name
	 *            the name of the value
	 * @return value
	 */
	public String getPersistenceValue(String name) {
		return getProperty(R.raw.persistence, name);
	}

	private String getProperty(int propsId, String name) {
		try {
			Resources resources = mContext.getResources();
			InputStream rawResource = resources.openRawResource(propsId);
			Properties properties = new Properties();
			properties.load(rawResource);
			return properties.getProperty(name);
		} catch (NotFoundException e) {
			// TODO
			return null;
		} catch (IOException e) {
			// TODO
			return null;
		}
	}

}
