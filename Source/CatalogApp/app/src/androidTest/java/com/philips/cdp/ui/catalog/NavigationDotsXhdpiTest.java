package com.philips.cdp.ui.catalog;

import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.IsCircleRadiusAsExpectedMatcher.isCircleRadiusSimilar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class NavigationDotsXhdpiTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Resources testResources;

    public NavigationDotsXhdpiTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    public void testNDotFocusedUnfocusedRadiusAsExpected() {
        onView(withText("Dot Navigation")).perform(click());
        onView(withId(R.id.indicator))
                .check(matches(isCircleRadiusSimilar(12, 7)));
    }
}

