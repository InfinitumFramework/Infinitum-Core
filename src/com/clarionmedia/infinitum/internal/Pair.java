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

import java.util.Arrays;

/**
 * <p>
 * A generic, ordered pair data structure for holding two related objects.
 * </p>
 *
 * @author Tyler Treat
 * @version 1.0 02/25/12
 * @since 1.0
 */
public class Pair<F, S> {

    private F mFirst;
    private S mSecond;

    /**
     * Constructs a new {@code Pair} with the given values.
     *
     * @param first  the first {@code Pair} value
     * @param second the second {@code Pair} value
     */
    public Pair(F first, S second) {
        setFirst(first);
        setSecond(second);
    }

    /**
     * Retrieves the first value.
     *
     * @return first value
     */
    public F getFirst() {
        return mFirst;
    }

    /**
     * Sets the first value.
     *
     * @param first value to set
     */
    public void setFirst(F first) {
        mFirst = first;
    }

    /**
     * Retrieves the second value.
     *
     * @return second value
     */
    public S getSecond() {
        return mSecond;
    }

    /**
     * Sets the second value.
     *
     * @param second second value to set
     */
    public void setSecond(S second) {
        mSecond = second;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int hash = 7;
        if (mFirst.getClass().isArray())
            hash *= PRIME + Arrays.deepHashCode((Object[]) mFirst);
        else
            hash *= PRIME + mFirst.hashCode();
        if (mSecond.getClass().isArray())
            hash *= PRIME + Arrays.deepHashCode((Object[]) mSecond);
        else
            hash *= PRIME * mSecond.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass() != getClass())
            return false;
        @SuppressWarnings("rawtypes")
        Pair otherPair = (Pair) other;
        boolean first;
        if (mFirst.getClass().isArray() && otherPair.mFirst.getClass().isArray())
            first = Arrays.deepEquals((Object[]) mFirst, (Object[]) otherPair.mFirst);
        else
            first = mFirst.equals(otherPair.mFirst);
        boolean second;
        if (mSecond.getClass().isArray() && otherPair.mSecond.getClass().isArray())
            second = Arrays.deepEquals((Object[]) mSecond, (Object[]) otherPair.mSecond);
        else
            second = mSecond.equals(otherPair.mSecond);
        return first && second;
    }

    @Override
    public String toString() {
        return "[" + mFirst.toString() + ", " + mSecond.toString() + "]";
    }

}
