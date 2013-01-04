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

package com.clarionmedia.infinitum.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * <p>
 * This interface provides reflection methods for working with classes contained
 * within projects that are using Infinitum.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 05/17/12
 * @since 1.0
 */
public interface ClassReflector {

	/**
	 * Indicates if the given {@link Object} or {@code Object} proxy is
	 * {@code null}.
	 * 
	 * @param object
	 *            the {@code Object} to check
	 * @return {@code true} if {@code object} is {@code null}, {@code false} if
	 *         not
	 */
	boolean isNull(Object object);

	/**
	 * Retrieves all {@code Fields} for the given {@link Class}.
	 * 
	 * @param clazz
	 *            the {@code Class} to get {@code Fields} for
	 * @return {@link List} of {@code Fields}
	 */
	List<Field> getAllFields(Class<?> clazz);

	/**
	 * Retrieves all {@code Methods} for the given {@link Class}.
	 * 
	 * @param clazz
	 *            the {@code Class} to get {@code Methods} for
	 * @return {@link List} of {@code Methods}
	 */
	List<Method> getAllMethods(Class<?> clazz);

	/**
	 * Retrieves all {@code Methods} for the given {@link Class} with the given
	 * name.
	 * 
	 * @param clazz
	 *            the {@code Class} to get {@code Methods} for
	 * @param name
	 *            the name of the {@code Methods} to retrieve
	 * @return {@link List} of {@code Methods}
	 */
	List<Method> getMethodsByName(Class<?> clazz, String name);

	/**
	 * Retrieves the {@link Method} with the given name and parameter types from
	 * the given {@link Class}.
	 * 
	 * @param name
	 *            the name of the {@code Method}
	 * @param clazz
	 *            the {@code Class} containing the desired {@code Method}
	 * @param paramTypes
	 *            the {@code Method} parameter types
	 * @return {@code Method}
	 */
	Method getMethod(Class<?> clazz, String name, Class<?>... paramTypes);

	/**
	 * Retrieves all {@code Methods} for the given {@link Class} that have the
	 * given {@link Annotation}.
	 * 
	 * @param clazz
	 *            the {@code Class} to get {@code Methods} for
	 * @param annotation
	 *            the {@code Annotation} type to find {@code Methods} for
	 * @return {@link List} of {@code Methods}
	 */
	List<Method> getAllMethodsAnnotatedWith(Class<?> clazz, Class<? extends Annotation> annotation);

	/**
	 * Retrieves all {@code Constructors} for the given {@link Class}.
	 * 
	 * @param clazz
	 *            the {@code Class} to get {@code Constructors} for
	 * @return {@link List} of {@code Constructors}
	 */
	List<Constructor<?>> getAllConstructors(Class<?> clazz);

	/**
	 * Retrieves the {@link Field} with the given name for the given
	 * {@link Class}.
	 * 
	 * @param clazz
	 *            the {@code Class} to retrieve the {@code Field} from
	 * @param name
	 *            the name of the {@code Field}
	 * @return {@code Field} with the given name or {@code null} if it does not
	 *         exist
	 */
	Field getField(Class<?> clazz, String name);

	/**
	 * Retrieves the value of the given {@link Field} for the given
	 * {@link Object}.
	 * 
	 * @param object
	 *            the {@code Object} to retrieve the value for
	 * @param field
	 *            the {@code Field} to retrieve the value for
	 * @return {@code Field} value
	 */
	Object getFieldValue(Object object, Field field);

	/**
	 * Retrieves a new instance of the given {@link Class} by invoking its empty
	 * constructor.
	 * 
	 * @param clazz
	 *            the {@code Class} to construct
	 * @return new instance of {@code Class}
	 */
	<T> T getClassInstance(Class<T> clazz);

	/**
	 * Retrieves a new instance of the {@link Class} with the given
	 * {@link Constructor} by invoking the {@code Constructor} with the given
	 * arguments.
	 * 
	 * @return new instance of the {@code Class} with the given
	 *         {@code Constructor}
	 */
	<T> T getClassInstance(Constructor<T> ctor, Object... args);

	/**
	 * Invokes the given {@link Method} on {@code receiver} using the provided
	 * arguments.
	 * 
	 * @param receiver
	 *            the {@link Object} to invoke the {@code Method} on
	 * @param method
	 *            the {@code Method} to invoke
	 * @param args
	 *            the arguments to use
	 * @return the {@code Method} return value or {@code null} if there is none
	 */
	Object invokeMethod(Object receiver, Method method, Object... args);

	/**
	 * Sets the value of the given {@link Field} for the given {@link Object}.
	 * 
	 * @param object
	 *            the {@code Object} to set the value for
	 * @param field
	 *            the {@code Field} to set the value for
	 * @param value
	 *            {@code Field} value to set
	 */
	void setFieldValue(Object object, Field field, Object value);

	/**
	 * Copies all of the fields in the "from" {@link Object} to the "to"
	 * {@code Object}.
	 * 
	 * @param from
	 *            the {@code Object} to copy from
	 * @param to
	 *            the {@code Object} to copy to
	 */
	void copyFields(Object from, Object to);

	/**
	 * Returns the first interface extending the given type in the class
	 * hierarchy for the given {@link Class}. If the {@code Class} itself is a
	 * qualifying interface, it will return itself.
	 * 
	 * @param clazz
	 *            the {@code Class} to retrieve an interface for
	 * @param interfaceType
	 *            the interface type the interface to retrieve must be
	 *            assignable from
	 * @return super interface or {@code null} if none exists
	 */
	Class<?> getSuperInterface(Class<?> clazz, Class<?> interfaceType);

	/**
	 * Returns the first interface in the class hierarchy for the given
	 * {@link Class}. If the {@code Class} itself is an interface, it will
	 * return itself.
	 * 
	 * @param clazz
	 *            the {@code Class} to retrieve an interface for
	 * @return super interface or {@code null} if none exists
	 */
	Class<?> getSuperInterface(Class<?> clazz);

	/**
	 * Indicates if the given {@link Class} has a {@link Method} with the given
	 * {@link Annotation}.
	 * 
	 * @param clazz
	 *            the {@code Class} to check
	 * @param annotation
	 *            the {@code Annotation} to check for
	 * @return {@code true} if the {@code Class} has the {@code Annotation},
	 *         {@code false} if not
	 */
	boolean containsMethodAnnotation(Class<?> clazz, Class<? extends Annotation> annotation);

}
