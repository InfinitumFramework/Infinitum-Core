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

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import org.apache.http.impl.client.RequestWrapper;

import com.clarionmedia.infinitum.exception.InfinitumRuntimeException;

/**
 * <p>
 * Used for token-based/shared-secret authentication. A {@link TokenGenerator}
 * can be supplied to this class in order to implement a token-generation
 * policy.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 03/21/12
 */
public class SharedSecretAuthentication implements AuthenticationStrategy {

	private static final String ENCODING = "UTF-8";

	private String mTokenName;
	private String mToken;
	private TokenGenerator mGenerator;
	private boolean mIsHeader;

	@Override
	public void authenticate(RequestWrapper request) {
		if (isHeader()) {
			request.addHeader(getTokenName(), getToken());
		} else {
			String uri = request.getURI().toString();
			if (uri.contains("?"))
				uri += "&" + getTokenName() + "=" + getToken();
			request.setURI(URI.create(uri));
		}
	}

	/**
	 * Indicates whether or not request authentication is included as a header.
	 * If not a header, it is included as a query string parameter.
	 * 
	 * @return {@code true} if it's a header, {@code false} if not
	 */
	public boolean isHeader() {
		return mIsHeader;
	}

	/**
	 * Sets the value indicating whether or not request authentication is
	 * included as a header. If not a header, it is included as a query string
	 * parameter.
	 * 
	 * @param isHeader
	 *            value to set
	 */
	public void setHeader(boolean isHeader) {
		mIsHeader = isHeader;
	}

	/**
	 * Sets the token name. This value represents either the header name or the
	 * query string parameter name, depending on what form of authentication is
	 * used.
	 * 
	 * @param tokenName
	 *            the token name to use for authentication
	 */
	public void setTokenName(String tokenName) {
		try {
			mTokenName = URLEncoder.encode(tokenName, ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new InfinitumRuntimeException("Invalid encoding '" + ENCODING + "'");
		}
	}

	/**
	 * Returns the token name. This value represents either the header name or
	 * the query string parameter name, depending on what form of authentication
	 * is used.
	 */
	public String getTokenName() {
		return mTokenName;
	}

	/**
	 * Sets the token value. This value represents the header value or the query
	 * string parameter value, depending on what form of authentication is used.
	 * 
	 * @param token
	 *            the shared secret to use for authentication
	 */
	public void setToken(String token) {
		try {
			mToken = URLEncoder.encode(token, ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new InfinitumRuntimeException("Invalid encoding '" + ENCODING + "'");
		}
	}

	/**
	 * Returns the token value. This value represents the header value or the
	 * query string parameter value, depending on what form of authentication is
	 * used. This is either the value provided to
	 * {@link SharedSecretAuthentication#setToken(String)} or the value
	 * generated from the {@link TokenGenerator} if one has been provided.
	 * 
	 * @return shared secret
	 */
	public String getToken() {
		if (mGenerator != null)
			return mGenerator.generateToken();
		return mToken;
	}

	/**
	 * Registers a {@link TokenGenerator} for this
	 * {@code SharedSecretAuthentication} strategy. The {@code TokenGenerator}
	 * is responsible for creating shared secrets.
	 * 
	 * @param generator
	 *            the {@code TokenGenerator} to register
	 */
	public void setTokenGenerator(TokenGenerator generator) {
		mGenerator = generator;
	}

	/**
	 * Removes any registered {@link TokenGenerator} from this
	 * {@code SharedSecretAuthentication} strategy.
	 */
	public void clearTokenGenerator() {
		mGenerator = null;
	}

}
