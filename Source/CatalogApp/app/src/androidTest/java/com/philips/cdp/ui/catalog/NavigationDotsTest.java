package com.philips.cdp.ui.catalog;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class NavigationDotsTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Resources testResources;

    public NavigationDotsTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    public void testFocusedNavigationDotsAsExpected() {
        onView(withText("Dot Navigation")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.dot_navigation_mdpi);
        onView(withId(R.id.indicator))
                .check(matches(IsPixelAsExpectedMatcher.isImageSimilar(expectedBitmap)));
    }
}
