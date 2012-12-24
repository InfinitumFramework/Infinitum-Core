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

package com.clarionmedia.infinitum.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;

import com.clarionmedia.infinitum.aop.AspectComponent;
import com.clarionmedia.infinitum.aop.annotation.Aspect;
import com.clarionmedia.infinitum.aop.impl.AnnotationsAspectWeaver;
import com.clarionmedia.infinitum.aop.impl.XmlAspectWeaver;
import com.clarionmedia.infinitum.context.impl.XmlApplicationContext;
import com.clarionmedia.infinitum.di.AbstractBeanDefinition;
import com.clarionmedia.infinitum.di.BeanComponent;
import com.clarionmedia.infinitum.di.BeanDefinitionBuilder;
import com.clarionmedia.infinitum.di.BeanFactory;
import com.clarionmedia.infinitum.di.BeanFactoryPostProcessor;
import com.clarionmedia.infinitum.di.BeanPostProcessor;
import com.clarionmedia.infinitum.di.annotation.Bean;
import com.clarionmedia.infinitum.di.annotation.Component;
import com.clarionmedia.infinitum.di.annotation.Scope;
import com.clarionmedia.infinitum.di.impl.AutowiredBeanPostProcessor;
import com.clarionmedia.infinitum.di.impl.GenericBeanDefinitionBuilder;
import com.clarionmedia.infinitum.exception.InfinitumRuntimeException;
import com.clarionmedia.infinitum.internal.StringUtil;
import com.clarionmedia.infinitum.reflection.PackageReflector;
import com.clarionmedia.infinitum.reflection.impl.DefaultClassReflector;
import com.clarionmedia.infinitum.reflection.impl.DefaultPackageReflector;

/**
 * <p>
 * Abstract implementation of {@link InfinitumContext}.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 07/11/12
 * @since 1.0
 */
public abstract class AbstractContext implements InfinitumContext {

	protected BeanFactory mBeanFactory;
	protected Context mContext;
	protected List<InfinitumContext> mChildContexts;
	protected InfinitumContext mParentContext;

	/**
	 * Returns a {@link List} of {@link BeanComponent} instances that were
	 * registered with the context through the Infinitum XML configuration.
	 * 
	 * @return {@code List} of {@code BeanComponents}
	 */
	protected abstract List<BeanComponent> getBeans();

	/**
	 * Returns a {@link List} of {@link AspectComponent} instances that were
	 * registered with the context through the Infinitum XML configuration.
	 * 
	 * @return {@code List} of {@code AspectComponents}
	 */
	protected abstract List<AspectComponent> getAspects();

