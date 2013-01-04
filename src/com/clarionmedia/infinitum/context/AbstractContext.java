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

package com.clarionmedia.infinitum.context;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;

import com.clarionmedia.infinitum.context.exception.InfinitumConfigurationException;
import com.clarionmedia.infinitum.context.impl.XmlApplicationContext;
import com.clarionmedia.infinitum.di.AbstractBeanDefinition;
import com.clarionmedia.infinitum.di.BeanDefinitionBuilder;
import com.clarionmedia.infinitum.di.BeanFactory;
import com.clarionmedia.infinitum.di.BeanFactoryPostProcessor;
import com.clarionmedia.infinitum.di.BeanPostProcessor;
import com.clarionmedia.infinitum.di.BeanProvider;
import com.clarionmedia.infinitum.di.XmlBean;
import com.clarionmedia.infinitum.di.annotation.Bean;
import com.clarionmedia.infinitum.di.annotation.Component;
import com.clarionmedia.infinitum.di.annotation.Scope;
import com.clarionmedia.infinitum.di.impl.AutowiredBeanPostProcessor;
import com.clarionmedia.infinitum.di.impl.GenericBeanDefinitionBuilder;
import com.clarionmedia.infinitum.exception.InfinitumRuntimeException;
import com.clarionmedia.infinitum.internal.ModuleUtils;
import com.clarionmedia.infinitum.internal.ModuleUtils.Module;
import com.clarionmedia.infinitum.internal.StringUtil;
import com.clarionmedia.infinitum.reflection.PackageReflector;
import com.clarionmedia.infinitum.reflection.impl.DefaultClassReflector;
import com.clarionmedia.infinitum.reflection.impl.DefaultPackageReflector;

/**
 * <p>
 * Abstract implementation of {@link InfinitumContext} representing a root
 * "parent" context.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/11/12
 * @since 1.0
 */
public abstract class AbstractContext implements InfinitumContext, BeanProvider {

	protected BeanFactory mBeanFactory;
	protected Context mContext;
	protected List<InfinitumContext> mChildContexts;
	protected InfinitumContext mParentContext;
	protected Set<Class<?>> mScannedComponents;
	protected Set<XmlBean> mXmlComponents;

	/**
	 * Returns a {@link List} of {@link XmlBean} instances that were registered
	 * with the context through the Infinitum XML configuration.
	 * 
	 * @return {@code List} of {@code BeanComponents}
	 */
	protected abstract List<XmlBean> getXmlBeans();

	/**
	 * Returns the {@link RestfulContext} that was registered with the context
	 * through the Infinitum XML configuration.
	 * 
	 * @return {@code RestfulContext} or {@code null} if none was registered
	 */
	public abstract RestfulContext getRestContext();

	/**
	 * Returns a {@link List} of packages to scan for components.
	 * 
	 * @return {@code List} of package names
	 */
	protected abstract List<String> getScanPackages();

