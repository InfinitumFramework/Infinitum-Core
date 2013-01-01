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

package com.clarionmedia.infinitum.di;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * <p>
 * Encapsulates the notion of a "bean", which consists of a name or ID, a class,
 * and optional properties, as defined in XML.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 06/26/12
 * @since 1.0
 */
@Root
public class XmlBean {

	@Attribute(name = "id")
	private String mId;

	@Attribute(name = "class")
	private String mClass;

	@Attribute(name = "scope", required = false)
	private String mScope;

	@ElementList(required = false, entry = "property", inline = true)
	private List<Property> mProperties;

	public XmlBean() {
		mProperties = new ArrayList<Property>();
	}

	public void setId(String id) {
		mId = id;
	}

	public String getId() {
		return mId;
	}

	public void setClassName(String className) {
		mClass = className;
	}

	public String getClassName() {
		return mClass;
	}

	public void setProperties(List<Property> properties) {
		mProperties = properties;
	}

	public List<Property> getProperties() {
		return mProperties;
	}

	public void setScope(String scope) {
		mScope = scope;
	}

	public String getScope() {
		return mScope;
	}

	/**
	 * <p>
	 * Encapsulates a bean property, which represents a {@link Field} inside a
	 * bean instance.
	 * </p>
	 * 
	 * @author Tyler Treat
	 * @version 1.0 06/26/12
	 * @since 06/26/12
	 */
	@Root
	public static class Property {

		@Attribute(name = "name")
		private String mName;

		@Text(required = false)
		private String mValue;

		@Attribute(name = "ref", required = false)
		private String mRef;

		public String getName() {
			return mName;
		}

		public void setName(String name) {
			mName = name;
		}

		public String getValue() {
			return mValue;
		}

		public void setValue(String value) {
			mValue = value;
		}

		public String getRef() {
			return mRef;
		}

		public void setRef(String ref) {
			mRef = ref;
		}

	}
}