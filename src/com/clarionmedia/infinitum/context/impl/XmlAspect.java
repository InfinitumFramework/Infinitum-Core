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

package com.clarionmedia.infinitum.context.impl;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import com.clarionmedia.infinitum.di.XmlBean;

/**
 * <p>
 * Encapsulates the notion of an "aspect", which consists of a name or ID, a
 * class, and optional properties, as defined in XML.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/18/12
 * @since 1.0
 */
@Root
public class XmlAspect extends XmlBean {

	@ElementList(required = false, entry = "advice", inline = true)
	private List<Advice> mAdvice;

	/**
	 * Returns the {@link List} of {@link Advice} for this
	 * {@code AspectComponent}.
	 * 
	 * @return {@code Advice}
	 */
	public List<Advice> getAdvice() {
		return mAdvice;
	}

	/**
	 * Sets the {@link List} of {@link Advice} for this {@code AspectComponent}.
	 * 
	 * @param advice
	 *            {@code Advice} to set
	 */
	public void setAdvice(List<Advice> advice) {
		mAdvice = advice;
	}

	/**
	 * <p>
	 * Encapsulates an "advice" contained within an aspect.
	 * </p>
	 * 
	 * @author Tyler Treat
	 * @version 1.0 07/18/12
	 * @since 1.0
	 */
	@Root
	public static class Advice {

		@Attribute(name = "id")
		private String mId;

		@Attribute(name = "pointcut")
		private String mPointcut;

		@Attribute(name = "value")
		private String mValue;

		@Attribute(name = "type")
		private String mType;

		@Attribute(name = "order", required = false)
		private int mOrder = Integer.MAX_VALUE;

		/**
		 * Returns the {@code Advice} ID. This is the name of the advice method
		 * in the aspect class.
		 * 
		 * @return ID
		 */
		public String getId() {
			return mId;
		}

		/**
		 * Sets the {@code AspectComponent} ID. This is the name of the advice
		 * method in the aspect class.
		 * 
		 * @return ID to set
		 */
		public void setId(String id) {
			mId = id;
		}

		/**
		 * Gets the pointcut type, such as {@code beans} or {@code within}.
		 * 
		 * @return pointcut type
		 */
		public String getPointcut() {
			return mPointcut;
		}

		/**
		 * Sets the pointcut type, such as {@code beans} or {@code within}.
		 * 
		 * @param pointcut
		 *            pointcut type to set
		 */
		public void setPointcut(String pointcut) {
			mPointcut = pointcut;
		}

		/**
		 * Returns the {@code Advice} pointcut value, which is the value
		 * corresponding to the pointcut type.
		 * 
		 * @return pointcut value
		 */
		public String getValue() {
			return mValue;
		}

		/**
		 * Sets the {@code Advice} pointcut value, which is the value
		 * corresponding to the pointcut type.
		 * 
		 * @param value
		 *            pointcut value to set
		 */
		public void setValue(String value) {
			mValue = value;
		}

		/**
		 * Returns the {@code Advice} type, such as {@code before},
		 * {@code after}, or {@code around}.
		 * 
		 * @return {@code Advice} type
		 */
		public String getType() {
			return mType;
		}

		/**
		 * Sets the {@code Advice} type, such as {@code before}, {@code after},
		 * or {@code around}.
		 * 
		 * @param type
		 *            {@code Advice} type to set
		 */
		public void setType(String type) {
			mType = type;
		}

		/**
		 * Returns the {@code Advice} precedence. A smaller number indicates a
		 * higher precedence, while a larger number indicates a lower
		 * precedence. The default value is {@link Integer#MAX_VALUE}. The
		 * precedence determines the order in which advice is executed.
		 * 
		 * @return {@code Advice} precedence
		 */
		public int getOrder() {
			return mOrder;
		}

		/**
		 * Sets the {@code Advice} precedence. A smaller number indicates a
		 * higher precedence, while a larger number indicates a lower
		 * precedence. The default value is {@link Integer#MAX_VALUE}. The
		 * precedence determines the order in which advice is executed.
		 * 
		 * @param order
		 *            {@code Advice} precedence to set
		 */
		public void setOrder(int order) {
			mOrder = order;
		}

		/**
		 * Returns the separated pointcut values, which are delimited by ';'.
		 * 
		 * @return pointcut values
		 */
		public String[] getSeparatedValues() {
			return mValue.split(";");
		}

	}

}