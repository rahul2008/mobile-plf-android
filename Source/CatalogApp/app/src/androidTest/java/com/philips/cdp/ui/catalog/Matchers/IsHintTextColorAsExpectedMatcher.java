package com.philips.cdp.ui.catalog.Matchers;

import android.graphics.Color;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class IsHintTextColorAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsHintTextColorAsExpectedMatcher";
    private String expectedColor;

    public IsHintTextColorAsExpectedMatcher(final Class<? extends View> expectedType, String expectedColor) {
        super(expectedType);
        this.expectedColor = expectedColor;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Actual Hinttextcolor   differs  when compared with expected hinttextcolor");
    }

    @Override
    public boolean matchesSafely(View view) {

        if (view instanceof TextView) {
            EditText actualTextview = (EditText) view;
            String actualcolor = "#"  +
                    Integer.toString(Color.red(actualTextview.getCurrentHintTextColor()), 16) +
                    Integer.toString(Color.green(actualTextview.getCurrentHintTextColor()), 16) +
                    Integer.toString(Color.blue(actualTextview.getCurrentHintTextColor()), 16);

            if (actualcolor.equalsIgnoreCase(expectedColor)) {
                return true;
            }
        }
        return false;
    }
    public static Matcher<View> isHintTextColorSimilar(final String expectedTextColor){
        return new IsHintTextColorAsExpectedMatcher(View.class, expectedTextColor);
    }
}
