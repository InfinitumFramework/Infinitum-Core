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

package com.clarionmedia.infinitum.reflection.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.clarionmedia.infinitum.di.AbstractProxy;
import com.clarionmedia.infinitum.exception.InfinitumRuntimeException;
import com.clarionmedia.infinitum.reflection.ClassReflector;

/**
 * <p>
 * This class provides reflection methods for working with classes contained
 * within projects that are using Infinitum.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 03/15/12
 * @since 1.0
 */
public class DefaultClassReflector implements ClassReflector {

	@Override
	public boolean isNull(Object object) {
		if (object == null)
			return true;
		if (AbstractProxy.isAopProxy(object)) {
			AbstractProxy proxy = AbstractProxy.getProxy(object);
			return proxy.getTarget() == null;
		}
		return false;
	}

	@Override
	public List<Field> getAllFields(Class<?> clazz) {
		return getAllFieldsRec(clazz, new ArrayList<Field>());
	}

	@Override
	public List<Method> getAllMethods(Class<?> clazz) {
		return getAllMethodsRec(clazz, new ArrayList<Method>());
	}

	@Override
	public List<Method> getMethodsByName(Class<?> clazz, String name) {
		return getAllMethodsRec(clazz, new ArrayList<Method>(), name);
	}

	@Override
	public Method getMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
		try {
			return clazz.getDeclaredMethod(name, paramTypes);
		} catch (SecurityException e) {
			throw new InfinitumRuntimeException("Unable to retrieve method '" + name + "' from '" + clazz.getName() + "'.");
		} catch (NoSuchMethodException e) {
			if (clazz.getSuperclass() == null)
				throw new InfinitumRuntimeException("Method '" + name + "' in '" + clazz.getName() + "' does not exist.");
			return getMethod(clazz.getSuperclass(), name, paramTypes);
		}
	}

	@Override
	public List<Method> getAllMethodsAnnotatedWith(Class<?> clazz, Class<? extends Annotation> annotation) {
		return getAllMethodsRec(clazz, new ArrayList<Method>(), annotation);
	}

	@Override
	public List<Constructor<?>> getAllConstructors(Class<?> clazz) {
		return Arrays.asList(clazz.getDeclaredConstructors());
	}

	@Override
	public Field getField(Class<?> clazz, String name) {
		for (Field f : getAllFields(clazz)) {
			if (f.getName().equals(name))
				return f;
		}
		return null;
	}

	private List<Field> getAllFieldsRec(Class<?> clazz, List<Field> fields) {
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null)
			getAllFieldsRec(superClass, fields);
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		return fields;
	}

	private List<Method> getAllMethodsRec(Class<?> clazz, List<Method> methods) {
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null)
			getAllMethodsRec(superClass, methods);
		methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
		return methods;
	}

	private List<Method> getAllMethodsRec(Class<?> clazz, List<Method> methods, Class<? extends Annotation> annotation) {
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null)
			getAllMethodsRec(superClass, methods, annotation);
		for (final Method method : clazz.getDeclaredMethods()) {
			if (method.isAnnotationPresent(annotation)) {
				methods.add(method);
			}
		}
		return methods;
	}

	private List<Method> getAllMethodsRec(Class<?> clazz, List<Method> methods, String name) {
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null)
			getAllMethodsRec(superClass, methods, name);
		for (final Method method : clazz.getDeclaredMethods()) {
			if (method.getName().equalsIgnoreCase(name))
				methods.add(method);
		}
		return methods;
	}

	@Override
	public <T> T getClassInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new InfinitumRuntimeException("Unable to instantiate '" + clazz.getName() + "'. Does it have a default constructor?");
		} catch (IllegalAccessException e) {
			throw new InfinitumRuntimeException("Unable to instantiate '" + clazz.getName() + "'.");
		}
	}

	@Override
	public <T> T getClassInstance(Constructor<T> ctor, Object... args) {
		if (args.length == 0)
			return getClassInstance(ctor.getDeclaringClass());
		try {
			ctor.setAccessible(true);
			return ctor.newInstance(args);
		} catch (InstantiationException e) {
			throw new InfinitumRuntimeException("Unable to instantiate '" + ctor.getDeclaringClass().getName() + "'.");
		} catch (IllegalAccessException e) {
			throw new InfinitumRuntimeException("Unable to instantiate '" + ctor.getDeclaringClass().getName() + "'.");
		} catch (IllegalArgumentException e) {
			throw new InfinitumRuntimeException("Unable to instantiate '" + ctor.getDeclaringClass().getName() + "'.");
		} catch (InvocationTargetException e) {
			throw new InfinitumRuntimeException("Unable to instantiate '" + ctor.getDeclaringClass().getName() + "'.");
		}
	}

	@Override
	public Object getFieldValue(Object object, Field field) {
		try {
			field.setAccessible(true);
			return field.get(object);
		} catch (IllegalArgumentException e) {
			throw new InfinitumRuntimeException("Unable to access field '" + field.getName() + "' for object of type '"
					+ object.getClass().getName() + "'.", e);
		} catch (IllegalAccessException e) {
			throw new InfinitumRuntimeException("Unable to access field '" + field.getName() + "' for object of type '"
					+ object.getClass().getName() + "'.", e);
		}
	}

	@Override
	public Object invokeMethod(Object receiver, Method method, Object... args) {
		try {
			method.setAccessible(true);
			return method.invoke(receiver, args);
		} catch (IllegalArgumentException e) {
			throw new InfinitumRuntimeException("Unable to invoke method '" + method.getName() + "' for object of type '"
					+ receiver.getClass().getName() + "'.", e);
		} catch (IllegalAccessException e) {
			throw new InfinitumRuntimeException("Unable to invoke method '" + method.getName() + "' for object of type '"
					+ receiver.getClass().getName() + "'.", e);
		} catch (InvocationTargetException e) {
			throw new InfinitumRuntimeException("Unable to invoke method '" + method.getName() + "' for object of type '"
					+ receiver.getClass().getName() + "'.", e);
		}
	}

	@Override
	public void setFieldValue(Object object, Field field, Object value) {
		try {
			field.setAccessible(true);
			field.set(object, value);
		} catch (IllegalArgumentException e) {
			throw new InfinitumRuntimeException("Unable to set field '" + field.getName() + "' for object of type '"
					+ object.getClass().getName() + "'.", e);
		} catch (IllegalAccessException e) {
			throw new InfinitumRuntimeException("Unable to set field '" + field.getName() + "' for object of type '"
					+ object.getClass().getName() + "'.", e);
		}
	}

	@Override
	public void copyFields(Object from, Object to) {
		if (!from.getClass().isAssignableFrom(to.getClass()))
			throw new InfinitumRuntimeException("Class '" + from.getClass().getName() + "' is not assignable from '"
					+ to.getClass().getName() + "'");
		for (Field fromField : getAllFields(from.getClass())) {
			Field toField = getField(to.getClass(), fromField.getName());
			setFieldValue(to, toField, getFieldValue(from, fromField));
		}
	}

	@Override
	public Class<?> getSuperInterface(Class<?> clazz, Class<?> interfaceType) {
		if (clazz.isInterface() && interfaceType.isAssignableFrom(clazz))
			return clazz;
		Class<?>[] interfaces = clazz.getInterfaces();
		for (Class<?> c : interfaces) {
			if (interfaceType.isAssignableFrom(c))
				return c;
		}
		Class<?> superClass = clazz.getSuperclass();
		if (superClass == null)
			return null;
		return getSuperInterface(superClass, interfaceType);
	}

	@Override
	public Class<?> getSuperInterface(Class<?> clazz) {
		if (clazz.isInterface())
			return clazz;
		Class<?>[] interfaces = clazz.getInterfaces();
		if (interfaces.length > 0)
			return interfaces[0];
		Class<?> superClass = clazz.getSuperclass();
		if (superClass == null)
			return null;
		return getSuperInterface(superClass);
	}

	@Override
	public boolean containsMethodAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
		for (Method method : getAllMethods(clazz)) {
			if (method.isAnnotationPresent(annotation))
				return true;
		}
		return false;
	}

}
