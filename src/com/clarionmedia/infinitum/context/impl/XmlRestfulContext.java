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

package com.clarionmedia.infinitum.context.impl;

import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import com.clarionmedia.infinitum.context.InfinitumContext;
import com.clarionmedia.infinitum.context.RestfulContext;
import com.clarionmedia.infinitum.context.exception.InfinitumConfigurationException;

/**
 * <p>
 * Implementation of {@link RestfulContext} containing RESTful web service
 * configuration information.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0
 * @since 03/30/12
 */
public class XmlRestfulContext implements RestfulContext {

	@Attribute(name = "ref", required = false)
	private String mClientBean;

	@ElementMap(required = true, entry = "property", key = "name", attribute = true, inline = true)
	private Map<String, String> mProperties;

	@Element(name = "authentication", required = false)
	private Authentication mAuthentication;

	private InfinitumContext mParentContext;

	@Override
	public InfinitumContext getParentContext() {
		return mParentContext;
	}

	@Override
	public void setParentContext(InfinitumContext context) {
		mParentContext = context;
	}

	@Override
	public String getRestHost() {
		String host = mProperties.get("host");
		if (host == null || host.trim().length() == 0)
			throw new InfinitumConfigurationException("Web service host not specified!");
		return host;
	}

	@Override
	public void setRestHost(String restHost) {
		if (!restHost.endsWith("/"))
			restHost += '/';
		mProperties.put("host", restHost);
	}

	@Override
	public void setRestAuthenticated(boolean isRestAuthenticated) {
		mAuthentication = new Authentication();
	}

	@Override
	public boolean isRestAuthenticated() {
		if (mAuthentication == null)
			return false;
		return mAuthentication.mIsEnabled;
	}

	@Override
	public int getConnectionTimeout() {
		String timeout = mProperties.get("connectionTimeout");
		if (timeout == null)
			return 0;
		return Integer.parseInt(timeout);
	}

	@Override
	public void setConnectionTimeout(int connectionTimeout) {
		mProperties.put("connectionTimeout", Integer.toString(connectionTimeout));
	}

	@Override
	public int getResponseTimeout() {
		String timeout = mProperties.get("responseTimeout");
		if (timeout == null)
			return 0;
		return Integer.parseInt(timeout);
	}

	@Override
	public void setResponseTimeout(int responseTimeout) {
		mProperties.put("responseTimeout", Integer.toString(responseTimeout));
	}

	@Override
	public String getClientBean() {
		return mClientBean;
	}

	@Override
	public void setClientBean(String clientBean) {
		mClientBean = clientBean;
	}

	public Authentication getAuthentication() {
		return mAuthentication;
	}

	public void setAuthentication(Authentication authentication) {
		mAuthentication = authentication;
	}

	@Override
	public MessageType getMessageType() {
		String messageType = mProperties.get("messageType");
		if (messageType == null || messageType.trim().length() == 0)
			return MessageType.PAIRS; // default to pairs
		if (messageType.equalsIgnoreCase("pairs"))
			return MessageType.PAIRS;
		if (messageType.equalsIgnoreCase("xml"))
			return MessageType.XML;
		if (messageType.equalsIgnoreCase("json"))
			return MessageType.JSON;
		throw new InfinitumConfigurationException("Invalid HTTP message type '" + messageType + "'.");
	}

	@Override
	public void setMessageType(MessageType messageType) {
		switch (messageType) {
		case PAIRS:
			mProperties.put("messageType", "pairs");
			break;
		case XML:
			mProperties.put("messageType", "xml");
			break;
		case JSON:
			mProperties.put("messageType", "json");
			break;
		}
	}

	@Root
	public static class Authentication {

		@Attribute(name = "enabled", required = false)
		private boolean mIsEnabled = true;

		@Attribute(name = "ref", required = false)
		private String mAuthBean;

		@Attribute(name = "strategy", required = false)
		private String mStrategy;

		@Attribute(name = "header", required = false)
		private boolean mHeader;

		@Attribute(name = "generator", required = false)
		private String mGenerator;

		@ElementMap(required = false, entry = "property", key = "name", attribute = true, inline = true)
		private Map<String, String> mAuthProperties;

		public boolean isEnabled() {
			return mIsEnabled;
		}

		public void setEnabled(boolean isEnabled) {
			mIsEnabled = isEnabled;
		}

		public String getAuthBean() {
			return mAuthBean;
		}

		public void setAuthBean(String authBean) {
			mAuthBean = authBean;
		}

		public String getStrategy() {
			return mStrategy;
		}

		public void setStrategy(String strategy) {
			mStrategy = strategy;
		}

		public boolean isHeader() {
			return mHeader;
		}

		public void setHeader(boolean header) {
			mHeader = header;
		}

		public String getGenerator() {
			return mGenerator;
		}

		public void setGenerator(String generator) {
			mGenerator = generator;
		}

		public Map<String, String> getAuthProperties() {
			return mAuthProperties;
		}

		public void setAuthProperties(Map<String, String> authProperties) {
			mAuthProperties = authProperties;
		}

	}

}
