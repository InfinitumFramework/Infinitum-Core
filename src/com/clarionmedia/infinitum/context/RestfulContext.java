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


/**
 * <p>
 * Container for RESTful web service configuration settings.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 05/18/12
 * @since 1.0
 */
public interface RestfulContext {

	/**
	 * Describes the message type to use for web-service PUT/POST requests.
	 */
	public static enum MessageType {
		XML, JSON, PAIRS
	};

	/**
	 * Returns the parent context containing this {@code RestfulConfiguration}.
	 * 
	 * @return parent {@code InfinitumContext}
	 */
	InfinitumContext getParentContext();

	/**
	 * Sets the parent context containing this {@code RestfulConfiguration}.
	 * 
	 * @param context
	 *            parent (@code InfinitumContext}
	 */
	void setParentContext(InfinitumContext context);

	/**
	 * Returns the configured web service host URL.
	 * 
	 * @return web service URL
	 */
	String getRestHost();

	/**
	 * Sets the web service host URL.
	 * 
	 * @param restHost
	 *            the host to set
	 */
	void setRestHost(String restHost);

	/**
	 * Sets the value indicating if the web service requires authentication.
	 * 
	 * @param isRestAuthenticated
	 *            {@code true} if authentication is required, {@code false} if
	 *            not
	 */
	void setRestAuthenticated(boolean isRestAuthenticated);

	/**
	 * Indicates if the web service requires authentication.
	 * 
	 * @return {@code true} if authentication is required, {@code false} if not
	 */
	boolean isRestAuthenticated();

	/**
	 * Retrieves the configured connection timeout in milliseconds. This is the
	 * timeout used until a connection is established with the web service.
	 * 
	 * @return connection timeout in milliseconds
	 */
	int getConnectionTimeout();

	/**
	 * Sets the connection timeout. This is the timeout used, in milliseconds,
	 * until a connection is established with the web service. The default value
	 * is zero.
	 * 
	 * @param connectionTimeout
	 *            the connection timeout to set
	 */
	void setConnectionTimeout(int connectionTimeout);

	/**
	 * Retrieves the configured response timeout in milliseconds. This is the
	 * timeout for waiting for data from the web service. A timeout of zero is
	 * interpreted as an infinite timeout.
	 * 
	 * @return response timeout in milliseconds
	 */
	int getResponseTimeout();

	/**
	 * Sets the response timeout. This is the timeout, in milliseconds, for
	 * waiting for data from the web service. A timeout of zero is interpreted
	 * as an infinite timeout.
	 * 
	 * @param responseTimeout
	 *            the response timeout in milliseconds
	 */
	void setResponseTimeout(int responseTimeout);

	/**
	 * Retrieves the name of the bean for the configured {@link RestfulSession}.
	 * 
	 * @return the name of the {@code RestfulClient} bean or {@code null} if it
	 *         has not been configured
	 */
	String getClientBean();

	/**
	 * Sets the name of the bean for the {@link RestfulSession}.
	 * 
	 * @param clientBean
	 *            the name of the {@code RestfulClient} bean
	 */
	void setClientBean(String clientBean);

	/**
	 * Retrieves the {@code MessageType} for the configured
	 * {@link RestfulSession}.
	 * 
	 * @return {@code MessageType}
	 */
	MessageType getMessageType();

	/**
	 * Sets the {@code MessageType} for the {@link RestfulSession}.
	 * 
	 * @param messageType
	 *            the {@code MessageType} to set
	 */
	void setMessageType(MessageType messageType);

}
