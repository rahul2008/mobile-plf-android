package com.philips.cdp.ui.catalog.Matchers;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class IsTextSizeAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsTextSizeAsExpectedMatcher";
    private float expectedSize;
    private float actualSize;

    public IsTextSizeAsExpectedMatcher(final Class<? extends View> expectedType, float expectedSize) {
        super(expectedType);
        this.expectedSize = expectedSize;
        this.actualSize = actualSize;
    }

    public static Matcher<View> isTextSizeSimilar(final float expectedSize) {
        return new IsTextSizeAsExpectedMatcher(View.class, expectedSize);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Actual Text Size differs when compared with expected Text size");
    }

    @Override
    public boolean matchesSafely(View view) {

        if (view instanceof TextView) {
            TextView actualTextview = (TextView) view;
            actualSize = actualTextview.getTextSize();
            if (Float.compare(actualTextview.getTextSize(), Math.round(expectedSize)) == 0) {
                return true;
            }
        }
        return false;
    }
}










