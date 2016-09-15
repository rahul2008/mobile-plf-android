package com.philips.platform.catalogapp.matcher;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.philips.platform.uit.view.widget.Button;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class BackgroundDrawableMatchers {
    public static Matcher<View> isSameHeight(final int expectedHeight) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                return DrawableMatcher.isSameHeight(expectedHeight).matches(view.getBackground());
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }

    public static Matcher<View> isSameWidth(final int expectedWidth) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                return DrawableMatcher.isSameWidth(expectedWidth).matches(view.getBackground());
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }
}
