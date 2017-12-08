/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.tooltip.widget;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

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

    private boolean isOnePixelDifference() {
        final int actual = Integer.parseInt(this.actual.toString());
        final int expected = Integer.parseInt(this.expected.toString());
        if ((actual - expected) == 1 || (expected - actual == 1)) {
            return true;
        }
        return false;
    }
}