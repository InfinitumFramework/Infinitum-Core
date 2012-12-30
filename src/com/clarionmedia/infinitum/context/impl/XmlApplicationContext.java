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

import static java.lang.Boolean.parseBoolean;
import static java.util.Arrays.asList;

import java.util.ArrayList;
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
import com.clarionmedia.infinitum.di.XmlBean;
import com.clarionmedia.infinitum.di.impl.ConfigurableBeanFactory;

/**
 * <p>
 * Implementation of {@link InfinitumContext}. This should not be instantiated
 * directly but rather obtained through the {@link XmlContextFactory}, which
 * creates an instance of this from {@code infinitum.cfg.xml}.
 * </p>
 *
 * @author Tyler Treat
 * @version 1.0 06/26/12
 * @since 06/26/12
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
        mBeanFactory = new ConfigurableBeanFactory(this);
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
                packages.add("com.clarionmedia.infinitum.internal");
                return packages;
            }

        }

    }

}
