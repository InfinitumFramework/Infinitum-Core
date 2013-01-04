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

import java.lang.reflect.Constructor;
import java.util.List;

import android.util.Log;

import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.reflection.ClassReflector;
import com.clarionmedia.infinitum.reflection.impl.DefaultClassReflector;

/**
 * <p>
 * Static utility methods for dealing with Infinitum Framework modules.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 12/23/12
 * @since 1.0
 */
public class ModuleUtils {

	private static final String ORM_MARKER_CLASS = "com.clarionmedia.infinitum.orm.context.InfinitumOrmContext";
	private static final String ORM_CONTEXT_CLASS = "com.clarionmedia.infinitum.orm.context.impl.XmlInfinitumOrmContext";
	private static final String AOP_MARKER_CLASS = "com.clarionmedia.infinitum.aop.context.InfinitumAopContext";
	private static final String AOP_CONTEXT_CLASS = "com.clarionmedia.infinitum.aop.context.impl.XmlInfinitumAopContext";
	private static final String WEB_MARKER_CLASS = "com.clarionmedia.infinitum.web.context.InfinitumWebContext";
	private static final String WEB_CONTEXT_CLASS = "com.clarionmedia.infinitum.web.context.impl.XmlInfinitumWebContext";

	/**
	 * Module enum containing information for the various framework modules.
	 */
	public enum Module {
		ORM(ORM_MARKER_CLASS, ORM_CONTEXT_CLASS), AOP(AOP_MARKER_CLASS, AOP_CONTEXT_CLASS), WEB(WEB_MARKER_CLASS, WEB_CONTEXT_CLASS);

		private String mMarker;
		private String mContext;
		private ClassReflector mReflector;

		Module(String marker, String context) {
			mMarker = marker;
			mContext = context;
			mReflector = new DefaultClassReflector();
		}

		/**
		 * Returns the marker class name for this {@code Module}. The marker
		 * class is used to determine if the {@code Module} is on the classpath.
		 * 
		 * @return marker class name
		 */
		public String getMarkerClass() {
			return mMarker;
		}

		/**
		 * Returns the context class name for this {@code Module}.
		 * 
		 * @return context class name
		 */
		public String getContextClass() {
			return mContext;
		}

		/**
		 * Initializes a new instance of the {@link InfinitumContext} for this
		 * {@code Module}.
		 * 
		 * @param parent
		 *            the parent context
		 * @return module context
		 */
		public InfinitumContext initialize(InfinitumContext parent) {
			try {
				Class<?> contextClass = Thread.currentThread().getContextClassLoader().loadClass(mContext);
				List<Constructor<?>> ctors = mReflector.getAllConstructors(contextClass);
				if (ctors.size() == 0)
					Log.e(getClass().getSimpleName(), "Unable to load Infinitum context for module " + name() + ".");
				return (InfinitumContext) mReflector.getClassInstance(ctors.get(0), parent);
			} catch (ClassNotFoundException e) {
				Log.e(getClass().getSimpleName(), "Unable to load Infinitum context for module " + name() + ",", e);
				return null;
			}
		}
	};

	/**
	 * Indicates if the given module is available.
	 * 
	 * @return {@code true} if the module is available, {@code false} if not
	 */
	public static boolean hasModule(Module module) {
		return hasClass(module.getMarkerClass());
	}

	private static boolean hasClass(String name) {
		try {
			Thread.currentThread().getContextClassLoader().loadClass(name);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

}
