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

package com.clarionmedia.infinitum.di.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;

import com.clarionmedia.infinitum.activity.annotation.Bind;
import com.clarionmedia.infinitum.activity.annotation.InjectLayout;
import com.clarionmedia.infinitum.activity.annotation.InjectResource;
import com.clarionmedia.infinitum.activity.annotation.InjectView;
import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.context.exception.InfinitumConfigurationException;
import com.clarionmedia.infinitum.di.ActivityInjector;
import com.clarionmedia.infinitum.di.BeanFactory;
import com.clarionmedia.infinitum.di.BeanUtils;
import com.clarionmedia.infinitum.di.annotation.Autowired;
import com.clarionmedia.infinitum.exception.InfinitumRuntimeException;
import com.clarionmedia.infinitum.reflection.ClassReflector;
import com.clarionmedia.infinitum.reflection.impl.DefaultClassReflector;

/**
 * <p>
 * Implementation of {@link ActivityInjector} for injecting Android resources
 * and framework components into any object.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 12/18/12
 * @since 1.0
 */
public class ObjectInjector implements ActivityInjector {

	private Object mObject;
	private ClassReflector mClassReflector;
	private List<Field> mFields;
	private InfinitumContext mInfinitumContext;

	/**
	 * Creates a new {@code ObjectInjector}.
	 * 
	 * @param infinitumContext
	 *            the {@link InfinitumContext} to use
	 * @param object
	 *            the {@link Object} to use
	 */
	public ObjectInjector(InfinitumContext infinitumContext, Object object) {
		mInfinitumContext = infinitumContext;
		mObject = object;
		mClassReflector = new DefaultClassReflector();
		mFields = mClassReflector.getAllFields(mObject.getClass());
	}

	@Override
	public void inject() {
		injectBeans();
		if (mObject instanceof Activity) {
			injectResources();
			injectLayout();
			injectViews();
			injectListeners();
		}
	}

	private void injectBeans() {
		BeanFactory beanFactory = mInfinitumContext.getBeanFactory();
		for (Field field : mFields) {
			if (!field.isAnnotationPresent(Autowired.class))
				continue;
			field.setAccessible(true);
			Autowired autowired = field.getAnnotation(Autowired.class);
			String qualifier = autowired.value().trim();
			Class<?> type = field.getType();
			Object bean = qualifier.equals("") ? BeanUtils.findCandidateBean(
					beanFactory, type) : mInfinitumContext.getBean(qualifier);
			if (bean == null) {
				throw new InfinitumConfigurationException(
						"Could not autowire property of type '"
								+ type.getName() + "' in Activity '"
								+ mObject.getClass().getName()
								+ "' (no autowire candidates found)");
			}
			mClassReflector.setFieldValue(mObject, field, bean);
		}
	}

	/**
	 * Injects the {@code Activity} layout based on the {@code @InjectLayout}
	 * annotation. This takes the place of calling
	 * {@link Activity#setContentView(int)}.
	 */
	private void injectLayout() {
		InjectLayout injectLayout = mObject.getClass().getAnnotation(
				InjectLayout.class);
		if (injectLayout == null)
			return;
		((Activity) mObject).setContentView(injectLayout.value());
	}

	/**
	 * Injects fields annotated with {@code @InjectView}.
	 */
	private void injectViews() {
		for (Field field : mFields) {
			if (!field.isAnnotationPresent(InjectView.class))
				continue;
			InjectView injectView = field.getAnnotation(InjectView.class);
			int viewId = injectView.value();
			field.setAccessible(true);
			mClassReflector.setFieldValue(mObject, field,
					((Activity) mObject).findViewById(viewId));
		}
	}

	/**
	 * Injects the fields annotated with {@code @InjectResource}.
	 */
	private void injectResources() {
		for (Field field : mFields) {
			if (!field.isAnnotationPresent(InjectResource.class))
				continue;
			InjectResource injectResource = field
					.getAnnotation(InjectResource.class);
			int resourceId = injectResource.value();
			field.setAccessible(true);
			Object resource = resolveResourceForField(field, resourceId);
			mClassReflector.setFieldValue(mObject, field, resource);
		}
	}

