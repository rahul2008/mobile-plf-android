package com.philips.cdp.di.iap.matcher;

import android.view.View;
import android.widget.TextView;

import org.hamcrest.Matcher;

/**
 * Created by philips on 5/2/17.
 */

public class TextViewPropertiesMatchers {

    public static Matcher<View> isSameFontSize(final float expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof TextView) {
                    setValues(((TextView) view).getTextSize(), expectedValue);
                    return areEqual();
                }
                throw new RuntimeException("expected TextView got " + view.getClass().getName());
            }
        };
    }

    public static Matcher<View> isSameLineSpacing(final float expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof TextView) {
                    setValues(((TextView) view).getLineSpacingExtra(), expectedValue);
                    return areEqual();
                }
                throw new RuntimeException("expected TextView got " + view.getClass().getName());
            }
        };
    }
}
