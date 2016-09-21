/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.catalogapp.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Helps to minimize code writing in child classes by avoiding to over write below method
 * @param <T>
 */
public abstract class BaseTypeSafteyMatcher<T> extends TypeSafeMatcher<T> {
    @Override
    public void describeTo(Description description) {

    }
}