	/**
	 * Constructs a new {@code AbstractContext} instance.
	 */
	public AbstractContext() {
		mChildContexts = new ArrayList<InfinitumContext>();
		mScannedComponents = new HashSet<Class<?>>();
		mXmlComponents = new HashSet<XmlBean>();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void postProcess(final Context context) {
		mContext = context;
		PackageReflector reflector = new DefaultPackageReflector();
		RestfulContext restContext = getRestContext();
		if (restContext != null)
			restContext.setParentContext(this);

		// Register XML beans
		List<XmlBean> beans = getXmlBeans();
		mBeanFactory.registerBeans(beans);

		// Get XML components
		Set<Class<? extends BeanPostProcessor>> xmlBeanPostProcessors = new HashSet<Class<? extends BeanPostProcessor>>();
		xmlBeanPostProcessors.add(AutowiredBeanPostProcessor.class);
		Set<Class<BeanFactoryPostProcessor>> xmlBeanFactoryPostProcessors = new HashSet<Class<BeanFactoryPostProcessor>>();
		Set<Class<BeanProvider>> xmlBeanProviders = new HashSet<Class<BeanProvider>>();
		for (XmlBean bean : beans) {
			Class<?> clazz = reflector.getClass(bean.getClassName());
			mXmlComponents.add(bean);
			if (BeanPostProcessor.class.isAssignableFrom(clazz))
				xmlBeanPostProcessors.add((Class<BeanPostProcessor>) clazz);
			if (BeanFactoryPostProcessor.class.isAssignableFrom(clazz))
				xmlBeanFactoryPostProcessors.add((Class<BeanFactoryPostProcessor>) clazz);
			if (BeanProvider.class.isAssignableFrom(clazz))
				xmlBeanProviders.add((Class<BeanProvider>) clazz);
		}

		// Scan for annotated components
		if (isComponentScanEnabled())
			mScannedComponents.addAll(getClasspathComponents());

		// Categorize the components while filtering down the original Set
		final Set<Class<? extends BeanPostProcessor>> beanPostProcessors = getAndRemoveBeanPostProcessors(mScannedComponents);
		beanPostProcessors.addAll(xmlBeanPostProcessors);
		final Set<Class<? extends BeanFactoryPostProcessor>> beanFactoryPostProcessors = getAndRemoveBeanFactoryPostProcessors(mScannedComponents);
		beanFactoryPostProcessors.addAll(xmlBeanFactoryPostProcessors);
		final Set<Class<? extends BeanProvider>> beanProviders = getAndRemoveBeanProviders(mScannedComponents);
		beanProviders.addAll(xmlBeanProviders);

		// Register scanned bean candidates
		BeanDefinitionBuilder beanDefinitionBuilder = new GenericBeanDefinitionBuilder(mBeanFactory);
		for (Class<?> candidate : mScannedComponents) {
			if (candidate.isAnnotationPresent(Bean.class)) {
				Bean bean = candidate.getAnnotation(Bean.class);
				String beanName = bean.value().trim().equals("") ? StringUtil.toCamelCase(candidate.getSimpleName()) : bean.value().trim();
				Scope scope = candidate.getAnnotation(Scope.class);
				String scopeVal = "singleton";
				if (scope != null)
					scopeVal = scope.value();
				AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.setName(beanName).setType(candidate).setProperties(null)
						.setScope(scopeVal).build();
				mBeanFactory.registerBean(beanDefinition);
			}
		}

		// Add provider beans
		registerProviderBeans(beanProviders, beanDefinitionBuilder);

		// Execute post processors
		executeBeanPostProcessors(beanPostProcessors);
		executeBeanFactoryPostProcessors(beanFactoryPostProcessors);

		// Post process child contexts
		for (InfinitumContext childContext : getChildContexts())
			childContext.postProcess(context);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends InfinitumContext> T getChildContext(Class<T> contextType) {
		if (AbstractContext.class.isAssignableFrom(contextType))
			return (T) this;
		for (InfinitumContext context : getChildContexts()) {
			if (contextType.isAssignableFrom(context.getClass()))
				return (T) context;
		}
		throw new InfinitumConfigurationException("Configuration of type '" + contextType.getClass().getName() + "' could not be found.");
	}

	@Override
	public List<InfinitumContext> getChildContexts() {
		return mChildContexts;
	}

	@Override
	public void addChildContext(InfinitumContext context) {
		mChildContexts.add(context);
	}

	@Override
	public InfinitumContext getParentContext() {
		return mParentContext;
	}

	@Override
	public BeanFactory getBeanFactory() {
		return mBeanFactory;
	}

	@Override
	public Object getBean(String name) {
		return mBeanFactory.loadBean(name);
	}

	@Override
	public <T> T getBean(String name, Class<T> clazz) {
		return mBeanFactory.loadBean(name, clazz);
	}

	@Override
	public Context getAndroidContext() {
		return mContext;
	}

	@Override
	public List<AbstractBeanDefinition> getBeans(BeanDefinitionBuilder beanDefinitionBuilder) {
		List<AbstractBeanDefinition> beans = new ArrayList<AbstractBeanDefinition>();
		beans.add(beanDefinitionBuilder.setName("$InfinitumContext").setType(XmlApplicationContext.class).build());
		beans.add(beanDefinitionBuilder.setName("$ClassReflector").setType(DefaultClassReflector.class).build());
		beans.add(beanDefinitionBuilder.setName("$PackageReflector").setType(DefaultPackageReflector.class).build());
		return beans;
	}

	public Set<Class<?>> getScannedComponents() {
		return mScannedComponents;
	}

	public Set<XmlBean> getXmlComponents() {
		return mXmlComponents;
	}

	/**
	 * Returns a {@link Set} of all {@link Class} instances containing the
	 * {@link Component} annotation from the classpath.
	 * 
	 * @return {@code Set} of {@code Classes}
	 */
	protected Set<Class<?>> getClasspathComponents() {
		Set<Class<?>> components = new HashSet<Class<?>>();
		List<String> packages = getScanPackages();
		if (packages.size() == 0)
			return components;
		PackageReflector reflector = new DefaultPackageReflector();
		String[] packageArr = new String[packages.size()];
		Set<Class<?>> classes = reflector.getPackageClasses(mContext, packages.toArray(packageArr));
		addQualifyingComponents(classes, components);
		return components;
	}

	@SuppressWarnings("unchecked")
	private void addQualifyingComponents(Set<Class<?>> classes, Set<Class<?>> components) {
		PackageReflector reflector = new DefaultPackageReflector();
		boolean isAopEnabled = ModuleUtils.hasModule(Module.AOP);
		Class<? extends Annotation> aspectAnnotation = null;
		if (isAopEnabled)
			aspectAnnotation = (Class<? extends Annotation>) reflector.getClass("com.clarionmedia.infinitum.aop.annotation.Aspect");
		for (Class<?> clazz : classes) {
			if (clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Bean.class)
					|| clazz.isAnnotationPresent(aspectAnnotation))
				components.add(clazz);
		}
	}

	@SuppressWarnings("unchecked")
	private Set<Class<? extends BeanFactoryPostProcessor>> getAndRemoveBeanFactoryPostProcessors(Collection<Class<?>> components) {
		Set<Class<? extends BeanFactoryPostProcessor>> postProcessors = new HashSet<Class<? extends BeanFactoryPostProcessor>>();
		Iterator<Class<?>> iter = components.iterator();
		while (iter.hasNext()) {
			Class<?> component = iter.next();
			if (BeanFactoryPostProcessor.class.isAssignableFrom(component)) {
				postProcessors.add((Class<BeanFactoryPostProcessor>) component);
				iter.remove();
			}
		}
		return postProcessors;
	}

	@SuppressWarnings("unchecked")
	private Set<Class<? extends BeanPostProcessor>> getAndRemoveBeanPostProcessors(Collection<Class<?>> components) {
		Set<Class<? extends BeanPostProcessor>> postProcessors = new HashSet<Class<? extends BeanPostProcessor>>();
		Iterator<Class<?>> iter = components.iterator();
		while (iter.hasNext()) {
			Class<?> component = iter.next();
			if (BeanPostProcessor.class.isAssignableFrom(component)) {
				postProcessors.add((Class<BeanPostProcessor>) component);
				iter.remove();
			}
		}
		return postProcessors;
	}

	@SuppressWarnings("unchecked")
	private Set<Class<? extends BeanProvider>> getAndRemoveBeanProviders(Collection<Class<?>> components) {
		Set<Class<? extends BeanProvider>> beanProviders = new HashSet<Class<? extends BeanProvider>>();
		Iterator<Class<?>> iter = components.iterator();
		while (iter.hasNext()) {
			Class<?> component = iter.next();
			if (BeanProvider.class.isAssignableFrom(component)) {
				beanProviders.add((Class<BeanProvider>) component);
				iter.remove();
			}
		}
		return beanProviders;
	}

	private void executeBeanPostProcessors(Collection<Class<? extends BeanPostProcessor>> postProcessors) {
		for (Class<? extends BeanPostProcessor> postProcessor : postProcessors) {
			try {
				BeanPostProcessor postProcessorInstance = postProcessor.newInstance();
				for (Entry<String, AbstractBeanDefinition> bean : mBeanFactory.getBeanDefinitions().entrySet()) {
					postProcessorInstance.postProcessBean(this, bean.getValue());
				}
			} catch (InstantiationException e) {
				throw new InfinitumRuntimeException("BeanPostProcessor '" + postProcessor.getName() + "' must have an empty constructor.");
			} catch (IllegalAccessException e) {
				throw new InfinitumRuntimeException("BeanPostProcessor '" + postProcessor.getName() + "' could not be invoked.", e);
			}
		}
	}

	private void executeBeanFactoryPostProcessors(Collection<Class<? extends BeanFactoryPostProcessor>> postProcessors) {
		for (Class<? extends BeanFactoryPostProcessor> postProcessor : postProcessors) {
			try {
				BeanFactoryPostProcessor postProcessorInstance = postProcessor.newInstance();
				postProcessorInstance.postProcessBeanFactory(mBeanFactory);
			} catch (InstantiationException e) {
				throw new InfinitumRuntimeException("BeanFactoryPostProcessor '" + postProcessor.getName()
						+ "' must have an empty constructor.");
			} catch (IllegalAccessException e) {
				throw new InfinitumRuntimeException("BeanFactoryPostProcessor '" + postProcessor.getName() + "' could not be invoked.", e);
			}
		}
	}

	private void registerProviderBeans(Set<Class<? extends BeanProvider>> beanProviders, BeanDefinitionBuilder builder) {
		// Register context-provided beans
		for (AbstractBeanDefinition bean : getBeans(builder))
			mBeanFactory.registerBean(bean);
		for (InfinitumContext childContext : getChildContexts()) {
			for (AbstractBeanDefinition bean : ((BeanProvider) childContext).getBeans(builder))
				mBeanFactory.registerBean(bean);
		}
		// Register user-provided beans
		for (Class<? extends BeanProvider> beanProvider : beanProviders) {
			try {
				BeanProvider beanProviderInstance = beanProvider.newInstance();
				for (AbstractBeanDefinition bean : beanProviderInstance.getBeans(builder))
					mBeanFactory.registerBean(bean);
			} catch (InstantiationException e) {
				throw new InfinitumRuntimeException("BeanProvider '" + beanProvider.getName() + "' must have an empty constructor.");
			} catch (IllegalAccessException e) {
				throw new InfinitumRuntimeException("BeanProvider '" + beanProvider.getName() + "' could not be invoked.", e);
			}
		}
	}

}
