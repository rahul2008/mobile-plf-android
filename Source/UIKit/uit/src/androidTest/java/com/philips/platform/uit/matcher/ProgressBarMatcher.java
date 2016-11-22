/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.matcher;

import android.view.View;

import com.philips.platform.uit.utils.UITTestUtils;
import com.philips.platform.uit.view.widget.ProgressBar;

import org.hamcrest.Matcher;

public class ProgressBarMatcher {
    private static final String INDETERMINATE_DURATION = "mDuration";

    public static Matcher<View> isSameDuration(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof ProgressBar) {
                    int actual = UITTestUtils.getIntegerFieldValueFromReflection(view, INDETERMINATE_DURATION);
                    setValues(String.valueOf(actual), String.valueOf(expectedValue));
                    return actual == expectedValue;
                }
                throw new RuntimeException("expected ProgressBar got " + view.getClass().getName());
            }
        };
    }
}
