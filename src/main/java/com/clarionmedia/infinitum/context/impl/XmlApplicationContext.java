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

package com.clarionmedia.infinitum.context.impl;

import static java.lang.Boolean.parseBoolean;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import com.clarionmedia.infinitum.context.AbstractContext;
import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.di.AbstractBeanDefinition;
import com.clarionmedia.infinitum.di.XmlBean;
import com.clarionmedia.infinitum.di.impl.ConfigurableBeanFactory;
import com.clarionmedia.infinitum.reflection.impl.JavaClassReflector;

/**
 * <p>
 * Implementation of {@link InfinitumContext}. This should not be instantiated
 * directly but rather obtained through the {@link XmlContextFactory}, which
 * creates an instance of this from {@code infinitum.cfg.xml}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 06/26/12
 * @since 1.0
 */
@Root(name = "infinitum-configuration")
public class XmlApplicationContext extends AbstractContext {

	@ElementMap(name = "application", entry = "property", key = "name", attribute = true, required = false)
	protected Map<String, String> mAppConfig;

	@ElementMap(name = "sqlite", entry = "property", key = "name", attribute = true, required = false)
	protected Map<String, String> mSqliteConfig;

	@ElementList(name = "domain")
	protected List<Model> mModels;

	@Element(name = "rest", required = false, type = XmlRestfulContext.class)
	protected XmlRestfulContext mRestConfig;

	@Element(name = "beans", required = false)
	protected BeanContainer mBeanContainer;

	protected XmlApplicationContext() {
		mBeanFactory = new ConfigurableBeanFactory(this, new JavaClassReflector(), new HashMap<String, AbstractBeanDefinition>());
	}

	@Override
	public boolean isDebug() {
		String debug = mAppConfig.get("debug");
		if (debug == null)
			return false;
		return parseBoolean(debug);
	}

	@Override
	protected List<XmlBean> getXmlBeans() {
		List<XmlBean> ret = new ArrayList<XmlBean>();
		if (mBeanContainer.mBeans != null)
			ret.addAll(mBeanContainer.mBeans);
		if (mBeanContainer.mAspects != null)
			ret.addAll(mBeanContainer.mAspects);
		return ret;
	}

	@Override
	public XmlRestfulContext getRestContext() {
		return mRestConfig;
	}

	public Map<String, String> getAppConfig() {
		return mAppConfig;
	}

	public Map<String, String> getSqliteConfig() {
		return mSqliteConfig;
	}

	public List<Model> getModels() {
		return mModels;
	}

	@Override
	protected List<String> getScanPackages() {
		if (mBeanContainer.mComponentScan == null)
			return new ArrayList<String>();
		return mBeanContainer.mComponentScan.getBasePackages();
	}

	@Override
	public boolean isComponentScanEnabled() {
		if (mBeanContainer.mComponentScan == null)
			return false;
		return mBeanContainer.mComponentScan.mIsEnabled;
	}

	@Root
	public static class Model {

		@Attribute(name = "resource")
		private String mResource;

		public String getResource() {
			return mResource;
		}

	}

	@Root
	private static class BeanContainer {

		@ElementList(entry = "bean", inline = true, required = false)
		private List<XmlBean> mBeans;

		@ElementList(entry = "aspect", inline = true, required = false)
		private List<XmlAspect> mAspects;

		@Element(name = "component-scan", required = false)
		private ComponentScan mComponentScan;

		@Root
		private static class ComponentScan {

			@Attribute(name = "enabled", required = false)
			private boolean mIsEnabled = true;

			@Attribute(name = "base-package", required = false)
			private String mBasePackages;

			public List<String> getBasePackages() {
				List<String> packages = new ArrayList<String>(asList(mBasePackages.split(",")));
				Iterator<String> iter = packages.iterator();
				while (iter.hasNext()) {
					String pkg = iter.next().trim();
					if (pkg.length() == 0)
						iter.remove();
				}
				return packages;
			}

		}

	}

}
