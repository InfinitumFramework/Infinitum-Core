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

package com.clarionmedia.infinitum.event.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * Indicates that the annotated parameter is part of an
 * {@link Event} payload.
 * </p>
 *
 * @author Tyler Treat
 * @version 1.0.4 04/13/13
 * @since 1.0.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface EventPayload {

    /**
     * Declares the name of the payload value. Defaults to the parameter name
     * is not specified.
     *
     * @return payload value name
     */
    String value() default "";

}
