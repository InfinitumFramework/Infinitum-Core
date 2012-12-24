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

package com.clarionmedia.infinitum.internal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * This class contains methods for formatting {@link Date} objects into Strings
 * and vice versa.
 * </p>
 * 
 * @author Tyler Treat
 * @version 1.0 02/13/12
 * @since 1.0
 */
public class DateFormatter {

	/**
	 * {@link SimpleDateFormat} containing the date pattern for the ISO 8601
	 * standard.
	 */
	public static final SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssZ");

	/**
	 * {@link SimpleDateFormat} containing the date pattern used by the HTTP
	 * Expires header.
	 */
	public static final SimpleDateFormat HTTP_EXPIRES_FORMAT = new SimpleDateFormat(
			"EEE, dd MMM yyyy HH:mm:ss z");

	/**
	 * Returns the {@link String} representation of the given {@link Date}.
	 * 
	 * @param date
	 *            the {@code Date} instance to retrieve the {@code String} for
	 * @return {@code String} for the specified {@code Date}
	 */
	public static String getDateAsISO8601String(Date date) {
		String result = ISO_8601_FORMAT.format(date);
		result = result.substring(0, result.length() - 2) + ":"
				+ result.substring(result.length() - 2);
		return result;
	}

	/**
	 * Parses the given {@link String} as a {@link Date} instance or
	 * {@code null} if it could not be parsed. Date {@code String} should be in
	 * ISO-8601 format.
	 * 
	 * @param input
	 *            the {@code String} to parse as a {@code Date}
	 * @return {@code Date} instance of {@code String}
	 */
	public static Date parseStringAsDate(String input) {
		try {
			return ISO_8601_FORMAT.parse(input);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Parses the given {@link String}, formatted as an HTTP Expires date, as a
	 * {@link Date} instance or {@code null} if it could not be parsed.
	 * 
	 * @param input
	 * @return
	 */
	public static Date parseHttpExpiresStringAsDate(String input) {
		try {
			return HTTP_EXPIRES_FORMAT.parse(input);
		} catch (ParseException e) {
			return null;
		}
	}

}
