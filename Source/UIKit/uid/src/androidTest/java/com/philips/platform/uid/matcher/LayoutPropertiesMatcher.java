/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.matcher;


import android.view.View;
import android.widget.LinearLayout;

import org.hamcrest.Matcher;

public class LayoutPropertiesMatcher {

    public static Matcher<View> isSameOrientation(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                setValues(((LinearLayout)view).getOrientation(), expectedValue);
                return areEqual();
            }
        };
    }
}
