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

	/**
	 * Module enum containing information for the various framework modules.
	 */
	public enum Module {
		ORM(ORM_MARKER_CLASS, ORM_CONTEXT_CLASS), AOP(AOP_MARKER_CLASS, AOP_CONTEXT_CLASS);

		private String mMarker;
		private String mContext;

		Module(String marker, String context) {
			mMarker = marker;
			mContext = context;
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
