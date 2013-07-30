/*
 * Copyright (C) 2013 Clarion Media, LLC
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

import android.content.Context;
import com.clarionmedia.infinitum.logging.Logger;
import com.clarionmedia.infinitum.logging.impl.SmartLogger;
import com.clarionmedia.infinitum.reflection.ClasspathReflector;
import dalvik.system.DexFile;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * <p> Implementation of {@link ClasspathReflector} which relies on Dalvik's {@code classes.dex} exposed through {@link
 * DexFile}. </p>
 *
 * @author Tyler Treat
 * @version 1.1.1 07/29/13
 * @since 1.0
 */
public class DexClasspathReflector implements ClasspathReflector {

    private Logger mLogger;

    /**
     * Creates a new {@code DexClasspathReflector} instance.
     */
    public DexClasspathReflector() {
        mLogger = new SmartLogger(getClass().getSimpleName());
    }

    @Override
    public synchronized Set<Class<?>> getPackageClasses(Context context, String... packageNames) {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        try {
            DexFile dex = new DexFile(context.getApplicationInfo().sourceDir);
            Enumeration<String> entries = dex.entries();
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Locale locale = Locale.getDefault();
            while (entries.hasMoreElements()) {
                String entry = entries.nextElement();
                for (String packageName : packageNames) {
                    if (entry.toLowerCase(locale).startsWith(packageName.toLowerCase(locale))) {
                        addClass(classes, entry, classLoader);
                        break;
                    }
                }
            }
            dex.close();
            return classes;
        } catch (IOException e) {
            mLogger.error("Component scanning is not supported in this environment.", e);
            return classes;
        }
    }

    private void addClass(Set<Class<?>> classes, String name, ClassLoader classLoader) {
        try {
            classes.add(classLoader.loadClass(name));
        } catch (ClassNotFoundException e) {
            mLogger.error("Failed to load class '" + name + "'.", e);
        }
    }

}
