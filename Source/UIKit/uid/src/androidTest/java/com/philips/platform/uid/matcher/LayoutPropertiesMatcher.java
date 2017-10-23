/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.matcher;


import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;


import com.philips.platform.uid.utils.UIDLog;

import org.hamcrest.Matcher;

import java.lang.reflect.Field;

public class LayoutPropertiesMatcher {

    public static Matcher<View> isSameOrientation(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                setValues(((LinearLayout) view).getOrientation(), expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<View> isSameGravity(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    setValues(((LinearLayout) view).getGravity(), expectedValue);
                } else {
                    int gravity = -1;

                    try {
                        final Field staticField = LinearLayout.class.getDeclaredField("mGravity");
                        staticField.setAccessible(true);
                        gravity = staticField.getInt((view));
                        setValues(gravity, expectedValue);
                    } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
                        UIDLog.e(e.getClass().getSimpleName(), e.getLocalizedMessage());
                        return false;
                    }
                }
                return areEqual();
            }
        };
    }
}
