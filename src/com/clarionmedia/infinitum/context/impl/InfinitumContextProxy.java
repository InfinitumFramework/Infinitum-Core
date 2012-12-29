package com.clarionmedia.infinitum.context.impl;

import java.lang.reflect.Method;

import com.clarionmedia.infinitum.context.ContextFactory;
import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.di.AopProxy;
import com.clarionmedia.infinitum.di.JdkDynamicProxy;

public class InfinitumContextProxy extends JdkDynamicProxy {
	
	private InfinitumContext mProxiedContext;
	private Class<? extends InfinitumContext> mContextType;

	public InfinitumContextProxy(Class<? extends InfinitumContext> contextType) {
		super(null, new Class<?>[] { contextType });
		mContextType = contextType;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (mProxiedContext == null)
			mProxiedContext = ContextFactory.newInstance().getContext(mContextType);
		return method.invoke(mProxiedContext, args);
	}

	@Override
	public AopProxy clone() {
		throw new UnsupportedOperationException("Clone is not supported for InfinitumContextProxy!");
	}
	
}
