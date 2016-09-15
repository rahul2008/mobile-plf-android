/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.catalogapp.matcher;


import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class DrawableMatcher {

    public static Matcher<Drawable> isSameHeight(final int expectedHeight) {
        return new TypeSafeMatcher<Drawable>() {
            int actualHeight;

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                Rect bounds = drawable.getBounds();
                actualHeight = bounds.bottom - bounds.top;
                return expectedHeight == actualHeight;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Actual height " + actualHeight + " doesn't match expected height " + expectedHeight);
            }
        };
    }

    public static Matcher<Drawable> isSameWidth(final int expectedHeight) {
        return new TypeSafeMatcher<Drawable>() {
            int actualWidth;

            @Override
            protected boolean matchesSafely(Drawable drawable) {
                Rect bounds = drawable.getBounds();
                actualWidth = bounds.right - bounds.left;
                return expectedHeight == actualWidth;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Actual width " + actualWidth + " doesn't match expected width " + expectedHeight);
            }
        };
    }

}
