/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uid.matcher;

import android.view.View;

import com.philips.platform.uid.utils.UIDTestUtils;
import com.philips.platform.uid.view.widget.ProgressBar;

import org.hamcrest.Matcher;

public class ProgressBarMatcher {
    private static final String INDETERMINATE_DURATION = "mDuration";

    public static Matcher<View> isSameDuration(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof ProgressBar) {
                    int actual = UIDTestUtils.getIntFieldValueFromReflection(view, INDETERMINATE_DURATION);
                    setValues(actual, expectedValue);
                    return areEqual();
                }
                throw new RuntimeException("expected ProgressBar got " + view.getClass().getName());
            }
        };
    }
}
