/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Helps to minimize code writing in child classes by avoiding to over write below method
 *
 * @param <T>
 */
public abstract class BaseTypeSafteyMatcher<T> extends TypeSafeMatcher<T> {
    String actual;
    String expected;
    @Override
    public void describeTo(Description description) {
        description.appendText("Actual=" + actual + " expected=" + expected);
    }

    void setValues(String actual, String expected) {
        this.actual = actual;
        this.expected = expected;
    }
}