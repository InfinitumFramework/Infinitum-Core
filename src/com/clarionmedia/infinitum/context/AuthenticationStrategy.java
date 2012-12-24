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

import org.apache.http.impl.client.RequestWrapper;

/**
 * <p>
 * Describes how web service requests are authenticated. This should be
 * implemented for specific web service authentication strategies. If using
 * token or shared-secret authentication, {@link SharedSecretAuthentication}
 * should be used.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 03/21/12
 * @since 1.0
 */
public interface AuthenticationStrategy {

	/**
	 * Adds the necessary authentication information to the given
	 * {@link RequestWrapper}.
	 * 
	 * @param request
	 *            the request to authenticate
	 */
	void authenticate(RequestWrapper request);

}