	/**
	 * Loads the appropriate resource based on the {@link Field} type and the
	 * given resource ID.
	 */
	private Object resolveResourceForField(Field field, int resourceId) {
		Resources resources = ((Context) mObject).getResources();
		String resourceType = resources.getResourceTypeName(resourceId);
		if (resourceType.equalsIgnoreCase("anim"))
			return AnimationUtils.loadAnimation((Context) mObject, resourceId);
		if (resourceType.equalsIgnoreCase("drawable"))
			return resources.getDrawable(resourceId);
		if (resourceType.equalsIgnoreCase("color"))
			return resources.getColor(resourceId);
		if (resourceType.equalsIgnoreCase("string"))
			return resources.getString(resourceId);
		if (resourceType.equalsIgnoreCase("integer"))
			return resources.getInteger(resourceId);
		if (resourceType.equalsIgnoreCase("bool"))
			return resources.getBoolean(resourceId);
		if (resourceType.equalsIgnoreCase("dimen"))
			return resources.getDimension(resourceId);
		if (resourceType.equalsIgnoreCase("movie"))
			return resources.getMovie(resourceId);
		if (resourceType.equalsIgnoreCase("array")) {
			if (field.getType() == int[].class
					|| field.getType() == Integer[].class)
				return resources.getIntArray(resourceId);
			else if (field.getType() == String[].class
					|| field.getType() == CharSequence[].class)
				return resources.getStringArray(resourceId);
			else
				return resources.obtainTypedArray(resourceId); // TODO: convert
																// to actual
																// array
		}
		if (resourceType.equalsIgnoreCase("id"))
			throw new InfinitumRuntimeException("Unable to inject field '"
					+ field.getName() + "' in Activity '"
					+ mObject.getClass().getName()
					+ "'. Are you injecting a view?");
		throw new InfinitumRuntimeException("Unable to inject field '"
				+ field.getName() + "' in Activity '"
				+ mObject.getClass().getName() + "' (unsupported type).");
	}

	/**
	 * Injects event listeners into {@code View} fields annotated with
	 * {@code Bind}
	 */
	private void injectListeners() {
		for (Field field : mFields) {
			if (!View.class.isAssignableFrom(field.getType())
					|| !field.isAnnotationPresent(Bind.class))
				continue;
			Bind bind = field.getAnnotation(Bind.class);
			Event event = bind.event();
			String callback = bind.value();
			View view = (View) mClassReflector.getFieldValue(mObject, field);
			registerCallback(view, callback, event);
		}
	}

	/**
	 * Registers an event callback.
	 */
	private void registerCallback(View view, String callback, Event event) {
		switch (event) {
		case OnClick:
			final Method onClick = mClassReflector.getMethod(
					mObject.getClass(), callback, View.class);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mClassReflector.invokeMethod(mObject, onClick, v);
				}
			});
			break;
		case OnLongClick:
			final Method onLongClick = mClassReflector.getMethod(
					mObject.getClass(), callback, View.class);
			view.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					return (Boolean) mClassReflector.invokeMethod(mObject,
							onLongClick, v);
				}
			});
			break;
		case OnCreateContextMenu:
			final Method onCreateContextMenu = mClassReflector.getMethod(
					mObject.getClass(), callback, ContextMenu.class,
					View.class, ContextMenu.ContextMenuInfo.class);
			view.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
				@Override
				public void onCreateContextMenu(ContextMenu menu, View v,
						ContextMenu.ContextMenuInfo menuInfo) {
					mClassReflector.invokeMethod(mObject, onCreateContextMenu,
							menu, v, menuInfo);
				}
			});
			break;
		case OnFocusChange:
			final Method onFocusChange = mClassReflector.getMethod(
					mObject.getClass(), callback, View.class, boolean.class);
			view.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					mClassReflector.invokeMethod(mObject, onFocusChange, v,
							hasFocus);
				}
			});
			break;
		case OnKey:
			final Method onKey = mClassReflector.getMethod(mObject.getClass(),
					callback, View.class, int.class, KeyEvent.class);
			view.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					return (Boolean) mClassReflector.invokeMethod(mObject,
							onKey, v, keyCode, event);
				}
			});
			break;
		case OnTouch:
			final Method onTouch = mClassReflector.getMethod(
					mObject.getClass(), callback, View.class,
					MotionEvent.class);
			view.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return (Boolean) mClassReflector.invokeMethod(mObject,
							onTouch, v, event);
				}
			});
			break;
		}
	}

}
