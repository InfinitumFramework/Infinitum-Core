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

package com.clarionmedia.infinitum.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.clarionmedia.infinitum.context.exception.InfinitumConfigurationException;
import com.clarionmedia.infinitum.di.annotation.Autowired;
import com.clarionmedia.infinitum.di.annotation.PostConstruct;
import com.clarionmedia.infinitum.di.impl.PrototypeBeanDefinition;
import com.clarionmedia.infinitum.di.impl.SingletonBeanDefinition;
import com.clarionmedia.infinitum.internal.Preconditions;
import com.clarionmedia.infinitum.internal.Primitives;
import com.clarionmedia.infinitum.reflection.ClassReflector;
import com.clarionmedia.infinitum.reflection.impl.DefaultClassReflector;

/**
 * <p>
 * Describes a bean instance, including its name, type, property values, and
 * constructor arguments.
 * </p>
 *
 * @author Tyler Treat
 * @version 1.0 08/04/12
 * @see SingletonBeanDefinition
 * @see PrototypeBeanDefinition
 * @since 1.0
 */
public abstract class AbstractBeanDefinition {

    protected String mName;
    protected Class<?> mType;
    protected Map<String, Object> mProperties;
    protected Map<Field, AbstractBeanDefinition> mFieldInjections;
    protected Map<Method, AbstractBeanDefinition> mSetterInjections;
    protected ClassReflector mClassReflector;
    protected BeanFactory mBeanFactory;
    protected AbstractProxy mBeanProxy;

    /**
     * Creates a new {@code AbstractBeanDefinition}.
     *
     * @param beanFactory the {@link BeanFactory} this bean definition is scoped to
     */
    public AbstractBeanDefinition(BeanFactory beanFactory) {
        mClassReflector = new DefaultClassReflector();
        mBeanFactory = beanFactory;
        mFieldInjections = new HashMap<Field, AbstractBeanDefinition>();
        mSetterInjections = new HashMap<Method, AbstractBeanDefinition>();
    }

    /**
     * Returns an instance of the bean, which could be a proxy for it.
     *
     * @return bean or bean proxy
     */
    public abstract Object getBeanInstance();

    /**
     * Returns an instance of the bean, guaranteeing that it is not a proxy for
     * the bean.
     *
     * @return bean
     */
    public abstract Object getNonProxiedBeanInstance();

    /**
     * Returns the bean name.
     *
     * @return bean name
     */
    public String getName() {
        return mName;
    }

    /**
     * Sets the bean name.
     *
     * @param name the bean name to set
     */
    public void setName(String name) {
        mName = name;
    }

    /**
     * Returns the bean type.
     *
     * @return bean type
     */
    public Class<?> getType() {
        return mType;
    }

    /**
     * Sets the bean type.
     *
     * @param type the bean type to set
     */
    public void setType(Class<?> type) {
        mType = type;
    }

    /**
     * Returns a {@link Map} consisting of the bean properties and their values.
     *
     * @return bean properties
     */
    public Map<String, Object> getProperties() {
        return mProperties;
    }

    /**
     * Sets the {@link Map} consisting of the bean properties and their values.
     *
     * @param properties the bean properties to set
     */
    public void setProperties(Map<String, Object> properties) {
        mProperties = properties;
    }

    /**
     * Returns the bean proxy.
     *
     * @return bean proxy or {@code null} if there is none
     */
    public AbstractProxy getBeanProxy() {
        return mBeanProxy;
    }

    /**
     * Sets the bean proxy.
     *
     * @param beanProxy the bean proxy to set
     */
    public void setBeanProxy(AbstractProxy beanProxy) {
        mBeanProxy = beanProxy;
    }

    /**
     * Returns a {@link Map} consisting of the {@link Field} instances and
     * values to inject.
     *
     * @return {@code Field} injection {@code Map}
     */
    public Map<Field, AbstractBeanDefinition> getFieldInjections() {
        return mFieldInjections;
    }

    /**
     * Sets the {@link Map} consisting of the {@link Field} instances and values
     * to inject.
     *
     * @param injections the {@code Field} injection {@code Map} to set
     */
    public void setFieldInjections(Map<Field, AbstractBeanDefinition> injections) {
        mFieldInjections = injections;
    }

    /**
     * Adds a {@link Field}-value pair to be injected.
     *
     * @param field the {@code Field} to inject
     * @param value the value to inject
     */
    public void addFieldInjection(Field field, AbstractBeanDefinition value) {
        mFieldInjections.put(field, value);
    }

    /**
     * Returns the {@link Map} consisting of the setter {@link Method} instances
     * and values to inject.
     *
     * @return setter injection {@code Map}
     */
    public Map<Method, AbstractBeanDefinition> getSetterInjections() {
        return mSetterInjections;
    }

