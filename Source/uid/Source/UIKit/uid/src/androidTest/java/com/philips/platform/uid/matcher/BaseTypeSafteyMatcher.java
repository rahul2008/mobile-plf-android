/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uid.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Assert;

/**
 * Helps to minimize code writing in child classes by avoiding to over write below method
 *
 * @param <T>
 */
public abstract class BaseTypeSafteyMatcher<T> extends TypeSafeMatcher<T> {
    Object actual;
    Object expected;

    @Override
    public void describeTo(Description description) {
        description.appendText("Actual=" + actual + " expected=" + expected);
    }

    void setValues(Object actual, Object expected) {
        this.actual = actual;
        this.expected = expected;
    }

    protected boolean areEqual() {
        return actual.equals(expected);
    }

    protected boolean floatEqual(float delta) {
        try {
            Assert.assertEquals((float)expected, (float)actual, delta);
        } catch (AssertionError ae) {
            return false;
        }
        return true;
    }
}