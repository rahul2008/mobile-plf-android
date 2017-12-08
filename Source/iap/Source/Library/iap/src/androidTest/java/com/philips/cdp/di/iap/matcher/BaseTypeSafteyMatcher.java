package com.philips.cdp.di.iap.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class BaseTypeSafteyMatcher<T> extends TypeSafeMatcher<T> {
    Object actual;
    Object expected;

    @Override
    public void describeTo(Description description) {
        description.appendText("Actual=" + actual + " expected=" + expected);
    }

    protected void setValues(Object actual, Object expected) {
        this.actual = actual;
        this.expected = expected;
    }

    protected boolean areEqual() {
        return actual.equals(expected);
    }

    @Override
    protected boolean matchesSafely(T item) {
        return false;
    }
}