    /**
     * Sets the {@link Map} consisting of the setter {@link Method} and values
     * to inject.
     *
     * @param setterInjections the setter injection {@code Map} to set
     */
    public void setSetterInjections(Map<Method, AbstractBeanDefinition> setterInjections) {
        mSetterInjections = setterInjections;
    }

    /**
     * Adds a {@link Method}-value pair to be injected.
     *
     * @param setter the setter to inject
     * @param value  value to inject
     */
    public void addSetterInjection(Method setter, AbstractBeanDefinition value) {
        mSetterInjections.put(setter, value);
    }

    /**
     * Creates an instance of the bean specified by this
     * {@code AbstractBeanDefinition}.
     *
     * @return bean instance
     */
    protected Object createBean() {
        Constructor<?> autowiredCtor = getAutowiredConstructor();
        if (autowiredCtor == null)
            return mClassReflector.getClassInstance(mType);
        Class<?>[] paramTypes = autowiredCtor.getParameterTypes();
        Object[] args = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            Object arg = BeanUtils.findCandidateBean(mBeanFactory, paramTypes[i]);
            if (arg == null)
                throw new InfinitumConfigurationException("Could not autowire constructor argument of type '" + paramTypes[i].getName()
                        + "' in bean '" + mName + "' (no autowire candidates found)");
            args[i] = arg;
        }
        return mClassReflector.getClassInstance(autowiredCtor, args);
    }

    /**
     * Performs dependency injection (field and setter) on the given bean
     * {@link Object}.
     *
     * @param bean the bean to inject
     */
    protected void inject(Object bean) {
        for (Entry<Field, AbstractBeanDefinition> injection : mFieldInjections.entrySet()) {
            mClassReflector.setFieldValue(bean, injection.getKey(), injection.getValue().getBeanInstance());
        }
        for (Entry<Method, AbstractBeanDefinition> injection : mSetterInjections.entrySet()) {
            mClassReflector.invokeMethod(bean, injection.getKey(), injection.getValue().getBeanInstance());
        }
    }

    /**
     * Invokes the method annotated with {@link PostConstruct} if it exists. An
     * {@link InfinitumConfigurationException} is thrown if more than one is
     * found.
     *
     * @param bean the bean to invoke the {@code PostConstruct} method for
     */
    protected void postConstruct(Object bean) {
        List<Method> postConstructs = mClassReflector.getAllMethodsAnnotatedWith(mType, PostConstruct.class);
        if (postConstructs.size() > 1)
            throw new InfinitumConfigurationException("Only 1 method may be annotated with PostConstruct (found " + postConstructs.size()
                    + " in '" + mType.getName() + "')");
        if (postConstructs.size() == 1)
            mClassReflector.invokeMethod(bean, postConstructs.get(0));
    }

    /**
     * Injects any properties into the given bean {@link Object}.
     *
     * @param bean the bean to inject
     */
    protected void setFields(Object bean) {
        Preconditions.checkNotNull(bean);
        if (mProperties == null || mProperties.size() == 0)
            return;
        for (Entry<String, Object> property : mProperties.entrySet()) {
            // Find the field
            Field field = mClassReflector.getField(bean.getClass(), property.getKey());
            if (field == null)
                continue;
            Class<?> type = Primitives.unwrap(field.getType());
            Object val = property.getValue();
            String argStr = null;
            if (val.getClass() == String.class)
                argStr = (String) val;
            Object arg = null;
            // Parse the string value into the proper type
            if (argStr != null) {
                if (type == boolean.class)
                    arg = Boolean.parseBoolean(argStr);
                else if (type == int.class)
                    arg = Integer.parseInt(argStr);
                else if (type == double.class)
                    arg = Double.parseDouble(argStr);
                else if (type == float.class)
                    arg = Float.parseFloat(argStr);
                else if (type == long.class)
                    arg = Long.parseLong(argStr);
                else if (type == char.class)
                    arg = argStr.charAt(0);
                else if (type == byte.class)
                    arg = Byte.parseByte(argStr);
                else
                    arg = val;
            } else {
                arg = val;
            }
            // Populate the field's value
            mClassReflector.setFieldValue(bean, field, arg);
        }
    }

    /**
     * Retrieves the {@link Constructor} annotated with {@link Autowired}, if
     * there is one. An {@link InfinitumConfigurationException} is thrown if
     * more than one is found.
     */
    private Constructor<?> getAutowiredConstructor() {
        Constructor<?> autowiredCtor = null;
        for (Constructor<?> ctor : mClassReflector.getAllConstructors(mType)) {
            if (ctor.isAnnotationPresent(Autowired.class)) {
                if (autowiredCtor != null)
                    throw new InfinitumConfigurationException("Only 1 constructor may be autowired (found more than 1 in class '"
                            + mType.getName() + "').");
                autowiredCtor = ctor;
            }
        }
        return autowiredCtor;
    }

}
