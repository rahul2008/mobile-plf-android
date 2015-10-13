package com.philips.cdp.ui.catalog.Matchers;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.util.Log;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class IsEqualMatcher extends BoundedMatcher<TextView, TextView> {

    private float expectedFontSize;
    public static final String TAG = "IsEqual";

    public IsEqualMatcher(final Class<? extends TextView> expectedType, float expectedFontSize) {
        super(expectedType);
        this.expectedFontSize = expectedFontSize;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Actual height is not same as Expected height");
    }

    @Override
    public boolean matchesSafely(TextView view) {
        float actualFontSize = view.getTextSize();
        if (actualFontSize != expectedFontSize) {
            Log.d(TAG, "width or height is not as expected");
        }
        return false;
    }

    public static Matcher<TextView> isFontSimilar(final float expectedFontSize) {
        return new IsEqualMatcher(TextView.class, expectedFontSize);
    }

    public IsEqualMatcher matches(final Matcher<TextView> isFontSimilar) {
        return new IsEqualMatcher(TextView.class, expectedFontSize);
    }
}

