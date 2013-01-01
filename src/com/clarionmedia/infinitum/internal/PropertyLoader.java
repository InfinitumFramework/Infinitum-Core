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