	/**
	 * Returns the {@link RestfulContext} that was registered with the context
	 * through the Infinitum XML configuration.
	 * 
	 * @return {@code RestfulContext} or {@code null} if none was registered
	 */
	protected abstract RestfulContext getRestContext();

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
	}

	/**
	 * Constructs a new {@code AbstractContext} instance as a child of the given
	 * parent context.
	 * 
	 * @param parentContext
	 *            the parent context
	 */
	public AbstractContext(InfinitumContext parentContext) {
		mChildContexts = new ArrayList<InfinitumContext>();
		mParentContext = parentContext;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void postProcess(Context context) {
		
		// Post process child contexts
		for (InfinitumContext childContext : getChildContexts()) {
			childContext.postProcess(context);
		}

		mContext = context;
		PackageReflector reflector = new DefaultPackageReflector();
		RestfulContext restContext = getRestContext();
		if (restContext != null)
			restContext.setParentContext(this);

		// Register XML beans
		registerFrameworkComponents();
		List<BeanComponent> beans = getBeans();
		mBeanFactory.registerBeans(beans); // Also registers aspects implicitly

		// Get XML components
		Set<Class<?>> xmlAspects = new HashSet<Class<?>>();
		Set<Class<? extends BeanPostProcessor>> xmlBeanPostProcessors = new HashSet<Class<? extends BeanPostProcessor>>();
		xmlBeanPostProcessors.add(AutowiredBeanPostProcessor.class); // Takes care of autowiring
		Set<Class<BeanFactoryPostProcessor>> xmlBeanFactoryPostProcessors = new HashSet<Class<BeanFactoryPostProcessor>>();
		for (BeanComponent bean : beans) {
			Class<?> clazz = reflector.getClass(bean.getClassName());
			if (AspectComponent.class.equals(clazz))
				xmlAspects.add(clazz);
			else if (BeanPostProcessor.class.isAssignableFrom(clazz))
				xmlBeanPostProcessors.add((Class<BeanPostProcessor>) clazz);
			else if (BeanFactoryPostProcessor.class.isAssignableFrom(clazz))
				xmlBeanFactoryPostProcessors.add((Class<BeanFactoryPostProcessor>) clazz);
		}

		// Scan for annotated components
		Set<Class<?>> components = new HashSet<Class<?>>();
		if (isComponentScanEnabled())
			components.addAll(getClasspathComponents());

		// Categorize the components while filtering down the original Set
		Set<Class<?>> aspects = getAndRemoveAspects(components);
		Set<Class<? extends BeanPostProcessor>> beanPostProcessors = getAndRemoveBeanPostProcessors(components);
		beanPostProcessors.addAll(xmlBeanPostProcessors);
		Set<Class<BeanFactoryPostProcessor>> beanFactoryPostProcessors = getAndRemoveBeanFactoryPostProcessors(components);
		beanFactoryPostProcessors.addAll(xmlBeanFactoryPostProcessors);

		// Register scanned aspects
		BeanDefinitionBuilder beanDefinitionBuilder = new GenericBeanDefinitionBuilder(mBeanFactory);
		for (Class<?> aspectClass : aspects) {
			Aspect aspect = aspectClass.getAnnotation(Aspect.class);
			String beanName = aspect.value().trim().equals("") ? StringUtil.toCamelCase(aspectClass.getSimpleName()) : aspect.value()
					.trim();
			Scope scope = aspectClass.getAnnotation(Scope.class);
			String scopeVal = "singleton";
			if (scope != null)
				scopeVal = scope.value();
			AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.setName(beanName).setType(aspectClass).setProperties(null)
					.setScope(scopeVal).build();
			mBeanFactory.registerAspect(beanDefinition);
		}

		// Register scanned bean candidates
		for (Class<?> candidate : components) {
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

		// Process aspects
		// Currently not supporting use of XML and annotation aspects in
		// conjunction, only one or the other right now...
		if (isComponentScanEnabled())
			new AnnotationsAspectWeaver(this).weave(mContext, aspects);
		else
			new XmlAspectWeaver(this, getAspects()).weave(mContext, null);

		// Execute post processors
		executeBeanPostProcessors(beanPostProcessors);
		executeBeanFactoryPostProcessors(beanFactoryPostProcessors);
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
		for (Class<?> clazz : classes) {
			if (clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Bean.class)
					|| clazz.isAnnotationPresent(Aspect.class))
				components.add(clazz);
		}
		return components;
	}

	private Set<Class<?>> getAndRemoveAspects(Collection<Class<?>> components) {
		Set<Class<?>> aspects = new HashSet<Class<?>>();
		Iterator<Class<?>> iter = components.iterator();
		while (iter.hasNext()) {
			Class<?> component = iter.next();
			if (component.isAnnotationPresent(Aspect.class)) {
				aspects.add(component);
				iter.remove();
			}
		}
		return aspects;
	}

	@SuppressWarnings("unchecked")
	private Set<Class<BeanFactoryPostProcessor>> getAndRemoveBeanFactoryPostProcessors(Collection<Class<?>> components) {
		Set<Class<BeanFactoryPostProcessor>> postProcessors = new HashSet<Class<BeanFactoryPostProcessor>>();
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
				throw new InfinitumRuntimeException("BeanPostProcessor '" + postProcessor.getName() + "' could not be invoked.");
			}
		}
	}

	private void executeBeanFactoryPostProcessors(Collection<Class<BeanFactoryPostProcessor>> postProcessors) {
		for (Class<BeanFactoryPostProcessor> postProcessor : postProcessors) {
			try {
				BeanFactoryPostProcessor postProcessorInstance = postProcessor.newInstance();
				postProcessorInstance.postProcessBeanFactory(mBeanFactory);
			} catch (InstantiationException e) {
				throw new InfinitumRuntimeException("BeanFactoryPostProcessor '" + postProcessor.getName()
						+ "' must have an empty constructor.");
			} catch (IllegalAccessException e) {
				throw new InfinitumRuntimeException("BeanFactoryPostProcessor '" + postProcessor.getName() + "' could not be invoked.");
			}
		}
	}

	/**
	 * Registers components used internally by the framework for injection.
	 */
	private void registerFrameworkComponents() {
		BeanDefinitionBuilder beanDefinitionBuilder = new GenericBeanDefinitionBuilder(mBeanFactory);
		AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.setName("$InfinitumContext").setType(XmlApplicationContext.class)
				.build();
		mBeanFactory.registerBean(beanDefinition);
		beanDefinition = beanDefinitionBuilder.setName("$RestContext").setType(RestfulContext.class).build();
		mBeanFactory.registerBean(beanDefinition);
		beanDefinition = beanDefinitionBuilder.setName("$ClassReflector").setType(DefaultClassReflector.class).build();
		mBeanFactory.registerBean(beanDefinition);
		beanDefinition = beanDefinitionBuilder.setName("$PackageReflector").setType(DefaultPackageReflector.class).build();
		mBeanFactory.registerBean(beanDefinition);
	}

}
